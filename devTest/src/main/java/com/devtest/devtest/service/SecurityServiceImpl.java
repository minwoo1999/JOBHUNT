package com.devtest.devtest.service;


import java.security.Key;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class SecurityServiceImpl implements SecurityService {


    private static String secretKey = "4C8kum4LxyKWYLM78sKdXrzb8jDCFyfX";

    @Override
    public String createToken(String subject, long ttlMillis) {
        // TODO Auto-generated method stub
//		System.out.println(subject);
        if (ttlMillis == 0) {
            throw new RuntimeException("토큰 만료기간은 0이상 이어야합니다.");
        }

        //HS256
        System.out.println("HS256 서명알고리즘으로 토큰 생성중");
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySeBytes = DatatypeConverter.parseBase64Binary(secretKey);
        Key signingKey = new SecretKeySpec(apiKeySeBytes, signatureAlgorithm.getJcaName());

        JwtBuilder builder = Jwts.builder()
                .setSubject(subject)
                .signWith(signatureAlgorithm, signingKey);

        long nowMillis = System.currentTimeMillis();
        builder.setExpiration(new Date(nowMillis + ttlMillis));
        return builder.compact();
    }

    //token

    @Override
    public String getSubject(String token) {
        // TODO Auto-generated method stub

        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
                .parseClaimsJws(token).getBody();

        return claims.getSubject();
    }

    @Override
    public String jwtExpireCheck(int time) {
        int CurrentTime = (int) ((new Date().getTime()));

        if (time >= CurrentTime) {
            System.out.println("유효시간 남음!_!_!_!_!");
            return "true";
        } else {
            System.out.println("유효시간 끝남!_!_!_!_!_!_!_!_!_!_!_!_!_!_!_!_!_!_!");
            return "false";
        }


    }
}
