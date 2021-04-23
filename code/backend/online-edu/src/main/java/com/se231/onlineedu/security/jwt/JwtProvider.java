package com.se231.onlineedu.security.jwt;

import com.se231.onlineedu.exception.BeforeStartException;
import com.se231.onlineedu.exception.NotFoundException;
import com.se231.onlineedu.exception.ValidationException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultClock;
import org.jsoup.helper.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * JwtAuthTokenFilter class
 *
 * provide jwt
 *
 * @author Yuxuan Liu
 *
 * @date 2019/07/01
 */
@Component
public class JwtProvider {
    private Clock clock = DefaultClock.INSTANCE;

    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    private String jwtSecret = "jwtSecret";

    private int jwtExpiration = 68400000;

    public String generateJwtToken(UserDetails userPrincipal) {
        Date createdDate = clock.now();
        Date expirationDate = new Date(createdDate.getTime() + jwtExpiration);

        if(StringUtil.isBlank(userPrincipal.getUsername())) {
            throw new NotFoundException("username cannot be null.");
        }
        if(userPrincipal.isEnabled() == false){
            throw new ValidationException("account hasn't been activated.");
        }
        if(expirationDate.before(createdDate)){
            throw new BeforeStartException("expiration date is before created date.");
        }
        if(StringUtil.isBlank(jwtSecret)){
            throw new ValidationException("Empty secret exception.");
        }
        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        if(StringUtil.isBlank(token)){
            throw new ValidationException("Empty token exception.");
        }
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        }   catch (MalformedJwtException e) {
            logger.error("Invalid JWT token -> Message: {}", e);
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token -> Message: {}", e);
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty -> Message: {}", e);
        }

        return false;
    }
}
