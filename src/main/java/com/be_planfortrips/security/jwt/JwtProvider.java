package com.be_planfortrips.security.jwt;

import com.be_planfortrips.dto.OauthModel.TypeLogin;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtProvider {

    private final String secretKey = "planfortripsdinhtangductiendangthanhhunghominhnhuthuynhanhquanhuynhhaonamnguyenanhtai";
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
                .signWith(SignatureAlgorithm.HS256, secretKey)
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