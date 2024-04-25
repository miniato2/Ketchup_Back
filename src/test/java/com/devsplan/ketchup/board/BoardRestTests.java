package com.devsplan.ketchup.board;

import com.devsplan.ketchup.board.dto.BoardDTO;
import com.devsplan.ketchup.board.dto.BoardFileDTO;
import com.devsplan.ketchup.board.entity.Board;
import com.devsplan.ketchup.board.repository.BoardRepository;
import com.devsplan.ketchup.board.service.BoardService;
import com.devsplan.ketchup.util.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class BoardRestTests {

    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private FileUtils fileUtils;

    @DisplayName("자료실 게시글 등록")
    @Test
    void insertBoard() {
        // given
        BoardDTO boardInfo = new BoardDTO();
        boardInfo.setMemberNo(1);
        boardInfo.setDepartmentNo(1);
        boardInfo.setBoardTitle("title4");
        boardInfo.setBoardContent("content4");
        boardInfo.setBoardCreateDttm(LocalDateTime.now());

        /*// 파일 업로드
        MultipartFile file1 = new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());

        // 파일 저장
        String fileName = null;
        try {
            fileName = fileUtils.saveFile("upload-dir", "testFile", file1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 파일 DTO 생성
        BoardFileDTO fileDTO = new BoardFileDTO();
        fileDTO.setBoardFileName(fileName);
        fileDTO.setBoardFilePath("upload-dir");
        fileDTO.setBoardFileSize(file1.getSize());
        fileDTO.setBoardOrigName(file1.getOriginalFilename());

        // 파일 DTO를 게시글 DTO에 추가
        List<BoardFileDTO> boardFiles = new ArrayList<>();
        boardFiles.add(fileDTO);
        boardInfo.setBoardFile(boardFiles);*/

        //when
        String result = boardService.insertBoard(boardInfo);

        //then
        Assertions.assertEquals(result, "성공");
    }

    @DisplayName("자료실 게시글 목록조회")
    @Test
    void selectBoardList() {
//        /boards?departmentno={departmentno}&page={pageno}&title={title}
        //given
        int departmentNo = 1;
        int page = 0;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by("board_no").descending());

        //when
        Page<BoardDTO> boardList = boardService.selectBoardList(departmentNo, page, size);
        //then
        Assertions.assertNotNull(boardList);
        boardList.forEach(System.out::println);
    }

    @DisplayName("자료실 게시글 상세조회")
    @Test
    void selectBoardDetail() {
        //given
        //when
        //then
    }
    @DisplayName("자료실 게시글 검색조회")
    @Test
    void selectBoardSearchList() {
        //given
        int departmentNo = 1;
        int page = 0;
        int size = 10;
        String title = "";

        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by("board_no").descending());

        //when
        Page<BoardDTO> boardList = boardService.selectBoardSearchList(departmentNo, page, size, title);
        //then
        Assertions.assertNotNull(boardList);
        boardList.forEach(System.out::println);
    }


    @DisplayName("자료실 게시글 수정")
    @Test
    void updateBoard() {
        //given
        //when
        //then
    }

    @DisplayName("자료실 게시글 삭제")
    @Test
    void deleteBoard() {
        //given
        //when
        //then
    }
}
