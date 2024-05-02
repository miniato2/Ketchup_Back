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
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final NoticeFileRepository noticeFileRepository;
    private final ModelMapper modelMapper;

    /* 이미지 저장 할 위치 및 응답 할 이미지 주소 */
    @Value("${image.image-dir}")
    private String IMAGE_DIR;

    @Value("${image.image-url}")
    private String IMAGE_URL;

    public NoticeService(NoticeRepository noticeRepository, NoticeFileRepository noticeFileRepository, ModelMapper modelMapper) {
        this.noticeRepository = noticeRepository;
        this.noticeFileRepository = noticeFileRepository;
        this.modelMapper = modelMapper;
    }

    /* 공지사항 목록조회 & 페이징 & 상단고정 */
    public Page<NoticeDTO> selectNoticeList(Criteria cri, String title) {

        int index = cri.getPageNum() -1;
        int count = cri.getAmount();

        Pageable paging = PageRequest.of(index, count, Sort.by("noticeNo").descending());

        System.out.println("title : " + title);
        // 상단에 고정된 공지사항 조회
        Page<Notice> fixedNoticeList = noticeRepository.findByNoticeFix(paging, 'Y');

        List<NoticeDTO> mergedList = new ArrayList<>();

        // 상단에 고정된 공지사항이 있으면 추가
        if (!fixedNoticeList.isEmpty()) {
            mergedList.addAll(fixedNoticeList.stream().map(notice -> modelMapper.map(notice, NoticeDTO.class)).toList());
        }

        // 검색어가 있으면 해당 공지사항 조회
        if (title != null && !title.isEmpty()) {
            String formattedTitle = "%" + title + "%"; // 검색어를 포함하는 제목을 찾기 위해 like 연산자 사용
            Page<Notice> searchedNoticeList = noticeRepository.findByNoticeTitleContaining(formattedTitle, paging);
            mergedList.addAll(searchedNoticeList.stream().map(notice -> modelMapper.map(notice, NoticeDTO.class)).toList());
            log.info("searchedNoticeList : " + searchedNoticeList);
        } else {
            // 검색어가 없으면 전체 공지사항 조회
            Page<Notice> allNoticeList = noticeRepository.findAll(paging);
            mergedList.addAll(allNoticeList.stream().map(notice -> modelMapper.map(notice, NoticeDTO.class)).toList());
            log.info("allNoticeList : " + allNoticeList);
        }

        log.info("mergedList : " + mergedList);

        // 합쳐진 리스트를 페이지로 변환하여 반환
        return new PageImpl<>(mergedList, paging, mergedList.size());
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
        List<String> downloadLinks = noticeFiles.stream()
                .map(noticeFile -> IMAGE_URL + noticeFile.getNoticeFileImgUrl())
                .collect(Collectors.toList());

        String noticeImgUrls = String.join(",", downloadLinks);
        noticeDTO.setNoticeImgUrl(noticeImgUrls);

        log.info("noticeDTO : " + noticeDTO);

        return noticeDTO;
    }

    public List<NoticeFile> findNoticeFileByNoticeNo(int noticeNo) {
        return noticeFileRepository.findByNoticeNo(noticeNo);
    }

    /* 공지사항 등록 */
    @Transactional
    @PreAuthorize("hasAnyAuthority('LV3', 'LV2')")
    public void insertNotice(NoticeDTO noticeDTO, String memberNo) {
        try {
            noticeDTO.setNoticeCreateDttm(new Timestamp(System.currentTimeMillis()));
            noticeDTO.setMemberNo(memberNo);

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
    public void insertNoticeWithFile(NoticeDTO noticeDTO, List<MultipartFile> files, String memberNo) {
        try {
            noticeDTO.setNoticeCreateDttm(new Timestamp(System.currentTimeMillis()));
            noticeDTO.setMemberNo(memberNo);
            Notice savedNotice = modelMapper.map(noticeDTO, Notice.class);

            noticeRepository.save(savedNotice);

            List<NoticeFileDTO> noticeFileDTOList = new ArrayList<>();

            if (files != null && !files.isEmpty()) {
                int fileCount = 0; // 등록된 파일 수를 세는 변수 추가
                for (MultipartFile file : files) {
                    if (fileCount >= 5) { // 등록된 파일이 5개 이상이면 더 이상 파일을 등록하지 않음
                        break;
                    }
                    String fileName = UUID.randomUUID().toString().replace("-", "");
                    String replaceFileName = FileUtils.saveFile(IMAGE_URL, fileName, file);

                    NoticeFileDTO noticeFileDTO = new NoticeFileDTO(savedNotice.getNoticeNo(), replaceFileName);
                    noticeFileDTOList.add(noticeFileDTO);
                    fileCount++;
                }

                // 파일 정보를 NoticeFile 엔티티로 변환하여 저장
                List<NoticeFile> noticeFileList = noticeFileDTOList.stream()
                        .map(noticeFileDTO -> modelMapper.map(noticeFileDTO, NoticeFile.class))
                        .toList();

                savedNotice.noticeImgUrl(noticeFileDTOList.get(0).getNoticeFileImgUrl());

                noticeRepository.save(savedNotice);
                noticeFileRepository.saveAll(noticeFileList);
                log.info("공지 등록 성공");

            } else {
                log.info("첨부 파일이 없습니다.");
            }
        } catch (IOException e) {
            log.error("첨부파일 등록 실패: " + e.getMessage());
            throw new RuntimeException(e);
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

            if (foundNotice.getMemberNo().equals(memberNo)) {
                foundNotice.noticeTitle(noticeDTO.getNoticeTitle());
                foundNotice.noticeContent(noticeDTO.getNoticeContent());
                foundNotice.noticeFix(noticeDTO.getNoticeFix());
                foundNotice.noticeUpdateDttm(new Timestamp(System.currentTimeMillis()));

                Notice savedNotice = noticeRepository.save(foundNotice);
                List<NoticeFileDTO> noticeFileDTOList = new ArrayList<>();

                if (files != null && !files.isEmpty()) {
                    int fileCount = 0; // 등록된 파일 수를 세는 변수 추가
                    for (MultipartFile file : files) {
                        if (fileCount >= 5) { // 등록된 파일이 5개 이상이면 더 이상 파일을 등록하지 않음
                            break;
                        }
                        String fileName = UUID.randomUUID().toString().replace("-", "");
                        String replaceFileName = FileUtils.saveFile(IMAGE_URL, fileName, file);

                        NoticeFileDTO noticeFileDTO = new NoticeFileDTO(savedNotice.getNoticeNo(), replaceFileName);
                        noticeFileDTOList.add(noticeFileDTO);
                        fileCount++;
                    }

                    // 파일 정보를 NoticeFile 엔티티로 변환하여 저장
                    List<NoticeFile> noticeFileList = noticeFileDTOList.stream()
                            .map(noticeFileDTO -> modelMapper.map(noticeFileDTO, NoticeFile.class))
                            .toList();

                    savedNotice.noticeImgUrl(noticeFileDTOList.get(0).getNoticeFileImgUrl());

                    noticeRepository.save(savedNotice);
                    noticeFileRepository.saveAll(noticeFileList);

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
    public boolean deleteNotice(int noticeNo, String memberNo) throws NoSuchFileException {
        Notice notice = noticeRepository.findById(noticeNo).orElseThrow(IllegalArgumentException::new);

        if(!notice.getMemberNo().equals(memberNo)) {
            throw new IllegalArgumentException("삭제 권한이 없음");
        }

        List<NoticeFile> noticeFiles = noticeFileRepository.findByNoticeNo(noticeNo);
        for(NoticeFile noticeFile : noticeFiles) {
            String imgUrl = noticeFile.getNoticeFileImgUrl();

            // 파일이 이미 삭제된 경우에 대한 예외처리 추가
            boolean isFileDeleted = FileUtils.deleteFile(IMAGE_DIR, FilenameUtils.getName(imgUrl));
            if (!isFileDeleted) {
                log.error(imgUrl, "파일 삭제에 실패했습니다.");
            }
            noticeFileRepository.delete(noticeFile);
        }
        noticeRepository.delete(notice);
        return true;
    }

    /* 수정, 삭제 여부 test 확인용 */
    public void getNoticeById(int noticeNo) throws ChangeSetPersister.NotFoundException {
        noticeRepository.findById(noticeNo).orElseThrow(ChangeSetPersister.NotFoundException::new);
    }

}