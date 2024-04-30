package com.devsplan.ketchup.notice.service;

import com.devsplan.ketchup.board.entity.BoardFile;
import com.devsplan.ketchup.notice.dto.NoticeDTO;
import com.devsplan.ketchup.notice.dto.NoticeFileDTO;
import com.devsplan.ketchup.notice.entity.Notice;
import com.devsplan.ketchup.notice.entity.NoticeFile;
import com.devsplan.ketchup.notice.repository.NoticeFileRepository;
import com.devsplan.ketchup.notice.repository.NoticeRepository;
import com.devsplan.ketchup.util.FileUtils;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final NoticeFileRepository noticeFileRepository;
    private final ModelMapper modelMapper;

    /* 이미지 저장 할 위치 및 응답 할 이미지 주소 */
    @Value("${image.image-dir}")
    private String IMAGE_DIR;

    public NoticeService(NoticeRepository noticeRepository, NoticeFileRepository noticeFileRepository, ModelMapper modelMapper) {
        this.noticeRepository = noticeRepository;
        this.noticeFileRepository = noticeFileRepository;
        this.modelMapper = modelMapper;
    }

    /* 공지사항 등록 */
    @Transactional
    public void insertNotice(NoticeDTO noticeDTO) {
        try {
            noticeDTO.setNoticeCreateDttm(new Timestamp(System.currentTimeMillis()));
            Notice notice = modelMapper.map(noticeDTO, Notice.class);

            noticeRepository.save(notice);

            System.out.println("공지 등록 성공");
        } catch (Exception e) {
            System.out.println("공지 등록 실패");
        }
    }

    /* 공지사항 등록(첨부파일) */
    @Transactional
    public void insertNoticeWithFile(NoticeDTO noticeDTO, List<MultipartFile> files) {
        try {
            noticeDTO.setNoticeCreateDttm(new Timestamp(System.currentTimeMillis()));
            Notice savedNotice = modelMapper.map(noticeDTO, Notice.class);

            if (files != null && !files.isEmpty()) {
                for (MultipartFile file : files) {
                    String fileOriginName = UUID.randomUUID().toString().replace("-", "");
                    String fileName = file.getOriginalFilename();
                    String fileType = file.getContentType();
                    String savedFilePath = FileUtils.saveFile(IMAGE_DIR, fileName, file);
                    String filePath = savedFilePath + "\\" + fileName;

                    // 파일을 저장할 디렉토리 생성 (만약 디렉토리가 없다면)
                    File uploadDir = new File(IMAGE_DIR);
                    if (!uploadDir.exists()) {
                        uploadDir.mkdirs();
                    }

                    File newFile = new File(savedFilePath);
                    file.transferTo(newFile);

                    savedNotice.noticeFilePath(filePath);
                    noticeRepository.save(savedNotice);

                    NoticeFileDTO noticeFileDTO = new NoticeFileDTO();
                    noticeFileDTO.setNoticeNo(savedNotice.getNoticeNo());
                    noticeFileDTO.setNoticeFileName(fileName);
                    noticeFileDTO.setNoticeFilePath(filePath);
                    noticeFileDTO.setNoticeOriginName(fileOriginName);
                    noticeFileDTO.setNoticeFileSize(file.getSize());
                    noticeFileDTO.setNoticeFileType(fileType);

                    NoticeFile noticeFile = modelMapper.map(noticeFileDTO, NoticeFile.class);
                    noticeFileRepository.save(noticeFile);
                }

                System.out.println("공지 등록 성공");
            } else {
                System.out.println("첨부파일이 없음");
            }
        } catch (Exception e) {
            System.out.println("공지 등록 실패");
        }
    }

    /* 공지사항 수정 */
    @Transactional
    public String updateNotice(int noticeNo, NoticeDTO noticeDTO, String memberNo) {
        try {
            Notice foundNotice = noticeRepository.findById(noticeNo).orElseThrow(IllegalArgumentException::new);

            // 직급 2,3인 사용자
            if(foundNotice.getMemberNo().equals(memberNo) || noticeDTO.getPositionLevel() == 2 || noticeDTO.getPositionLevel() == 3) {
                foundNotice.noticeTitle(noticeDTO.getNoticeTitle());
                foundNotice.noticeContent(noticeDTO.getNoticeContent());
                foundNotice.noticeUpdateDttm(new Timestamp(System.currentTimeMillis()));

                Notice savedNotice = noticeRepository.save(foundNotice);
                if(savedNotice.getNoticeNo() == noticeNo) {
                    return "공지 수정 성공";
                } else {
                    return "공지 수정 실패 : 엔티티 잘못 저장되었습니다.";
                }

            } else {
                return "공지 수정 권한이 없습니다.";
            }
        } catch (Exception e) {
            log.error("공지 수정 중 오류 발생: " + e.getMessage(), e);
            return "공지 수정 중 오류가 발생했습니다.";
        }

    }

    /* 공지사항 수정(첨부파일) */
    @Transactional
    public String updateNoticeWithFile(int noticeNo, NoticeDTO noticeDTO, List<MultipartFile> files, String memberNo) {
        try {
            Notice foundNotice = noticeRepository.findById(noticeNo).orElseThrow(IllegalArgumentException::new);

            if(foundNotice.getMemberNo().equals(memberNo) || noticeDTO.getPositionLevel() == 2 || noticeDTO.getPositionLevel() == 3) {
                foundNotice.noticeTitle(noticeDTO.getNoticeTitle());
                foundNotice.noticeContent(noticeDTO.getNoticeContent());
                foundNotice.noticeUpdateDttm(new Timestamp(System.currentTimeMillis()));

                Notice savedNotice = noticeRepository.save(foundNotice);

                if (files != null && !files.isEmpty()) {
                    for (MultipartFile file : files) {
                        String fileOriginName = UUID.randomUUID().toString().replace("-", "");
                        String fileName = file.getOriginalFilename();
                        String fileType = file.getContentType();
                        String savedFilePath = FileUtils.saveFile(IMAGE_DIR, fileName, file);
                        String filePath = savedFilePath + "\\" + fileName;

                        // 파일을 저장할 디렉토리 생성 (만약 디렉토리가 없다면)
                        File uploadDir = new File(IMAGE_DIR);
                        if (!uploadDir.exists()) {
                            uploadDir.mkdirs();
                        }

                        File newFile = new File(savedFilePath);
                        file.transferTo(newFile);

                        savedNotice.noticeFilePath(filePath);
                        noticeRepository.save(savedNotice);

                        NoticeFileDTO noticeFileDTO = new NoticeFileDTO();
                        noticeFileDTO.setNoticeNo(savedNotice.getNoticeNo());
                        noticeFileDTO.setNoticeFileName(fileName);
                        noticeFileDTO.setNoticeFilePath(filePath);
                        noticeFileDTO.setNoticeOriginName(fileOriginName);
                        noticeFileDTO.setNoticeFileSize(file.getSize());
                        noticeFileDTO.setNoticeFileType(fileType);

                        NoticeFile noticeFile = modelMapper.map(noticeFileDTO, NoticeFile.class);
                        noticeFileRepository.save(noticeFile);
                    }

                    return "공지 등록 성공";
                } else {
                    return "첨부파일이 없음";
                }
            } else {
                return "공지 수정 권한이 없습니다.";
            }

        } catch (Exception e) {
            log.error("공지 수정 중 오류 발생: " + e.getMessage(), e);
            return "공지 수정 중 오류가 발생했습니다.";
        }
    }

    /* 공지사항 삭제 */
    @Transactional
    public void deleteNotice(int noticeNo, String memberNo) {
        Notice notice = noticeRepository.findById(noticeNo).orElseThrow(IllegalArgumentException::new);

        // 직급정보에 따라 공지사항 삭제가능하게 추가 (positionLevel 2, 3)
        if(!notice.getMemberNo().equals(memberNo)) {
            System.out.println("삭제 권한이 없음");
        }

        List<NoticeFile> noticeFiles = noticeFileRepository.findByNoticeNo(noticeNo);
        for(NoticeFile noticeFile : noticeFiles) {
            String filePath = noticeFile.getNoticeFilePath();

            boolean isFileDeleted = FileUtils.deleteFile(IMAGE_DIR, FilenameUtils.getName(filePath));

            if(!isFileDeleted) {
                log.error("파일 삭제에 실패했습니다.", filePath);
            }
            noticeFileRepository.delete(noticeFile);
        }

        noticeRepository.delete(notice);
    }

    /* 삭제 여부 test 확인용 */
    public void getNoticeById(int noticeNo) {
        try {
            noticeRepository.findById(noticeNo)
                    .orElseThrow(ChangeSetPersister.NotFoundException::new);

        } catch (ChangeSetPersister.NotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}