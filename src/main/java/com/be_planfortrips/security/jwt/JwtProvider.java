package com.be_planfortrips.security.jwt;

import com.be_planfortrips.dto.OauthModel.TypeLogin;
import com.be_planfortrips.entity.Role;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.security.userPrincipal.CustomUserServiceDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
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


    public String createToken(String userIdentify, String role, TypeLogin typeLogin) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("loginType", typeLogin.name());
        claims.put("role", role);

        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userIdentify)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public Boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e){
            log.error("Expired JWT token -> Message: {}",e);
        } catch (MalformedJwtException e) {
            log.error("Invalid format token -> Message: {}",e);
        } catch (UnsupportedJwtException e){
            log.error("Unsupported JWT token -> Message: {}",e);
        } catch (IllegalArgumentException e){
            log.error("JWT claims string is empty -> Message: {}",e);
        }
        return false;
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
    }

    public String getRoleFromToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().get("role", String.class);
    }
}
