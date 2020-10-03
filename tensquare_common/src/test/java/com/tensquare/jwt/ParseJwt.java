package com.tensquare.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.text.SimpleDateFormat;

public class ParseJwt {
    public static void main(String[] args) {
        Claims claims = Jwts.parser()
                .setSigningKey("itcast")
                .parseClaimsJws("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2NjY2NiIsInN1YiI6IndlaWJpbmd5YW5nIiwiaWF0IjoxNTUzODg4MDg5LCJyb2xlIjoiYWRtaW4ifQ.ZBjiAyII6rzJzsyNsYFSIviJlidI0mHvh2oJnvdwmmw")
                .getBody();
        System.out.println(claims.getId());
        System.out.println(claims.getSubject());
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(claims.getIssuedAt()));
        System.out.println(claims.get("role"));
    }
}
