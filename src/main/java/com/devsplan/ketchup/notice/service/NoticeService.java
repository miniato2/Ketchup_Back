package com.devsplan.ketchup.notice.service;

import com.devsplan.ketchup.board.dto.BoardDTO;
import com.devsplan.ketchup.board.dto.BoardFileDTO;
import com.devsplan.ketchup.board.entity.Board;
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
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
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

    /* 공지사항 목록조회 & 페이징 & 상단고정 */
    public Page<NoticeDTO> selectNoticeList(Pageable pageable, String title) {

        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1,
                pageable.getPageSize(), Sort.by("noticeNo").descending());

        Page<Notice> fixedNoticeList = noticeRepository.findByNoticeFix(pageable, 'Y');

        Page<Notice> unfixedNoticeList;

        if(title != null && !title.isEmpty()) {
            unfixedNoticeList = noticeRepository.findByNoticeTitleContaining(pageable, title);
            log.info("unfixedNoticeList : " + unfixedNoticeList);

        } else {
            unfixedNoticeList = noticeRepository.findAll(pageable);
            log.info("unfixedNoticeList : " + unfixedNoticeList);
        }

        // 상단에 고정된 공지사항과 그렇지 않은 공지사항을 합쳐서 반환
        List<NoticeDTO> mergedList = new ArrayList<>();
        mergedList.addAll(fixedNoticeList.stream().map(notice -> modelMapper.map(notice, NoticeDTO.class)).toList());
        mergedList.addAll(unfixedNoticeList.stream().map(notice -> modelMapper.map(notice, NoticeDTO.class)).toList());

        log.info("mergedList : " + mergedList);

        if (title == null || title.isEmpty() && mergedList.isEmpty()) {
            System.out.println("현재 공지가 없습니다.");
        }

        return new PageImpl<>(mergedList, pageable, mergedList.size());
    }

    /* 공지사항 상세조회(첨부파일 다운) */
    public NoticeDTO selectNoticeDetail(int noticeNo) {
        Notice foundNotice= noticeRepository.findById(noticeNo).orElse(null);
        if (foundNotice == null) {
            return null; // 해당 게시물이 존재하지 않으면 null 반환
        }

        NoticeDTO noticeDTO = modelMapper.map(foundNotice, NoticeDTO.class);

        // 게시물에 첨부된 파일 다운로드 링크 생성
        List<NoticeFile> noticeFiles = findNoticeFileByNoticeNo(noticeNo);
        List<NoticeFileDTO> noticeFileDTOS = new ArrayList<>();
        for (NoticeFile noticeFile : noticeFiles) {
            NoticeFileDTO noticeFileDTO = modelMapper.map(noticeFile, NoticeFileDTO.class);

            // 파일의 다운로드 경로를 클라이언트에 전달
            noticeFileDTO.setNoticeFilePath(noticeFile.getNoticeFilePath());
            noticeFileDTOS.add(noticeFileDTO);
        }
        noticeDTO.setNoticeFiles(noticeFileDTOS);

        log.info("noticeDTO : " + noticeDTO);

        return noticeDTO;
    }

    public List<NoticeFile> findNoticeFileByNoticeNo(int noticeNo) {
        return noticeFileRepository.findByNoticeNo(noticeNo);
    }

    /* 공지사항 등록 */
    @Transactional
    @PreAuthorize("hasAnyAuthority('LV3', 'LV2')")
    public void insertNotice(NoticeDTO noticeDTO) {
        try {
            noticeDTO.setNoticeCreateDttm(new Timestamp(System.currentTimeMillis()));
            Notice notice = modelMapper.map(noticeDTO, Notice.class);

            noticeRepository.save(notice);

            log.info("공지 등록 성공");
        } catch (Exception e) {
            log.error("공지 등록 실패: {}", e.getMessage(), e);
        }
    }

    /* 공지사항 등록(첨부파일) */
    @Transactional
    @PreAuthorize("hasAnyAuthority('LV3', 'LV2')")
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

                log.info("공지 등록 성공");
            } else {
                log.info("첨부파일이 없음");
            }
        } catch (Exception e) {
            log.error("공지 등록 실패: {}", e.getMessage(), e);
        }
    }

    /* 공지사항 수정 */
    @Transactional
    @PreAuthorize("hasAnyAuthority('LV3', 'LV2')")
    public String updateNotice(int noticeNo, NoticeDTO noticeDTO, String memberNo) {
        try {
            Notice foundNotice = noticeRepository.findById(noticeNo).orElseThrow(IllegalArgumentException::new);

            // 직급 2,3인 사용자
            if(foundNotice.getMemberNo().equals(memberNo)) {
                foundNotice.noticeTitle(noticeDTO.getNoticeTitle());
                foundNotice.noticeContent(noticeDTO.getNoticeContent());
                foundNotice.noticeFix(noticeDTO.getNoticeFix());
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
    @PreAuthorize("hasAnyAuthority('LV3', 'LV2')")
    public String updateNoticeWithFile(int noticeNo, NoticeDTO noticeDTO, List<MultipartFile> files, String memberNo) {
        try {
            Notice foundNotice = noticeRepository.findById(noticeNo).orElseThrow(IllegalArgumentException::new);

            if(foundNotice.getMemberNo().equals(memberNo)) {
                foundNotice.noticeTitle(noticeDTO.getNoticeTitle());
                foundNotice.noticeContent(noticeDTO.getNoticeContent());
                foundNotice.noticeFix(noticeDTO.getNoticeFix());
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

                    return "공지 수정 성공";
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
    @PreAuthorize("hasAnyAuthority('LV3', 'LV2')")
    public void deleteNotice(int noticeNo, String memberNo) {
        Notice notice = noticeRepository.findById(noticeNo).orElseThrow(IllegalArgumentException::new);

        if(!notice.getMemberNo().equals(memberNo)) {
            throw new IllegalArgumentException("삭제 권한이 없음");
        }

        List<NoticeFile> noticeFiles = noticeFileRepository.findByNoticeNo(noticeNo);
        for(NoticeFile noticeFile : noticeFiles) {
            String filePath = noticeFile.getNoticeFilePath();

            boolean isFileDeleted = FileUtils.deleteFile(IMAGE_DIR, FilenameUtils.getName(filePath));

            if(!isFileDeleted) {
                log.error(filePath, "파일 삭제에 실패했습니다.");
            }
            noticeFileRepository.delete(noticeFile);
        }

        noticeRepository.delete(notice);
    }

    /* 삭제 여부 test 확인용 */
    public void getNoticeById(int noticeNo) throws ChangeSetPersister.NotFoundException {
        noticeRepository.findById(noticeNo).orElseThrow(ChangeSetPersister.NotFoundException::new);
    }



}