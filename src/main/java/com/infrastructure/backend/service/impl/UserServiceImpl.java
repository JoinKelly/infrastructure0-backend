package com.infrastructure.backend.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infrastructure.backend.common.exception.CustomResponseStatusException;
import com.infrastructure.backend.common.exception.ErrorCode;
import com.infrastructure.backend.configuration.security.auth.TokenHelper;
import com.infrastructure.backend.entity.user.User;
import com.infrastructure.backend.model.common.request.UserAddition;
import com.infrastructure.backend.model.common.request.UserUpdateRequest;
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

import java.util.List;
import java.util.Optional;

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

    @Autowired
    private ObjectMapper objectMapper;

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

    @Override
    public User create(UserAddition user) {

        Optional<User> dbUserOptional = this.userRepository.findByUsername(user.getUsername());

        if (dbUserOptional.isPresent()) {
            throw new CustomResponseStatusException(HttpStatus.BAD_REQUEST, ErrorCode.USERNAME_EXIST.name(), "Username is exist");
        }

        dbUserOptional = this.userRepository.findByEmail(user.getEmail());

        if (dbUserOptional.isPresent()) {
            throw new CustomResponseStatusException(HttpStatus.BAD_REQUEST, ErrorCode.EMAIL_EXIST.name(), "Email is exist");
        }

        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(this.passwordEncoder.encode(user.getPassword()));
        newUser.setFullName(user.getFullName());
        newUser.setEmail(user.getEmail());

        return this.userRepository.save(newUser);
    }

    @Override
    public User update(int userId, UserUpdateRequest user) {

        User dbUser = this.userRepository.findById(userId).orElseThrow(() -> new CustomResponseStatusException(HttpStatus.NOT_FOUND, ErrorCode.USER_NOT_EXIST.name(), "User is not exist"));
        Optional<User> checkEmailUser = this.userRepository.findByEmail(user.getEmail());
        if (checkEmailUser.isPresent() && !dbUser.getId().equals(checkEmailUser.get().getId())) {
            throw new CustomResponseStatusException(HttpStatus.BAD_REQUEST, ErrorCode.EMAIL_EXIST.name(), "Email is exist");
        }
        dbUser.setFullName(user.getFullName());
        dbUser.setEmail(user.getEmail());
        return this.userRepository.save(dbUser);
    }

    @Override
    public User delete(int userId) {
        User dbUser = this.userRepository.findById(userId).orElseThrow(() -> new CustomResponseStatusException(HttpStatus.NOT_FOUND, ErrorCode.USER_NOT_EXIST.name(), "User is not exist"));
        this.userRepository.delete(dbUser);
        User deletedUser = new User();
        deletedUser.setId(dbUser.getId());
        return deletedUser ;
    }

    @Override
    public User find(int userId) {
        return this.userRepository.findById(userId).orElseThrow(() -> new CustomResponseStatusException(HttpStatus.NOT_FOUND, ErrorCode.USER_NOT_EXIST.name(), "User is not exist"));
    }

    @Override
    public List<User> findAll() {
        return this.userRepository.findAll();
    }
}
