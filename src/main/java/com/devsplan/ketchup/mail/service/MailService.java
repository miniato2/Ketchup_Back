package com.devsplan.ketchup.mail.service;

import com.devsplan.ketchup.common.Criteria;
import com.devsplan.ketchup.mail.dto.MailDTO;
import com.devsplan.ketchup.mail.dto.MailFileDTO;
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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

    @Transactional
    public int insertMail(MailDTO mailInfo, List<MultipartFile> mailFiles) throws IOException {
        // 메일 내용 등록(수신자 제외)
        Mail mail = new Mail(
                mailInfo.getSenderMem(),
                mailInfo.getMailTitle(),
                mailInfo.getMailContent(),
                mailInfo.getSendCancelStatus(),
                mailInfo.getSendDelStatus(),
                mailInfo.getReplyMailNo()
        );

        Mail saveMail = mailRepository.save(mail);
        // 등록 메일 번호
        int sendMailNo = saveMail.getMailNo();

        // 반환할 결과값
        int result = 0;

        // 수신자 등록
        for (ReceiverDTO list : mailInfo.getReceivers()) {
            Receiver receiver = new Receiver(
                    sendMailNo,
                    list.getReceiverMem(),
                    'N'
            );

            receiverRepository.save(receiver);
            result = sendMailNo;
        }

        if (mailFiles != null) {
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
            }

            result = sendMailNo;
        }

        return result;
    }

    // 보낸 메일 목록 조회
    public Page<MailDTO> selectSendMailList(Criteria cri, String senderMem, String search, String searchValue) {
        int page = cri.getPageNum() - 1;
        int size = cri.getAmount();
        Pageable paging = PageRequest.of(page, size, Sort.by("mailNo").descending());

        Page<Mail> mailList = null;
        if (search != null) {
            if (search.equals("mailTitle") && !searchValue.isEmpty()) {
                mailList = mailRepository.findBySenderMemAndSendDelStatusAndMailTitleContaining(senderMem, 'N', searchValue, paging);
            } else if (search.equals("senderMem") && !searchValue.isEmpty()) {
//                mailList = mailRepository.findBySenderMemAndSendDelStatusAndReceiverMemContaining(senderMem, 'N', searchValue);
            }
        } else {
            mailList = mailRepository.findBySenderMemAndSendDelStatus(senderMem, 'N', paging);
        }

        List<MailDTO> mailDtoList = mailList.stream().map(mail -> {
            List<Receiver> mailReceivers = receiverRepository.findByMailNo(mail.getMailNo());
            List<ReceiverDTO> mailReceiverList = mailReceivers.stream()
                    .map(receiverMap -> new ReceiverDTO(
                            receiverMap.getReceiverNo(),
                            receiverMap.getMailNo(),
                            receiverMap.getReceiverMem(),
                            receiverMap.getReadTime(),
                            receiverMap.getReceiverDelStatus()
                    )).collect(Collectors.toList());

            return new MailDTO(
                    mail.getMailNo(),
                    mail.getSenderMem(),
                    mail.getMailTitle(),
                    mail.getMailContent(),
                    mail.getSendMailTime(),
                    mail.getSendCancelStatus(),
                    mail.getSendDelStatus(),
                    mail.getReplyMailNo(),
                    mailReceiverList
            );
        }).collect(Collectors.toList());

        return new PageImpl<>(mailDtoList, paging, mailList.getTotalElements());
    }

    // 받은 메일 목록 조회
    public Page<MailDTO> selectReceiveMailList(Criteria cri, String receiverMem, String search, String searchValue) {
        int page = cri.getPageNum() - 1;
        int size = cri.getAmount();
        Pageable paging = PageRequest.of(page, size, Sort.by("mailNo").descending());

        System.out.println("🍥🍥🍥🍥🍥");
        System.out.println(paging.getSort());

        Page<Mail> mailAllList = null;
        if (search != null) {
            if (search.equals("mailTitle") && !searchValue.isEmpty()) {
                mailAllList = mailRepository.findByMailTitleContaining(searchValue, paging);
            } else if (search.equals("senderMem") && !searchValue.isEmpty()) {
//                mailAllList = mailRepository.findBySenderMemContaining(searchValue, paging).getContent();
            }
        } else {
            mailAllList = mailRepository.findAll(paging);
        }

        List<Receiver> receivers = receiverRepository.findByReceiverMemAndReceiverDelStatus(receiverMem, 'N');

        List<MailDTO> receiverMail = new ArrayList<>();
        for (Receiver list : receivers) {
            System.out.println("🌼🌼🌼🌼🌼");
            System.out.println(list);
            for (Mail mailList : mailAllList) {
                System.out.println("💐💐💐💐💐");
                System.out.println(mailList);
                if (list.getMailNo() == mailList.getMailNo() && list.getReceiverDelStatus() == 'N') {
                    Timestamp readTime = list.getReadTime();

                    List<ReceiverDTO> receiverReadTime = new ArrayList<>();

                    ReceiverDTO receiveDTO = new ReceiverDTO(readTime);
                    receiverReadTime.add(receiveDTO);

                    MailDTO mailDTO = new MailDTO(
                            mailList.getMailNo(),
                            mailList.getSenderMem(),
                            mailList.getMailTitle(),
                            mailList.getMailContent(),
                            mailList.getSendMailTime(),
                            mailList.getSendCancelStatus(),
                            mailList.getSendDelStatus(),
                            mailList.getReplyMailNo(),
                            receiverReadTime
                    );

                    System.out.println("🍀🍀🍀🍀🍀");
                    System.out.println(mailDTO);

                    receiverMail.add(mailDTO);
                }
            }
        }

        System.out.println("🍁🍁🍁🍁🍁");
        System.out.println(receiverMail);

        return new PageImpl<>(receiverMail, paging, receivers.size());
    }

    // 메일 상세 조회
    public MailDTO selectMailDetail(int mailNo) {
        Mail mailDetail = mailRepository.findByMailNoAndSendDelStatus(mailNo, 'N');
        List<Receiver> mailReceiver = receiverRepository.findByMailNo(mailNo);
        List<MailFile> mailFileList = mailFileRepository.findByMailNo(mailNo);

        return new MailDTO(
                mailDetail.getMailNo()
                , mailDetail.getSenderMem()
                , mailDetail.getMailTitle()
                , mailDetail.getMailContent()
                , mailDetail.getSendMailTime()
                , mailDetail.getSendCancelStatus()
                , mailDetail.getSendDelStatus()
                , mailDetail.getReplyMailNo()
                , mailReceiver.stream()
                    .map(receiver -> new ReceiverDTO(
                        receiver.getMailNo(),
                        receiver.getReceiverMem(),
                        receiver.getReadTime(),
                        receiver.getReceiverDelStatus()
                    )).toList()
                , mailFileList.stream()
                    .map(mailFile -> new MailFileDTO(
                        mailFile.getMailFileNo(),
                        mailFile.getMailFilePath(),
                        mailFile.getMailFileName(),
                        mailFile.getMailFileOriName()
                    )).toList()
        );
    }

    @Transactional
    public int cancelSendMail(int mailNo) {
        List<Receiver> mailRead = receiverRepository.findByMailNo(mailNo);

        int result = 0;
        for (Receiver list : mailRead) {
            if (list.getReadTime() != null) {
                result = 0;
                break;
            }

            result = 1;
        }

        if (result == 1) {
            Mail oneMail = mailRepository.findByMailNo(mailNo);
            oneMail.sendCancelStatus('Y');
            mailRepository.save(oneMail);

            for (Receiver read : mailRead) {
                read.receiverDelStatus('Y');
            }
        }

        return result;
    }

    @Transactional
    public int deleteSendMail(List<Integer> mailNo) {
        int result = 0;
        for (Integer list : mailNo) {
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
        for (Integer list : mailNo) {
            List<Receiver> Receivers = receiverRepository.findByMailNo(list);

            for (Receiver receiver : Receivers) {
                receiver.receiverDelStatus('Y');

                receiverRepository.save(receiver);

                result++;
            }
        }

        return result;
    }

    @Transactional
    public Object updateReadMailTime(String memberNo, int mailNo) {
        Receiver updateMail = receiverRepository.findByMailNoAndReceiverMem(mailNo, memberNo);

//        if(updateMail.getReadTime() == null) {
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        updateMail.readTime(now);

        receiverRepository.save(updateMail);
//        }
        System.out.println(updateMail);

        return updateMail;
    }
}