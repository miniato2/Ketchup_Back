package com.devsplan.ketchup.notice.service;

import com.devsplan.ketchup.common.Criteria;
import com.devsplan.ketchup.notice.dto.NoticeDTO;
import com.devsplan.ketchup.notice.dto.NoticeFileDTO;
import com.devsplan.ketchup.notice.entity.Notice;
import com.devsplan.ketchup.notice.entity.NoticeFile;
import com.devsplan.ketchup.notice.repository.NoticeFileRepository;
import com.devsplan.ketchup.notice.repository.NoticeRepository;
import com.devsplan.ketchup.util.FileUtils;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final NoticeFileRepository noticeFileRepository;
    private final ModelMapper modelMapper;

    private final FileUtils fileUtils;

    /* 이미지 저장 할 위치 및 응답 할 이미지 주소 */
    @Value("${image.image-dir}")
    private String IMAGE_DIR;

    @Value("${image.image-url}")
    private String IMAGE_URL;

    public NoticeService(FileUtils fileUtils, NoticeRepository noticeRepository, NoticeFileRepository noticeFileRepository, ModelMapper modelMapper) {
        this.fileUtils = fileUtils;
        this.noticeRepository = noticeRepository;
        this.noticeFileRepository = noticeFileRepository;
        this.modelMapper = modelMapper;
    }

    /* 공지사항 목록조회 & 페이징 & 상단고정 */
    public Page<NoticeDTO> selectNoticeList(Criteria cri, String title) {

        int page = cri.getPageNum() - 1;
        int size = cri.getAmount();
        Pageable paging = PageRequest.of(page, size, Sort.by("noticeNo").descending());

        // 검색어가 있을 경우에는 해당 검색어를 포함하는 공지 목록을 가져옵니다.
        Page<Notice> noticePage;
        if (title != null && !title.isEmpty()) {
            noticePage = noticeRepository.findByNoticeTitleContainingIgnoreCase(title, paging);
        } else {
            // 검색어가 없을 경우에는 모든 공지 목록을 가져옵니다.
            noticePage = noticeRepository.findAll(paging);
        }

        // 페이징 정보 없이 모든 공지를 가져오기
        List<Notice> allNotices = noticeRepository.findAll(Sort.by("noticeCreateDttm").descending());
        System.out.println("===================== 공지 목록 조회 =====================");
        System.out.println("allNotices : " + allNotices.size());

        // 상단에 고정된 공지와 일반 공지로 나누기
        List<Notice> fixedNotices = allNotices.stream()
                .filter(notice -> notice.getNoticeFix() == 'Y')
                .collect(Collectors.toList());
        System.out.println("fixedNotices : " + fixedNotices.size());

        List<Notice> normalNotices = allNotices.stream()
                .filter(notice -> notice.getNoticeFix() != 'Y')
                .collect(Collectors.toList());
        System.out.println("normalNotices : " + normalNotices.size());

        // 검색어에 따라 필터링
        if (title != null && !title.isEmpty()) {
            normalNotices = normalNotices.stream()
                    .filter(notice -> notice.getNoticeTitle().toLowerCase().contains(title.toLowerCase()))
                    .collect(Collectors.toList());
        }
        System.out.println("title normalNotices : " + normalNotices.size());

        // 상단에 고정된 공지를 목록 가장 상단에 추가
        normalNotices.addAll(0, fixedNotices);

        // 전체 공지 수
        int totalElements = normalNotices.size();
        System.out.println("normalNotices.size : " + totalElements);

        // 페이지 객체 생성하여 반환
        return new PageImpl<>(normalNotices.stream()
                .map(notice -> modelMapper.map(notice, NoticeDTO.class))
                .collect(Collectors.toList()), paging, totalElements);
    }

    /* 공지사항 상세조회(첨부파일 다운) */
    public NoticeDTO selectNoticeDetail(int noticeNo) {

        Notice foundNotice = noticeRepository.findById(noticeNo).orElse(null);
        if (foundNotice == null) {
            return null; // 해당 게시물이 존재하지 않으면 null 반환
        }

        NoticeDTO noticeDTO = modelMapper.map(foundNotice, NoticeDTO.class);

        // 게시물에 첨부된 파일 다운로드 링크 생성
        List<NoticeFile> noticeFiles = findNoticeFileByNoticeNo(noticeNo);
        List<NoticeFileDTO> noticeFileDTOs = new ArrayList<>();

        for (NoticeFile noticeFile : noticeFiles) {
            NoticeFileDTO noticeFileDTO = modelMapper.map(noticeFile, NoticeFileDTO.class);
            String downloadLink = IMAGE_URL + noticeFile.getNoticeFileName();
            noticeFileDTO.setDownloadLink(downloadLink);
            noticeFileDTOs.add(noticeFileDTO);
        }

        noticeDTO.setNoticeFileList(noticeFileDTOs);
        log.info("noticeDTO : " + noticeDTO);

        return noticeDTO;
    }

    public List<NoticeFile> findNoticeFileByNoticeNo(int noticeNo) {
        return noticeFileRepository.findByNoticeNo(noticeNo);
    }

    /* 공지사항 등록 */
    @Transactional
    public Object insertNotice(NoticeDTO noticeDTO, String memberNo) {

        try {
            noticeDTO.setNoticeCreateDttm(new Timestamp(System.currentTimeMillis()));
            noticeDTO.setMemberNo(memberNo);

            Notice notice = modelMapper.map(noticeDTO, Notice.class);

            Notice savedNotice = noticeRepository.save(notice);

            log.info("공지 등록 성공");
            System.out.println("savedNotice : " + savedNotice.getNoticeNo());
            return savedNotice.getNoticeNo(); // 등록된 공지의 번호 반환
        } catch (Exception e) {
            log.error("공지 등록 실패: {}", e.getMessage(), e);
            return null; // 실패 시 null 반환
        }
    }

    /* 공지사항 등록(첨부파일) */
    @Transactional
    public Object insertNoticeWithFile(NoticeDTO noticeDTO, List<MultipartFile> files, String memberNo) throws IOException {

        log.info("NoticeDTO : " + noticeDTO);
        Map<String, Object> result = new HashMap<>();

        try {
            noticeDTO.setNoticeCreateDttm(new Timestamp(System.currentTimeMillis()));
            noticeDTO.setMemberNo(memberNo);

            Notice savedNotice = modelMapper.map(noticeDTO, Notice.class);
            noticeRepository.save(savedNotice);

            if (files != null) {
                int fileCount = 0;
                for (MultipartFile file : files) {
                    if (fileCount >= 5) { // 등록된 파일이 5개 이상이면 더 이상 파일을 등록하지 않음
                        break;
                    }
                    String fileName = file.getOriginalFilename();
                    String newFileName = UUID.randomUUID().toString().replace("-", "") + "." + fileName ;
                    String savedFilePath = fileUtils.saveFile(IMAGE_DIR, newFileName, file);
                    String filePath = savedFilePath + "//" + fileName;

                    // 파일을 저장할 디렉토리 생성 (만약 디렉토리가 없다면)
                    File newFile = new File(savedFilePath);
                    file.transferTo(newFile);

                    NoticeFileDTO noticeFileDTO = new NoticeFileDTO();
                    noticeFileDTO.setNoticeNo(savedNotice.getNoticeNo());
                    noticeFileDTO.setNoticeFileName(newFileName);
                    noticeFileDTO.setNoticeFilePath(filePath);
                    noticeFileDTO.setNoticeFileOriName(fileName);

                    NoticeFile noticeFile = modelMapper.map(noticeFileDTO, NoticeFile.class);
                    noticeFileRepository.save(noticeFile);
                    fileCount++;
                }
            }
            log.info("공지 등록 성공");
            System.out.println("savedNotice : " + savedNotice.getNoticeNo());
            return savedNotice.getNoticeNo(); // 등록된 공지의 번호 반환
        } catch (Exception e) {
            log.error("Error while inserting Announce with Files: " + e.getMessage());
            result.put("result", false);
        }
        return result;
    }


    /* 공지사항 수정 */
    @Transactional
    public String updateNotice(int noticeNo, NoticeDTO noticeDTO, String memberNo) {
        try {
            Notice foundNotice = noticeRepository.findById(noticeNo).orElseThrow(IllegalArgumentException::new);

            // 직급 2,3인 사용자
            if (foundNotice.getMemberNo().equals(memberNo)) {
                foundNotice.noticeTitle(noticeDTO.getNoticeTitle());
                foundNotice.noticeContent(noticeDTO.getNoticeContent());
                foundNotice.noticeFix(noticeDTO.getNoticeFix());
                foundNotice.noticeUpdateDttm(new Timestamp(System.currentTimeMillis()));

                Notice savedNotice = noticeRepository.save(foundNotice);

                if (savedNotice.getNoticeNo() == noticeNo) {
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

            if (foundNotice.getMemberNo().equals(memberNo)) {
                foundNotice.noticeTitle(noticeDTO.getNoticeTitle());
                foundNotice.noticeContent(noticeDTO.getNoticeContent());
                foundNotice.noticeFix(noticeDTO.getNoticeFix());
                foundNotice.noticeUpdateDttm(new Timestamp(System.currentTimeMillis()));

                Notice savedNotice = noticeRepository.save(foundNotice);

                if (files != null && !files.isEmpty()) {
                    int fileCount = 0; // 등록된 파일 수를 세는 변수 추가
                    for (MultipartFile file : files) {
                        if (fileCount >= 5) { // 등록된 파일이 5개 이상이면 더 이상 파일을 등록하지 않음
                            break;
                        }
                        String fileName = file.getOriginalFilename();
                        String newFileName = UUID.randomUUID().toString().replace("-", "") + "." + fileName ;
                        String savedFilePath = fileUtils.saveFile(IMAGE_DIR, newFileName, file);
                        String filePath = savedFilePath + "//" + fileName;

                        // 파일을 저장할 디렉토리 생성 (만약 디렉토리가 없다면)
                        File newFile = new File(savedFilePath);
                        file.transferTo(newFile);

                        NoticeFileDTO noticeFileDTO = new NoticeFileDTO();
                        noticeFileDTO.setNoticeNo(savedNotice.getNoticeNo());
                        noticeFileDTO.setNoticeFileName(newFileName);
                        noticeFileDTO.setNoticeFilePath(filePath);
                        noticeFileDTO.setNoticeFileOriName(fileName);

                        NoticeFile noticeFile = modelMapper.map(noticeFileDTO, NoticeFile.class);
                        noticeFileRepository.save(noticeFile);
                        fileCount++;
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
    public Object deleteNotice(int noticeNo, String memberNo) throws NoSuchFileException {
        try {

            // 공지사항 조회
            Notice notice = noticeRepository.findById(noticeNo).orElseThrow();

            System.out.println("service noticeNo : " + noticeNo);
            System.out.println("service memberNo : " + memberNo);

            // 현재 사용자의 Authentication 객체를 가져옴
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // 현재 사용자의 권한을 확인하여 LV2 또는 LV3 권한이 있는지 확인
            boolean hasPermission = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("LV2") || a.getAuthority().equals("LV3"));

            // 작성자인 경우에도 삭제 권한 부여
            hasPermission = hasPermission || notice.getMemberNo().equals(memberNo);

            if (hasPermission) {
                // 공지사항과 연관된 파일 조회
                List<NoticeFile> noticeFiles = noticeFileRepository.findByNoticeNo(noticeNo);
                for (NoticeFile noticeFile : noticeFiles) {
                    // 파일 삭제
                    noticeFileRepository.delete(noticeFile);
                }

                // 공지사항 삭제
                noticeRepository.delete(notice);

                return true; // 성공적으로 삭제됨

            } else {
                throw new IllegalArgumentException("권한이 없습니다.");
            }
        } catch (Exception e) {
            log.error("공지 삭제 중 오류 발생: " + e.getMessage(), e);
            return false; // 삭제 중 오류 발생
        }
    }

    /* 수정, 삭제 여부 test 확인용 */
    public void getNoticeById(int noticeNo) throws ChangeSetPersister.NotFoundException {
        noticeRepository.findById(noticeNo).orElseThrow(ChangeSetPersister.NotFoundException::new);
    }

}