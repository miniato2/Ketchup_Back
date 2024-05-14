package com.devsplan.ketchup.mail.service;

import com.devsplan.ketchup.common.Criteria;
import com.devsplan.ketchup.common.ResponseDTO;
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
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
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

    @Value("${image.image-dir}")
    private String IMAGE_DIR;

    @Value("${image.image-url}")
    private String IMAGE_URL;

    // 메일 등록
    @Transactional
    public Object insertMail(MailDTO mailInfo, List<MultipartFile> mailFiles) {
        try {
            System.out.println("메일 내용 등록!!!!!!!!!!");
            // Mail 객체 생성 및 등록
            Mail mail = new Mail(
                    mailInfo.getSenderMem(),
                    mailInfo.getMailTitle(),
                    mailInfo.getMailContent(),
                    mailInfo.getSendCancelStatus(),
                    mailInfo.getSendDelStatus()
            );

            Mail saveMail = mailRepository.save(mail);
            System.out.println("메일 등록 성공?????????!!!!!!!!!!");
            int sendMailNo = saveMail.getMailNo();

            // 수신자 등록
            for(ReceiverDTO list : mailInfo.getReceivers()) {
                Receiver receiver = new Receiver(
                        sendMailNo,
                        list.getReceiverMem(),
                        list.getReceiverDelStatus()
                );

                receiverRepository.save(receiver);
                System.out.println("수신자 등록...!!!!!!!!!!!!!!!!!!1");
            }

            // 파일 업로드
            for (MultipartFile file : mailFiles) {
                String mailFileName = UUID.randomUUID().toString().replace("-", "");
                String replaceFileName = FileUtils.saveFile(IMAGE_DIR, mailFileName, file);

                MailFile mailFileEntity = new MailFile(
                        sendMailNo,
                        replaceFileName,
                        mailFileName,
                        file.getOriginalFilename()
                );

                mailFileRepository.save(mailFileEntity);
                System.out.println("파일 업로드,,,,,,,,,,,,,,,,,,,,우우우우우우우");
            }

            return new ResponseDTO(HttpStatus.OK, "메일 전송 성공", sendMailNo);
        } catch (IOException e) {
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드 중 오류가 발생했습니다.", null);
        }
    }




//    @Transactional
//    public int insertMail(MailDTO mailInfo) {
//        Mail mail = new Mail(
//                mailInfo.getSenderMem(),
//                mailInfo.getMailTitle(),
//                mailInfo.getMailContent(),
//                mailInfo.getSendCancelStatus(),
//                mailInfo.getSendDelStatus()
//        );
//
//        Mail saveMail = mailRepository.save(mail);
//
//        return saveMail.getMailNo();
//    }
//
//    // 수신자 등록
//    @Transactional
//    public Object insertReceiver(List<ReceiverDTO> receivers) {
//        Receiver receiveList = null;
//        for(ReceiverDTO list : receivers) {
//            Receiver receiver = new Receiver(
//                list.getMailNo(),
//                list.getReceiverMem(),
//                list.getReceiverDelStatus()
//            );
//
//            receiveList = receiverRepository.save(receiver);
//        }
//
//        return receiveList;
//    }
//
//    // 파일 업로드
//    @Transactional
//    public Object insertMailFile(int sendMailNo, MultipartFile mailFile) {
//        String mailFileName = UUID.randomUUID().toString().replace("-", "");
//        String replaceFileName = "";
//
//        try {
//            replaceFileName = FileUtils.saveFile(IMAGE_DIR, mailFileName, mailFile);
//
//            MailFile mailFiles = new MailFile(
//                    sendMailNo,
//                    replaceFileName,
//                    mailFileName,
//                    mailFile.getOriginalFilename()
//            );
//
//            return mailFileRepository.save(mailFiles);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    // 보낸 메일 목록 조회
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

    // 받은 메일 조회
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
                if(list.getMailNo() == mailList.getMailNo() && list.getReceiverDelStatus() == 'N') {
                    Timestamp readTime = list.getReadTime();

                    // ReceiveDTO 객체 생성 및 수신자가 메일을 읽은 시간 설정
                    List<ReceiverDTO> receiverReadTime = new ArrayList<>();

                    ReceiverDTO receiveDTO = new ReceiverDTO(readTime);
                    receiverReadTime.add(receiveDTO);

                    // MailDTO 객체를 생성하여 수신자가 메일을 읽은 시간을 포함시킴
                    MailDTO mailDTO = new MailDTO(
                            mailList.getMailNo(),
                            mailList.getSenderMem(),
                            mailList.getMailTitle(),
                            mailList.getMailContent(),
                            mailList.getSendMailTime(),
                            mailList.getSendCancelStatus(),
                            mailList.getSendDelStatus(),
                            receiverReadTime
                    );

                    // 생성한 MailDTO를 리스트에 추가
                    receiverMail.add(mailDTO);
                }
            }
        }

        System.out.println("🚚🚚🚚🚚🚚🚚🚚🚚🚚🚚🚚🚚🚚🚚🚚🚚🚚🚚🚚🚚🚚");
        System.out.println(receiverMail);

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
            oneMail.sendCancelStatus('Y');
            mailRepository.save(oneMail);

            for(Receiver read : mailRead) {
                read.receiverDelStatus('Y');
            }
        }

        return result;
    }

    @Transactional
    public int deleteSendMail(List<Integer> mailNo) {
        int result = 0;
        for(Integer list : mailNo) {
            Mail oneMail = mailRepository.findByMailNo(list);
            oneMail.sendDelStatus('Y');
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
                receiver.receiverDelStatus('Y');

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