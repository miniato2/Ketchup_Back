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

import static com.devsplan.ketchup.util.TokenUtils.decryptToken;


@RestController
@PreAuthorize("hasAuthority('LV1')")
public class TestController {




    @GetMapping("/test")
    public String test(@RequestHeader("Authorization") String token){

        decryptToken(token);


        int depNo = decryptToken(token).get("depNo", Integer.class);

        return "test GET: " + depNo;
    }



    @PostMapping("/test2")
    public String test2(){

        return "test POST";
    }


}
