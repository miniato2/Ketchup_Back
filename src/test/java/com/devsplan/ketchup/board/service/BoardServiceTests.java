package com.devsplan.ketchup.board.service;

import com.devsplan.ketchup.board.dto.BoardDTO;
import com.devsplan.ketchup.board.entity.Board;
import com.devsplan.ketchup.board.repository.BoardRepository;
import com.devsplan.ketchup.member.entity.Member;
import com.devsplan.ketchup.util.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@SpringBootTest
@AutoConfigureMockMvc
public class BoardServiceTests {

    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private FileUtils fileUtils;

    @DisplayName("자료실 게시물 등록")
    @Test
    void insertBoard() throws IOException {
        // given
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());


        BoardDTO boardInfo = new BoardDTO();
        boardInfo.setMemberNo(2);
        boardInfo.setDepartmentNo(1);
        boardInfo.setBoardTitle("title1");
        boardInfo.setBoardContent("content1");
        boardInfo.setBoardCreateDttm(timestamp);

        //when
        String result = boardService.insertBoard(boardInfo);

        //then
        Assertions.assertEquals(result, "성공");
    }

    @DisplayName("자료실 게시물 등록(파일첨부)")
    @Test
    void insertBoardWithFile() throws IOException {
        // given
        // 테스트할 파일 생성
        // given
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

//        File imageFile = new File(getClass().getClassLoader().getResource("jjang-gu.png").getFile());
//        FileInputStream input = new FileInputStream(imageFile);
//        MultipartFile multipartFile = new MockMultipartFile("test.png", imageFile.getName(), "image/png", IOUtils.toByteArray(input));

        // 이미지 파일 가져오기
        InputStream inputStream = getClass().getResourceAsStream("/jjang-gu.png");
//        File imageFile = new File("src/test/resources/jjang-gu.png");
//        FileInputStream input = new FileInputStream(inputStream);

        MultipartFile multipartFile = new MockMultipartFile("test.png", "jjang-gu.png", "image/png", inputStream);


        BoardDTO boardInfo = new BoardDTO();
        boardInfo.setMemberNo(2);
        boardInfo.setDepartmentNo(1);
        boardInfo.setBoardTitle("title4");
        boardInfo.setBoardContent("content4");
        boardInfo.setBoardCreateDttm(timestamp);

        // when
        Map<String, Object> result = new HashMap<>();

        try {
            List<MultipartFile> files = new ArrayList<>();
            files.add(multipartFile);
            boardService.insertBoardWithFile(boardInfo, files);
            result.put("result", true);
        } catch (Exception e) {
            result.put("result", false);
        }

        // then
//        Assertions.assertNotNull(result);
        Assertions.assertTrue((Boolean) result.get("result"));
//        Assertions.assertEquals(result.get("result"), true);
    }


    @DisplayName("자료실 게시물 목록조회 & 페이징 & 목록 제목검색 조회")
    @Test
    void selectBoardList() {
        //given
        int departmentNo = 1;
        int page = 0;
        int size = 10;
        String title = "";
        Pageable pageRequest = PageRequest.of(page, size, Sort.by("board_no").descending());

        //when
        Page<BoardDTO> boardList = boardService.selectBoardList(departmentNo, pageRequest, title);

        //then
        Assertions.assertNotNull(boardList);
        boardList.forEach(System.out::println);
    }

    @DisplayName("자료실 게시물 상세조회")
    @Test
    void selectBoardDetail() {
        //given
        int boardNo = 1;

        //when
        BoardDTO foundBoard = boardService.selectBoardDetail(boardNo);

        //then
        Assertions.assertNotNull(foundBoard);
    }


    @DisplayName("자료실 게시물 수정")
    @Test
    void updateBoard() {
        // given
        int boardNo = 2;
        int memberNo = 2;

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        BoardDTO boardInfo = new BoardDTO();
        boardInfo.setBoardTitle("title1");
        boardInfo.setBoardContent("content content");
        boardInfo.setBoardUpdateDttm(timestamp);

        //when
        String result = boardService.updateBoard(boardNo, boardInfo, memberNo);

        //then
        Assertions.assertEquals(result, "성공");
    }

    @DisplayName("자료실 게시물 삭제")
    @Test
    void deleteBoard() {
        // 테스트할 게시물 번호와 작성자 회원번호
        int boardNo = 1;
        int memberNo = 1;

        // 테스트: 게시물 삭제
        boardService.deleteBoard(boardNo, memberNo);

        // 삭제 후 해당 게시물이 존재하지 않는지 확인
        assertFalse(boardRepository.existsById(boardNo));
    }
}


