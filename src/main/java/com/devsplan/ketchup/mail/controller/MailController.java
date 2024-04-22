package com.devsplan.ketchup.mail.controller;

import com.devsplan.ketchup.common.ResponseDTO;
import com.devsplan.ketchup.mail.service.MailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mails")
public class MailController {
//    private final MailService mailService;
//
//    public MailController(MailService mailService) {
//        this.mailService = mailService;
//    }
//
//    @GetMapping("/list")
//    public ResponseEntity<ResponseDTO> selectMailList() {
//
//        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "메일 목록 조회", mailService.selectMailList));
//    }
}
