package com.infrastructure.backend.service.impl;

import com.infrastructure.backend.common.exception.CustomResponseStatusException;
import com.infrastructure.backend.common.exception.ErrorCode;
import com.infrastructure.backend.configuration.security.auth.TokenHelper;
import com.infrastructure.backend.entity.user.User;
import com.infrastructure.backend.model.common.response.CommonResponse;
import com.infrastructure.backend.model.user.CustomUserDetails;
import com.infrastructure.backend.model.user.request.ChangePassword;
import com.infrastructure.backend.model.user.request.UserLogin;
import com.infrastructure.backend.model.user.response.UserTokenState;
import com.infrastructure.backend.repository.UserRepository;
import com.infrastructure.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserServiceImpl implements UserService {

    private Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Value("${jwt.expires_in}")
    private int EXPIRES_IN;

    @Autowired
    private UserRepository userRepository;

    @Lazy
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenHelper tokenHelper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserTokenState login(UserLogin userLogin) {
        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userLogin.getUsername(),
                            userLogin.getPassword()
                    )
            );
        } catch (ResponseStatusException e) {
            LOG.error("Error", e);

            throw e;
        } catch (Exception e) {

            LOG.error("Error", e);
            throw new CustomResponseStatusException(HttpStatus.NOT_FOUND, ErrorCode.USER_NOT_EXIST.name(), "User does not exist");
        }

        // Inject into security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        // Return the token
        String jws = tokenHelper.generateToken(customUserDetails.getUsername(), customUserDetails);
        return new UserTokenState(jws, EXPIRES_IN);
    }

    @Override
    public UserTokenState refreshToken(String token) {
        String jws = tokenHelper.refreshToken(token);
        return new UserTokenState(jws, EXPIRES_IN);
    }

    @Override
    public CommonResponse changePassword(String token, ChangePassword changePasswordJson) {
        if (!changePasswordJson.getNewPassword().equals(changePasswordJson.getConfirmPassword())) {
            throw new CustomResponseStatusException(HttpStatus.BAD_REQUEST, ErrorCode.PASSWORD_NOT_MATCH.name(), "New password and Confirm password is not matching");
        }

        String username = tokenHelper.getUsernameFromToken(token);

        if (username != null) {
            User user = this.userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
            if (!this.passwordEncoder.matches(changePasswordJson.getCurrentPassword(), user.getPassword())) {
                throw new CustomResponseStatusException(HttpStatus.BAD_REQUEST, ErrorCode.PASSWORD_WRONG.name(), "Current password is wrong");
            }

            user.setPassword(passwordEncoder.encode(changePasswordJson.getNewPassword()));
            this.userRepository.save(user);
            return new CommonResponse("Change password success", null);

        } else {
            throw new CustomResponseStatusException(HttpStatus.NOT_FOUND, ErrorCode.USERNAME_NOT_EXIST.name(), "Username is not exist");
        }
    }
}
