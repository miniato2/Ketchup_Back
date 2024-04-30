package com.devsplan.ketchup.notice.service;

import com.devsplan.ketchup.notice.dto.NoticeDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class NoticeServiceTests {

    @Autowired
    private NoticeService noticeService;

    @DisplayName("공지 등록")
    @Test
    void insertNotice() {
        // given
        NoticeDTO noticeDTO = new NoticeDTO();
        noticeDTO.setMemberNo("1");
        noticeDTO.setPositionLevel(3);
        noticeDTO.setNoticeTitle("공지 제목");
        noticeDTO.setNoticeContent("공지 내용");
        noticeDTO.setNoticeFix('N');

        // when
        // then
        Assertions.assertDoesNotThrow(
                () -> noticeService.insertNotice(noticeDTO)
        );
    }

    @DisplayName("공지 등록(첨부파일)")
    @Test
    void insertNoticeWithFile() throws IOException {
        // given
        InputStream inputStream = getClass().getResourceAsStream("/test.png");
        MultipartFile multipartFile = new MockMultipartFile("test1", "test-img.png", "image/png", inputStream);

        NoticeDTO noticeDTO = new NoticeDTO();
        noticeDTO.setMemberNo("1");
        noticeDTO.setPositionLevel(3);
        noticeDTO.setNoticeTitle("공지 제목");
        noticeDTO.setNoticeContent("공지 내용");
        noticeDTO.setNoticeFix('N');

        // when
        List<MultipartFile> files = new ArrayList<>();
        files.add(multipartFile);

        // then
        Assertions.assertDoesNotThrow(
                () -> noticeService.insertNoticeWithFile(noticeDTO, files)
        );
    }

    @DisplayName("공지 수정")
    @Test
    void updateNotice() {
        // given
        int noticeNo = 1;
        String memberNo = "1";

        NoticeDTO noticeDTO = new NoticeDTO();
        noticeDTO.setNoticeTitle("공지 제목 수정");
        noticeDTO.setNoticeContent("공지 내용 수정");
        noticeDTO.setNoticeFix('Z');

        // when
        String result = noticeService.updateNotice(noticeNo, noticeDTO, memberNo);

        // then
        Assertions.assertEquals(result, "공지 수정 성공");
    }

    @DisplayName("공지 수정(첨부파일)")
    @Test
    void updateNoticeWithFile() throws IOException {
        // given
        int noticeNo = 1;
        String memberNo = "1";

        InputStream inputStream1 = getClass().getResourceAsStream("/test-img1.png");
        InputStream inputStream2 = getClass().getResourceAsStream("/test-img2.png");

        MultipartFile multipartFile1 = new MockMultipartFile("test1", "test-img1.png", "image/png", inputStream1);
        MultipartFile multipartFile2 = new MockMultipartFile("test2", "test-img2.png", "image/png", inputStream2);

        List<MultipartFile> files = Arrays.asList(multipartFile1, multipartFile2);

        NoticeDTO noticeDTO = new NoticeDTO();
        noticeDTO.setNoticeTitle("공지 제목 수정");
        noticeDTO.setNoticeContent("공지 내용 수정");
        noticeDTO.setNoticeFix('Z');

        // when
        String result = noticeService.updateNoticeWithFile(noticeNo, noticeDTO, files, memberNo);

        // then
        Assertions.assertEquals(result, "공지 수정 성공");
    }

    @DisplayName("공지 삭제")
    @Test
    void deleteNotice() {
        // given
        int noticeNo = 1;
        String memberNo = "1";

        // when
        noticeService.deleteNotice(noticeNo, memberNo);

        // then
        assertThrows(ChangeSetPersister.NotFoundException.class, () -> noticeService.getNoticeById(noticeNo));



    }

}
