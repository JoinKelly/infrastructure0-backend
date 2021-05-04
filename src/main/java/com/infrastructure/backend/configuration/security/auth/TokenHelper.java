package com.infrastructure.backend.configuration.security.auth;

import com.infrastructure.backend.common.exception.CustomResponseStatusException;
import com.infrastructure.backend.common.exception.ErrorCode;
import com.infrastructure.backend.entity.user.User;
import com.infrastructure.backend.model.user.CustomUserDetails;
import com.infrastructure.backend.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenHelper {

    Logger logger = LoggerFactory.getLogger(TokenHelper.class);

    @Value("${spring.application.name}")
    private String APP_NAME;

    @Value("${jwt.secret}")
    public String SECRET;

    @Value("${jwt.expires_in}")
    private int EXPIRES_IN;

    @Value("${jwt.header}")
    private String AUTH_HEADER;

    @Autowired
    private UserRepository userRepository;

    private static final String AUDIENCE = "AUDIENCE";

    private SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

    public String getUsernameFromToken(String token) {
        String username;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            username = claims.get("username").toString();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    public Date getIssuedAtDateFromToken(String token) {
        Date issueAt;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            issueAt = claims.get("issuedAt", Date.class);
        } catch (Exception e) {
            issueAt = null;
        }
        return issueAt;
    }

    public String getAudienceFromToken(String token) {
        String audience;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            audience = claims.getAudience();
        } catch (Exception e) {
            audience = null;
        }
        return audience;
    }

    public String refreshToken(String token) {
        String refreshedToken;
        try {
            Date now = new Date();
            final Claims claims = this.getAllClaimsFromToken(token);
            claims.put("issuedAt", now);
            claims.put("expireAt", now.getTime() + EXPIRES_IN * 1000);
            refreshedToken = Jwts.builder()
                    .setClaims(claims)
                    .setExpiration(new Date(now.getTime() + EXPIRES_IN * 1000))
                    .signWith(SIGNATURE_ALGORITHM, SECRET)
                    .compact();
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    public String generateToken(String username, CustomUserDetails customUserDetails) {
        Date now = new Date();
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        claims.put("email", customUserDetails.getEmail());
        claims.put("name", customUserDetails.getFullName());
        claims.put("issuedAt", now);
        claims.put("expireAt", now.getTime() + EXPIRES_IN * 1000);
        return Jwts.builder()
                .setIssuer(APP_NAME)
                .setSubject(username)
                .setAudience(AUDIENCE)
                .setIssuedAt(now)
                .setClaims(claims)
                .setExpiration(new Date(now.getTime() + EXPIRES_IN * 1000))
                .signWith(SIGNATURE_ALGORITHM, SECRET)
                .compact();
    }

    public String generateToken(String username, User user) {
        String audience = AUDIENCE;
        Date now = new Date();
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        claims.put("email", user.getEmail());
        claims.put("name", user.getFullName());
        claims.put("issuedAt", now);
        claims.put("expireAt", now.getTime() + EXPIRES_IN * 1000);
        return Jwts.builder()
                .setIssuer(APP_NAME)
                .setSubject(username)
                .setAudience(audience)
                .setIssuedAt(now)
                .setClaims(claims)
                .setExpiration(new Date(now.getTime() + EXPIRES_IN * 1000))
                .signWith(SIGNATURE_ALGORITHM, SECRET)
                .compact();
    }

    private Claims getAllClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            logger.error("Error", e);

            claims = null;
        }
        return claims;
    }

    public Boolean validateToken(String token, CustomUserDetails customUserDetails) {
        final String username = getUsernameFromToken(token);
        final Date created = getIssuedAtDateFromToken(token);
        return (
                username != null &&
                        username.equals(customUserDetails.getUsername())
        );
    }

    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    public String getToken(HttpServletRequest request) {
        /**
         *  Getting the token from Authentication header
         *  e.g Bearer your_token
         */
        String authHeader = getAuthHeaderFromHeader(request);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }

    public String getToken(String authHeader) {
        /**
         *  Getting the token from Authentication header
         *  e.g Bearer your_token
         */
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }

    public String getAuthHeaderFromHeader(HttpServletRequest request) {
        return request.getHeader(AUTH_HEADER);
    }

    public User getUserFromToken(String token) throws CustomResponseStatusException {
        String username = this.getUsernameFromToken(token);
        return this.userRepository.findByUsername(username).orElseThrow(() -> new CustomResponseStatusException(HttpStatus.NOT_FOUND, ErrorCode.USERNAME_NOT_EXIST.name(), "Username is not exist"));
    }

}