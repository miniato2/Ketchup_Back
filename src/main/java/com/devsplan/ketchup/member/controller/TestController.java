package com.devsplan.ketchup.member.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;


@RestController
@PreAuthorize("hasAuthority('LV1')")
public class TestController {

    @Value("${jwt.key}")
    private String jwtSecret;


    @GetMapping("/test")
    public String test(@RequestHeader("Authorization") String token){
        // "Bearer " 이후의 토큰 값만 추출
        String jwtToken = token.substring(7);

        // 토큰 파싱하여 클레임 추출
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwtToken).getBody();

        // 클레임에서 depNo 추출
        int depNo = claims.get("depNo", Integer.class);

        return "test GET: " + depNo;
    }



    @PostMapping("/test2")
    public String test2(){

        return "test POST";
    }


}
