package com.devsplan.ketchup.mail.service;

import com.devsplan.ketchup.mail.dto.MailDTO;
import com.devsplan.ketchup.mail.entity.Mail;
import com.devsplan.ketchup.mail.entity.Receiver;
import com.devsplan.ketchup.mail.repository.MailRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MailService {
    private final MailRepository mailRepository;

    public MailService(MailRepository mailRepository) {
        this.mailRepository = mailRepository;
    }

    @Transactional
    public void insertMail(MailDTO mailInfo) {
        Mail mail = new Mail(
                mailInfo.getMailNo(),
                mailInfo.getSender(),
                mailInfo.getMailTitle(),
                mailInfo.getMailContent(),
                mailInfo.getSendCancelStatus(),
                mailInfo.getSendDelStatus()
        );

//        Receiver recevier = new Receiver(
//                mailInfo.getReceivers().get(0).getReceiverNo(),
//                mailInfo.getMailNo(),
//                mailInfo.getReceivers().get(0).getReceiverName(),
//                mailInfo.getReceivers().get(0).getReadTime(),
//                mailInfo.getReceivers().get(0).getReceiverDelStatus()
//        );
//
//        List<Receiver> receiverList = new ArrayList<>();
//        receiverList.add(recevier);
//        mail.setReceivers(receiverList);

        mailRepository.save(mail);
    }

    @Transactional
    public List<Mail> selectMailList(String senderNo) {
        List<Mail> mailList = mailRepository.findBySender(senderNo);

        return mailList;
    }
}
