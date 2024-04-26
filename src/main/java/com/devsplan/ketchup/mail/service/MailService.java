package com.devsplan.ketchup.mail.service;

import com.devsplan.ketchup.mail.dto.MailDTO;
import com.devsplan.ketchup.mail.dto.ReceiverDTO;
import com.devsplan.ketchup.mail.entity.Mail;
import com.devsplan.ketchup.mail.entity.Receiver;
import com.devsplan.ketchup.mail.repository.MailRepository;
import com.devsplan.ketchup.mail.repository.ReceiverRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MailService {
    private final MailRepository mailRepository;
    private final ReceiverRepository receiverRepository;

    public MailService(MailRepository mailRepository, ReceiverRepository receiverRepository) {
        this.mailRepository = mailRepository;
        this.receiverRepository = receiverRepository;
    }

    @Transactional
    public void insertMail(MailDTO mailInfo, ReceiverDTO receiverInfo) {
        Mail mail = new Mail(
                mailInfo.getMailNo(),
                mailInfo.getSenderMem(),
                mailInfo.getMailTitle(),
                mailInfo.getMailContent(),
                mailInfo.getSendCancelStatus(),
                mailInfo.getSendDelStatus()
        );

        mailRepository.save(mail);

        Receiver receiver = new Receiver(
                receiverInfo.getReceiverNo(),
                receiverInfo.getMailNo(),
                receiverInfo.getReceiverMem(),
                receiverInfo.getReadTime(),
                receiverInfo.getReceiverDelStatus()
        );

        receiverRepository.save(receiver);
    }

//    @Transactional
//    public void insertReceiver(ReceiverDTO receiverInfo) {
//
//    }

    @Transactional
    public List<MailDTO> selectSendMailList(int senderMem) {
        List<Mail> mailList = mailRepository.findBySenderMem(senderMem);

        return mailList.stream()
                .map(mail -> new MailDTO(
                        mail.getMailNo()
                        , mail.getSenderMem()
                        , mail.getMailTitle()
                        , mail.getMailContent()
                        , mail.getSendCancelStatus()
                        , mail.getSendDelStatus()
                ))
                .toList();

//        for(int i = 0; i < mailList.size(); i++) {
//            if(mailList.get(i).getSendDelStatus() != 'Y') {
//                return mailList.stream()
//                        .map(mail -> new MailDTO(
//                                mail.getMailNo()
//                                , mail.getSenderMem()
//                                , mail.getMailTitle()
//                                , mail.getMailContent()
//                                , mail.getSendCancelStatus()
//                                , mail.getSendDelStatus()
//                        ))
//                        .toList();
//            }
//        }
    }

    public List<MailDTO> selectReceiveMailList(int receiverTest) {
        List<Receiver> receiveList = receiverRepository.findByReceiverMem(receiverTest);

        List<ReceiverDTO> result =
                receiveList.stream()
                .map(receiver -> new ReceiverDTO(
                        receiver.getReceiverNo(),
                        receiver.getMailNo(),
                        receiver.getReceiverMem(),
                        receiver.getReadTime(),
                        receiver.getReceiverDelStatus()
                ))
                .toList();

        List<MailDTO> mailList = new ArrayList<>();

        for(int i = 0; i < result.size(); i++) {
            System.out.println("음하하하하하하하하하");
            int mailNo = result.get(i).getMailNo();
            Mail receiveMail = mailRepository.findByMailNo(mailNo);
            MailDTO mail = new MailDTO(
                    receiveMail.getMailNo(),
                    receiveMail.getSenderMem(),
                    receiveMail.getMailTitle(),
                    receiveMail.getMailContent(),
                    receiveMail.getSendCancelStatus(),
                    receiveMail.getSendDelStatus()
            );

            mailList.add(mail);
        }

        return mailList;
    }

    public MailDTO selectMailDetail(int mailNo) {
        Mail mailDetail = mailRepository.findByMailNo(mailNo);

        return new MailDTO(
                mailDetail.getMailNo()
                , mailDetail.getSenderMem()
                , mailDetail.getMailTitle()
                , mailDetail.getMailContent()
                , mailDetail.getSendCancelStatus()
                , mailDetail.getSendDelStatus()
        );
    }

    @Transactional
    public int cancelSendMail(int mailNo) {
        List<Receiver> mailRead = receiverRepository.findReadTime(mailNo);

        int result = 0;
        for(int i = 0; i < mailRead.size(); i++) {
            System.out.println("읽은 메일 확인 : " + mailRead.get(i).getReadTime());
            if(mailRead.get(i).getReadTime() != null) {
                // 수신자 중 한명이라도 메일을 읽었을 경우
                result = 0;
            }else {
                // 수신자 모두 메일을 읽지 않았을 경우 - 발송 취소 여부 'Y' / 수신자 삭제 여부 'Y' 변경
                result = mailRepository.cancelSendMail(mailNo);
//                System.out.println("으아아아아아아아아아악");
//                System.out.println(mailRead.get(i).getMailNo());
//                deleteReceiveMail(mailNo);
            }
        }

        return result;
    }

    @Transactional
    public int deleteSendMail(int mailNo, int senderMem) {
        int result = mailRepository.updateBySendDelStatus(mailNo, senderMem);
        System.out.println("발신 삭제 : " + result);

        return result;
    }

    @Transactional
    public int deleteReceiveMail(int mailNo, int receiverMem) {
        int result = receiverRepository.updateByReceiverDelStatus(mailNo, receiverMem);
        System.out.println("수신 삭제 : " + result);

        return result;
    }
}
