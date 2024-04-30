package com.devsplan.ketchup.mail.service;

import com.devsplan.ketchup.mail.dto.MailDTO;
import com.devsplan.ketchup.mail.dto.ReceiverDTO;
import com.devsplan.ketchup.mail.entity.Mail;
import com.devsplan.ketchup.mail.entity.Receiver;
import com.devsplan.ketchup.mail.repository.MailRepository;
import com.devsplan.ketchup.mail.repository.ReceiverRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MailService {
    private final MailRepository mailRepository;
    private final ReceiverRepository receiverRepository;

    public MailService(MailRepository mailRepository, ReceiverRepository receiverRepository) {
        this.mailRepository = mailRepository;
        this.receiverRepository = receiverRepository;
    }

    // 메일 등록
    @Transactional
    public int insertMail(MailDTO mailInfo) {
        Mail mail = new Mail(
                mailInfo.getSenderMem(),
                mailInfo.getMailTitle(),
                mailInfo.getMailContent(),
                mailInfo.getSendCancelStatus(),
                mailInfo.getSendDelStatus()
        );

        mailRepository.save(mail);

        return mail.getMailNo();
    }

    // 수신자 등록
    @Transactional
    public void insertReceiver(List<ReceiverDTO> receivers) {

        for(ReceiverDTO list : receivers) {
            Receiver receiver = new Receiver();

            receiver.setMailNo(list.getMailNo());
            receiver.setReceiverMem(list.getReceiverMem());
            receiver.setReceiverDelStatus(list.getReceiverDelStatus());

            receiverRepository.save(receiver);
        }
    }

    public List<MailDTO> selectSendMailList(String senderMem) {
        List<Mail> mailList = mailRepository.findBySenderMem(senderMem);

        return mailList.stream()
                .map(mail -> new MailDTO(
                        mail.getMailNo()
                        , mail.getSenderMem()
                        , mail.getMailTitle()
                        , mail.getMailContent()
                        , mail.getSendMailTime()
                        , mail.getSendCancelStatus()
                        , mail.getSendDelStatus()
                        , mail.getReceivers().stream()
                        .map(receiver -> new ReceiverDTO(
                                receiver.getMailNo(),
                                receiver.getReceiverMem(),
                                receiver.getReadTime(),
                                receiver.getReceiverDelStatus()
                        )).toList()
                ))
                .toList();
    }

    public List<MailDTO> selectReceiveMailList(String receiverTest) {
        List<Receiver> receiverList = receiverRepository.findByReceiverMem(receiverTest);
        List<Mail> mailAllList = mailRepository.findAll();

        List<MailDTO> receiverMail = new ArrayList<>();
        for(Receiver list : receiverList) {
            for(Mail mailList : mailAllList) {
                if(list.getMailNo() == mailList.getMailNo()) {
                    if(list.getReceiverDelStatus() == 'N') {
                        receiverMail.add(new MailDTO(
                                mailList.getMailNo(),
                                mailList.getSenderMem(),
                                mailList.getMailTitle(),
                                mailList.getMailContent(),
                                mailList.getSendMailTime(),
                                mailList.getSendCancelStatus(),
                                mailList.getSendDelStatus()
                        ));
                    }
                }
            }
        }

        return receiverMail;
    }

    public MailDTO selectMailDetail(int mailNo) {
        Mail mailDetail = mailRepository.findByMailNo(mailNo);

        return new MailDTO(
                mailDetail.getMailNo()
                , mailDetail.getSenderMem()
                , mailDetail.getMailTitle()
                , mailDetail.getMailContent()
                , mailDetail.getSendMailTime()
                , mailDetail.getSendCancelStatus()
                , mailDetail.getSendDelStatus()
        );
    }

    @Transactional
    public String cancelSendMail(int mailNo) {
        List<Receiver> mailRead = receiverRepository.findReadTime(mailNo);

        String result = "";
        for(int i = 0; i < mailRead.size(); i++) {
            if(mailRead.get(i).getReadTime() != null) {
                // 수신자 중 한명이라도 메일을 읽었을 경우
                result = "발송 취소 실패";
            }else {
                // 수신자 모두 메일을 읽지 않았을 경우 - 발송 취소 여부 'Y' / 수신자 삭제 여부 'Y' 변경
                mailRepository.cancelSendMail(mailNo);
                receiverRepository.updateReceiverDelByMailNo(mailNo);
            }
            result = "발송 취소 성공";
        }

        return result;
    }

    @Transactional
    public int deleteSendMail(int mailNo) {
        int result = mailRepository.updateDelByMailNoSender(mailNo);
        System.out.println("발신 삭제 : " + result);

        return result;
    }

    @Transactional
    public int deleteReceiveMail(int mailNo) {
        int receiverMem = 240429001;
        int result = receiverRepository.updateDelByMailNoReceiver(mailNo, receiverMem);
        System.out.println("수신 삭제 : " + result);

        return result;
    }
}