package com.devsplan.ketchup.board;

import com.devsplan.ketchup.board.dto.BoardDTO;
import com.devsplan.ketchup.board.service.BoardService;
import com.devsplan.ketchup.util.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class BoardRestTests {

    @Autowired
    private BoardService boardService;

    @Autowired
    private FileUtils fileUtils;

    @DisplayName("자료실 게시글 등록")
    @Test
    void insertBoard(/*int boardNo, String boardTitle, List<BoardFileDTO> boardFiles, String boardContent, LocalDateTime boardCreateDttm*/) {
        // given


        BoardDTO boardInfo = new BoardDTO();
        boardInfo.setBoardNo(1);
        boardInfo.setMemberNo(1);
        boardInfo.setDepartmentNo(1);
        boardInfo.setBoardTitle("title");
        boardInfo.setBoardFile(null);
        boardInfo.setBoardContent("content");
        boardInfo.setBoardCreateDttm(LocalDateTime.now());

        //when
        String result = (String) boardService.insertBoard(boardInfo);

        //then
        Assertions.assertEquals(result, "성공");

    }

    @DisplayName("자료실 게시글 목록조회")
    @Test
    void selectBoardList() {
//        /boards?departmentno={departmentno}&title={title}&page={pageno}
        //given
        //when
        List<BoardDTO> boardList = boardService.selectBoardList();
        //then
        Assertions.assertNotNull(boardList);
        boardList.forEach(System.out::println);
    }

    @DisplayName("자료실 게시글 검색조회")
    @Test
    void selectBoardSearchList() {
        //given
        //when
        //then
    }

    @DisplayName("자료실 게시글 상세조회")
    @Test
    void selectBoardDetail() {
        //given
        //when
        //then
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
