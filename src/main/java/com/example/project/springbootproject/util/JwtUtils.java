package com.example.project.springbootproject.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {
    private static final String key = "1234567890";

    public static String createJwt(String username) {

        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

        Map<String, Object> payloads = new HashMap<>();
        payloads.put("username", username);

        String jwt = Jwts.builder()
            .setHeader(headers)
            .setClaims(payloads)
            .signWith(SignatureAlgorithm.HS256, key.getBytes())
            .compact();

        return jwt;
    }

    public static Map<String, Object> checkJwt(String jwt) throws UnsupportedEncodingException {
        Map<String, Object> claimMap = null;

        try {
            Claims claims = Jwts.parser()
                .setSigningKey(key.getBytes("UTF-8"))
                .parseClaimsJws(jwt)
                .getBody();
            claimMap = claims;
        } catch (ExpiredJwtException e) {
            System.out.println(e);
        } catch (Exception e) {
            System.out.println(e);
        }
        return claimMap;
    }
}
