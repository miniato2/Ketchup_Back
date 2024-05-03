package com.devsplan.ketchup.mail.service;

import com.devsplan.ketchup.mail.dto.MailDTO;
import com.devsplan.ketchup.mail.dto.ReceiverDTO;
import com.devsplan.ketchup.mail.entity.Mail;
import com.devsplan.ketchup.mail.entity.MailFile;
import com.devsplan.ketchup.mail.entity.Receiver;
import com.devsplan.ketchup.mail.repository.MailFileRepository;
import com.devsplan.ketchup.mail.repository.MailRepository;
import com.devsplan.ketchup.mail.repository.ReceiverRepository;
import com.devsplan.ketchup.util.FileUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MailService {
    private final MailRepository mailRepository;
    private final ReceiverRepository receiverRepository;
    private final MailFileRepository mailFileRepository;

    public MailService(MailRepository mailRepository, ReceiverRepository receiverRepository, MailFileRepository mailFileRepository) {
        this.mailRepository = mailRepository;
        this.receiverRepository = receiverRepository;
        this.mailFileRepository = mailFileRepository;
    }

    @Value("${image.image-dir}/mails")
    private String IMAGE_DIR;

    @Value("${image.image-url}")
    private String IMAGE_URL;

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

    // 파일 업로드
    @Transactional
    public void insertMailFile(int sendMailNo, MultipartFile mailFile) {
        String mailFileName = UUID.randomUUID().toString().replace("-", "");
        String replaceFileName = "";

        try {
            replaceFileName = FileUtils.saveFile(IMAGE_DIR, mailFileName, mailFile);

            MailFile mailFiles = new MailFile();

            mailFiles.setMailNo(sendMailNo);
            mailFiles.setMailFilePath(replaceFileName);
            mailFiles.setMailFileName(mailFileName);
            mailFiles.setMailFileOriName(mailFile.getOriginalFilename());

            mailFileRepository.save(mailFiles);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public List<MailDTO> selectSendMailList(String senderMem, String search, String searchValue) {

        List<Mail> mailList = new ArrayList<>();
        if(search != null) {
            if(search.equals("mailtitle") && !searchValue.isEmpty()) {
                mailList = mailRepository.findBySenderMemAndSendDelStatusAndMailTitleContaining(senderMem, 'N', searchValue);
            }else if(search.equals("sendermem") && !searchValue.isEmpty()) {
//                mailList = mailRepository.findBySenderMemAndSendDelStatusAndReceiverMemContaining(senderMem, 'N', searchValue);
            }
        }else {
            mailList = mailRepository.findBySenderMemAndSendDelStatus(senderMem, 'N');
        }

        // --------------------------------------------------

//        List<Mail> mailList = mailRepository.findBySenderMemAndSendDelStatus(senderMem, 'N');

        List<ReceiverDTO> mailReceiverList;
        List<MailDTO> mailDtoList = new ArrayList<>();

        for(Mail list : mailList) {
            List<Receiver> mailReceivers = receiverRepository.findByMailNo(list.getMailNo());
            mailReceiverList =
                    mailReceivers.stream()
                    .map(receiverMap -> new ReceiverDTO(
                            receiverMap.getReceiverNo(),
                            receiverMap.getMailNo(),
                            receiverMap.getReceiverMem(),
                            receiverMap.getReadTime(),
                            receiverMap.getReceiverDelStatus()
                    )).toList();

            mailDtoList.add(new MailDTO(
                    list.getMailNo(),
                    list.getSenderMem(),
                    list.getMailTitle(),
                    list.getMailContent(),
                    list.getSendMailTime(),
                    list.getSendCancelStatus(),
                    list.getSendDelStatus(),
                    mailReceiverList
            ));
        }

        return mailDtoList;
    }

    // 받은 메일 조회 + 검색(페이징 X)
    public List<MailDTO> selectReceiveMailList(String receiverMem, String search, String searchValue) {
        List<Receiver> receivers = receiverRepository.findByReceiverMemAndReceiverDelStatus(receiverMem, 'N');

        List<Mail> mailAllList = new ArrayList<>();

        if(search != null) {
            if(search.equals("mailtitle") && !searchValue.isEmpty()) {
                mailAllList = mailRepository.findByMailTitleContaining(searchValue);
            }else if(search.equals("sendermem") && !searchValue.isEmpty()) {
                mailAllList = mailRepository.findBySenderMemContaining(searchValue);
            }
        }else {
            mailAllList = mailRepository.findAll();
        }

        List<MailDTO> receiverMail = new ArrayList<>();
        for(Receiver list : receivers) {
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
        int result = 0;
        for(Integer list : mailNo) {
            Mail oneMail = mailRepository.findByMailNo(list);
            oneMail.setSendDelStatus('Y');
            mailRepository.save(oneMail);
            result++;
        }

        return result;
    }

    @Transactional
    public int deleteReceiveMail(List<Integer> mailNo) {
        int result = 0;
        for(Integer list : mailNo) {
            List<Receiver> Receivers = receiverRepository.findByMailNo(list);

            for(Receiver receiver : Receivers) {
                receiver.setReceiverDelStatus('Y');

                receiverRepository.save(receiver);

                result++;
            }
        }

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