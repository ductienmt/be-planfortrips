package com.be_planfortrips.security.jwt;

import com.be_planfortrips.entity.Role;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.security.userPrincipal.CustomUserServiceDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtProvider {
    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private final int jwtExpiration = 86400 * 1000;
    @Autowired
    private CustomUserServiceDetails userDetailsService;

    public String createToken(String username, String role) {
        // Tạo một instance Claims mới với username và role
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);

        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }


    public Boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("JWT token không hợp lệ: " + e.getMessage());
            throw new AppException(ErrorType.internalServerError);
        }
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUsernameFromToken(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}