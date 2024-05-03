package com.devsplan.ketchup.mail.controller;

import com.devsplan.ketchup.mail.dto.MailDTO;
import com.devsplan.ketchup.mail.dto.MailFileDTO;
import com.devsplan.ketchup.mail.dto.ReceiverDTO;
import com.devsplan.ketchup.mail.service.MailService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/mails")
public class MailController {
    @Value("${jwt.key}")
    private String jwtSecret;

    private final MailService mailService;

    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping
    public String insertMail(@RequestHeader("Authorization") String token,
                             @RequestPart("mailInfo") MailDTO mailDto,
                             @RequestPart("mailFile") MultipartFile mailFile){
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

        // 첨부 파일 정보
        mailService.insertMailFile(sendMailNo, mailFile);

        return "Send Mail!!";
    }

    // 메일 조회 + 검색
    @GetMapping
    public String selectMailList(@RequestHeader("Authorization") String token,
                                 @RequestParam("part") String partValue,
                                 @RequestParam(value = "search", required = false) String search,
                                 @RequestParam(value = "searchvalue", required = false) String searchValue) {
        // 사원 번호
        String jwtToken = token.substring(7);
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwtToken).getBody();
        String memberNo = claims.get("memberNo", String.class);

        String result = "";
        List<MailDTO> mailList;
        if(partValue.equals("send")) {
            mailList = mailService.selectSendMailList(memberNo, search, searchValue);
            for(MailDTO list : mailList) {
                System.out.println("보낸 메일" + list);
                result = "Send Mail List!!";
            }
        }else if(partValue.equals("receive")) {
            mailList = mailService.selectReceiveMailList(memberNo, search, searchValue);
            for(MailDTO list : mailList) {
                System.out.println("받은 메일" + list);
                result = "Receive Mail List!!";
            }
        }

        return result;
    }

    @GetMapping("/{mailNo}")
    public String selectMailDetail(@PathVariable int mailNo) {
        MailDTO oneMail = mailService.selectMailDetail(mailNo);

        System.out.println(oneMail);

        return "메일 상세 조회 성공!!";
    }

    @PutMapping("/{mailNo}")
    public String cancelSendMail(@PathVariable int mailNo) {
        int result = mailService.cancelSendMail(mailNo);

        return result == 0 ? "발송 취소 실패..." : "발송 취소 성공!!";
    }

    @PutMapping
    public String deleteMail(@RequestParam("part") String partValue, @RequestParam("mailno") List<Integer> mailNo) {
        String result = "";
        if(partValue.equals("send")) {
            // 보낸 메일 삭제
            mailService.deleteSendMail(mailNo);

            result = "보낸 메일을 삭제했습니다.";
        }else if(partValue.equals("receive")) {
            // 받은 메일 삭제
            mailService.deleteReceiveMail(mailNo);

            result = "받은 메일을 삭제했습니다.";
        }

        return result;
    }

    @PostMapping("/{mailNo}/reply")
    public String replyMail(@RequestHeader("Authorization") String token,
                            @PathVariable int mailNo,
                            @RequestPart("mailInfo") MailDTO mailDto,
                            @RequestPart("mailFile") MultipartFile mailFile){
        // 사원 번호
        String jwtToken = token.substring(7);
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwtToken).getBody();
        String memberNo = claims.get("memberNo", String.class);

        // 이전 메일 가져오기
        MailDTO prevMail = mailService.selectMailDetail(mailNo);

        MailDTO replyMail = new MailDTO(
                memberNo,
                "RE:" + prevMail.getMailTitle(),
                mailDto.getMailContent() + prevMail.getMailContent(),
                'N',
                'N'
        );

        int replyMailNo = mailService.replyMail(replyMail);

        List<ReceiverDTO> replyReceivers = new ArrayList<>();
        ReceiverDTO replyReceiver = new ReceiverDTO(
                replyMailNo,
                prevMail.getSenderMem(),
                'N'
        );

        replyReceivers.add(replyReceiver);

        mailService.insertReceiver(replyReceivers);

        mailService.insertMailFile(replyMailNo, mailFile);

        return "메일 답장 성공!!";
    }
}
