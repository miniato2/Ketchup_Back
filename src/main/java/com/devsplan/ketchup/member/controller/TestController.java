package com.devsplan.ketchup.member.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@PreAuthorize("hasAuthority('LV1')")
public class TestController {


    @GetMapping("/test")
    public String test(){
        return "test GET";
    }



    @PostMapping("/test2")
    public String test2(){

        return "test POST";
    }


}
