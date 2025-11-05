package com.inmobiliaria.backend.jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private static final String clave = "dGhpc2lzYXNlY3JldGtleXRoaXNpc2FzZWNyZXRrZXk"; //esto debería ir guardado en un lugar seguro

    public String getToken(UserDetails usuario) {
        return getToken(new HashMap<>(), usuario);
    }

    @SuppressWarnings("deprecation")
    private String getToken(Map<String, Object> extraClaims, UserDetails usuario) {
        return Jwts
        .builder()
        .setClaims(extraClaims)
        .setSubject(usuario.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + 1000*60*24))
        .signWith(getKey(), SignatureAlgorithm.HS256)
        .compact();
    }

    private Key getKey() {
        byte[] keyBytes=Decoders.BASE64.decode(clave);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public boolean isTokenValido(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername())&& !isTokenExpirado(token));
    }

    @SuppressWarnings("deprecation")
    private Claims getAllClaims(String token){
        return Jwts
        .parser()
        .setSigningKey(getKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
    }

    public <T> T getClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Date getExpiracion(String token){
        return getClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpirado(String token){
        return getExpiracion(token).before(new Date());
    }

}
