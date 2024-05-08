package com.devsplan.ketchup.mail.controller;

import com.devsplan.ketchup.common.Criteria;
import com.devsplan.ketchup.common.PageDTO;
import com.devsplan.ketchup.common.PagingResponseDTO;
import com.devsplan.ketchup.common.ResponseDTO;
import com.devsplan.ketchup.mail.dto.MailDTO;
import com.devsplan.ketchup.mail.dto.MailFileDTO;
import com.devsplan.ketchup.mail.dto.ReceiverDTO;
import com.devsplan.ketchup.mail.service.MailService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static com.devsplan.ketchup.util.TokenUtils.decryptToken;

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
    public ResponseEntity<ResponseDTO> insertMail(@RequestHeader("Authorization") String token,
                             @RequestPart("mailInfo") MailDTO mailDto,
                             @RequestPart("mailFile") MultipartFile mailFile){
        // 사원 번호
        String memberNo = decryptToken(token).get("memberNo", String.class);

        // 메일 정보
        mailDto.setSenderMem(memberNo);
        mailDto.setSendCancelStatus('N');
        mailDto.setSendDelStatus('N');

        int sendMailNo = mailService.insertMail(mailDto);

        if(sendMailNo == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO("메일 전송 실패"));
        }else {
            for(int i = 0; i < mailDto.getReceivers().size(); i++) {
                mailDto.getReceivers().get(i).setMailNo(sendMailNo);
                mailDto.getReceivers().get(i).setReceiverDelStatus('N');
            }

            mailService.insertReceiver(mailDto.getReceivers());

            if(mailFile != null) {
                mailService.insertMailFile(sendMailNo, mailFile);
            }

            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "메일 전송 성공", null));
        }
    }

    // 메일 조회 + 검색
    @GetMapping
    public ResponseEntity<ResponseDTO> selectMailList(@RequestHeader("Authorization") String token,
                                                      @RequestParam("part") String partValue,
                                                      @RequestParam(value = "search", defaultValue = "") String search,
                                                      @RequestParam(value = "searchvalue", defaultValue = "") String searchValue) {
        // 사원 번호
        String memberNo = decryptToken(token).get("memberNo", String.class);

        String result = "";
        List<MailDTO> mailList = null;
        if(partValue.equals("send")) {
            mailList = mailService.selectSendMailList(memberNo, search, searchValue);
            result = "보낸 메일 조회";
        }else if(partValue.equals("receive")) {
            mailList = mailService.selectReceiveMailList(memberNo, search, searchValue);
            result = "받은 메일 조회";
        }

        if(mailList != null) {
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, result + "성공", mailList));
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(result + "실패"));
        }
    }

    // 메일 조회 + 검색 + 페이지
//    @GetMapping
//    public ResponseEntity<ResponseDTO> selectMailList(@RequestHeader("Authorization") String token,
//                                                      @RequestParam("part") String partValue,
//                                                      @RequestParam(value = "search", defaultValue = "") String search,
//                                                      @RequestParam(value = "searchvalue", defaultValue = "") String searchValue,
//                                                      @RequestParam(name = "offset", defaultValue = "1") String offset) {
//        // 사원 번호
//        String memberNo = decryptToken(token).get("memberNo", String.class);
//
//        Criteria cri = new Criteria(Integer.valueOf(offset),10);
//        PagingResponseDTO pagingResponseDTO = new PagingResponseDTO();
//
//        String result = "";
//        Page<MailDTO> mailList = null;
//        if(partValue.equals("send")) {
//            mailList = mailService.selectSendMailList(memberNo, search, searchValue, cri);
//            pagingResponseDTO.setData(mailList);
//            pagingResponseDTO.setPageInfo(new PageDTO(cri, (int) mailList.getTotalElements()));
//
//            System.out.println("🍕🍕🍕🍕🍕🍕🍕🍕🍕🍕🍕🍕🍕🍕🍕🍕🍕🍕🍕🍕🍕🍕🍕🍕🍕🍕🍕");
//            System.out.println(mailList);
//
//            result = "보낸 메일 조회";
//        }else if(partValue.equals("receive")) {
////            mailList = mailService.selectReceiveMailList(memberNo, search, searchValue, cri);
////            pagingResponseDTO.setData(mailList);
////            pagingResponseDTO.setPageInfo(new PageDTO(cri, (int) mailList.getTotalElements()));
//
//            result = "받은 메일 조회";
//        }
//
//        if(mailList != null) {
//            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, result + "성공", mailList));
//        }else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(result + "실패"));
//        }
//    }

    @GetMapping("/{mailNo}")
    public ResponseEntity<ResponseDTO> selectMailDetail(@PathVariable int mailNo) {
        MailDTO oneMail = mailService.selectMailDetail(mailNo);

        if(oneMail != null) {
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "상세 조회 성공", oneMail));
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO("메일 상세 조회 실패"));
        }
    }

    @PutMapping("/{mailNo}")
    public ResponseEntity<ResponseDTO> cancelSendMail(@PathVariable int mailNo) {
        int result = mailService.cancelSendMail(mailNo);

        if(result > 0) {
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "메일 발송 취소 성공", result));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO("메일 발송 취소 실패"));
        }
    }

    @PutMapping
    public ResponseEntity<ResponseDTO> deleteMail(@RequestParam("part") String partValue, @RequestParam("mailno") List<Integer> mailNo) {
        String result = "";
        int delResult = 0;
        if(partValue.equals("send")) {
            delResult = mailService.deleteSendMail(mailNo);
            result = "보낸 메일 삭제";
        }else if(partValue.equals("receive")) {
            delResult = mailService.deleteReceiveMail(mailNo);
            result = "받은 메일 삭제";
        }

        if(delResult > 0) {
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, result + "성공", null));
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(result + "실패"));
        }
    }

    @PostMapping("/{mailNo}/reply")
    public ResponseEntity<ResponseDTO> replyMail(@RequestHeader("Authorization") String token,
                            @PathVariable int mailNo,
                            @RequestPart("mailInfo") MailDTO mailDto,
                            @RequestPart("mailFile") MultipartFile mailFile){
        // 사원 번호
        String memberNo = decryptToken(token).get("memberNo", String.class);

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

        if(replyMailNo == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO("메일 답장 실패"));
        }else {
            List<ReceiverDTO> replyReceivers = new ArrayList<>();
            ReceiverDTO replyReceiver = new ReceiverDTO(
                    replyMailNo,
                    prevMail.getSenderMem(),
                    'N'
            );

            replyReceivers.add(replyReceiver);

            mailService.insertReceiver(replyReceivers);

            if(mailFile != null) {
                mailService.insertMailFile(replyMailNo, mailFile);
            }

            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "메일 답장 성공", null));
        }







    }
}
