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
import java.util.stream.Collectors;

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
        List<Mail> mailList = mailRepository.findBySenderMemAndSendDelStatus(senderMem, 'N');
        for(Mail list : mailList) {
            List<Receiver> mailReceiver = receiverRepository.findByMailNo(list.getMailNo());
        }
        System.out.println(mailList);

        return mailList.stream()
                .map(mail -> new MailDTO(
                        mail.getMailNo()
                        , mail.getSenderMem()
                        , mail.getMailTitle()
                        , mail.getMailContent()
                        , mail.getSendMailTime()
                        , mail.getSendCancelStatus()
                        , mail.getSendDelStatus()
//                        , mailReceiver.stream()
//                        .map(receiver -> new ReceiverDTO(
//                                receiver.getMailNo(),
//                                receiver.getReceiverMem(),
//                                receiver.getReadTime(),
//                                receiver.getReceiverDelStatus()
//                        )).toList()
                )).toList();
    }

    public List<MailDTO> selectReceiveMailList(String receiverMem) {
        List<Receiver> receiverList = receiverRepository.findByReceiverMemAndReceiverDelStatus(receiverMem, 'N');
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
        Mail mailDetail = mailRepository.findByMailNoAndSendDelStatus(mailNo, 'N');
        List<Receiver> mailReceiver = receiverRepository.findByMailNo(mailNo);
        System.out.println(mailDetail);

        return new MailDTO(
                mailDetail.getMailNo()
                , mailDetail.getSenderMem()
                , mailDetail.getMailTitle()
                , mailDetail.getMailContent()
                , mailDetail.getSendMailTime()
                , mailDetail.getSendCancelStatus()
                , mailDetail.getSendDelStatus()
                , mailReceiver.stream()
                    .map(receiver -> new ReceiverDTO(
                            receiver.getMailNo(),
                            receiver.getReceiverMem(),
                            receiver.getReadTime(),
                            receiver.getReceiverDelStatus()
                    )).toList()
        );
    }

    @Transactional
    public int cancelSendMail(int mailNo) {
        List<Receiver> mailRead = receiverRepository.findByMailNo(mailNo);

        int result = 0;
        for(Receiver list : mailRead) {
            if(list.getReadTime() != null) {
                result = 0; break;
            }

            result = 1;
        }

        if(result == 1) {
            Mail oneMail = mailRepository.findByMailNo(mailNo);
            oneMail.setSendCancelStatus('Y');
            mailRepository.save(oneMail);

            for(Receiver read : mailRead) {
                read.setReceiverDelStatus('Y');
            }
        }

        return result;
    }

    @Transactional
        public int deleteSendMail(List<Integer> mailNo) {

        System.out.println("삭제할 메일 번호!!!!!!!!!!!!!!" + mailNo);
        int result = 0;
        for(Integer list : mailNo) {
            Mail oneMail = mailRepository.findByMailNo(list);
            oneMail.setSendDelStatus('Y');
            mailRepository.save(oneMail);
            result++;
        }
        System.out.println("발신 삭제 : " + result);

        return result;
    }

    @Transactional
    public int deleteReceiveMail(List<Integer> mailNo) {
        String receiverMem = "4";

        int result = 0;
        for(Integer list : mailNo) {
            List<Receiver> Receivers = receiverRepository.findByMailNo(list);

            for(Receiver receiver : Receivers) {
                receiver.setReceiverDelStatus('Y');

                receiverRepository.save(receiver);

                result++;
            }
        }
        System.out.println("수신 삭제 : " + result);

        return result;
    }

    @Transactional
    public int replyMail(MailDTO replyMail) {
        Mail mail = new Mail(
                replyMail.getSenderMem(),
                replyMail.getMailTitle(),
                replyMail.getMailContent(),
                replyMail.getSendCancelStatus(),
                replyMail.getSendDelStatus()
        );

        mailRepository.save(mail);

        return mail.getMailNo();
    }
}