package com.devsplan.ketchup.mail.controller;

import com.devsplan.ketchup.mail.dto.MailDTO;
import com.devsplan.ketchup.mail.service.MailService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MailController {
    @Value("${jwt.key}")
    private String jwtSecret;

    private final MailService mailService;

    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping("/mails")
    public String insertMail(@RequestHeader("Authorization") String token,
                             @RequestBody MailDTO mailDto){
        // 사원 번호
        String jwtToken = token.substring(7);
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwtToken).getBody();
        String memberNo = claims.get("memberNo", String.class);

        // 메일 정보
        mailDto.setSenderMem(memberNo);
        mailDto.setSendCancelStatus('N');
        mailDto.setSendDelStatus('N');

        int sendMailNo = mailService.insertMail(mailDto);

        // 수신자 정보
        for(int i = 0; i < mailDto.getReceivers().size(); i++) {
            mailDto.getReceivers().get(i).setMailNo(sendMailNo);
            mailDto.getReceivers().get(i).setReceiverDelStatus('N');
        }

        mailService.insertReceiver(mailDto.getReceivers());

        return "Mail Send!!";
    }

    @GetMapping("/mails")
    public String selectMailList(@RequestHeader("Authorization") String token, @RequestParam("part") String partValue) {
        // 사원 번호
        String jwtToken = token.substring(7);
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwtToken).getBody();
        String memberNo = claims.get("memberNo", String.class);

        String result = "";
        if(partValue.equals("send")) {
            List<MailDTO> sendMailList = mailService.selectSendMailList(memberNo);
            for(MailDTO list : sendMailList) {
                System.out.println("보낸 메일" + list);
                result = "Send Mail List!!";
            }
        }else if(partValue.equals("receive")) {
            List<MailDTO> receiveMailList = mailService.selectReceiveMailList(memberNo);
            for(MailDTO list : receiveMailList) {
                System.out.println("받은 메일" + list);
                result = "Receive Mail List!!";
            }
        }

        return result;
    }
}
