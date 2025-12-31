package org.example.securityapp.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtProvider {

    private static final String SECRET =
            "this-is-a-very-long-secret-key-for-hs256-signature";

    private static final long EXPIRATION_TIME = 1000 * 60 * 60;

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String createToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("roles",
                        userDetails.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .toList()
                )
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);

        List<GrantedAuthority> authorities =
                ((List<?>) claims.get("roles")).stream()
                        .map(role -> (GrantedAuthority)
                                new SimpleGrantedAuthority(role.toString()))
                        .toList();

        UserDetails userDetails =
                new org.springframework.security.core.userdetails.User(
                        claims.getSubject(),
                        "",
                        authorities
                );

        return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                authorities
        );
    }

    public String createRefreshToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getSubject(String token){
        return parseClaims(token).getSubject();
    }

    public UserDetails getUserDetails(String token){
        Claims claims = parseClaims(token);
        List<SimpleGrantedAuthority> authorities =
                ((List<?>) claims.get("roles")).stream()
                        .map(role -> new SimpleGrantedAuthority(role.toString()))
                        .toList();

        return new User(
                claims.getSubject(),
                "",
                authorities
        );
    }
}