package com.devsplan.ketchup.board;

import com.devsplan.ketchup.board.dto.BoardDTO;
import com.devsplan.ketchup.board.entity.Board;
import com.devsplan.ketchup.board.repository.BoardRepository;
import com.devsplan.ketchup.board.service.BoardService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
public class BoardRestTests {

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private BoardService boardService;

    @DisplayName("자료실 게시글 목록조회")
    @Test
    void selectBoardList() {
//        /boards?departmentno={departmentno}&title={title}&page={pageno}
        //given
        //when
        //then
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

    @DisplayName("자료실 게시글 등록")
    @Test
    void insertBoard() {
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
