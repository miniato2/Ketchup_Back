package com.devsplan.ketchup.board;

import com.devsplan.ketchup.board.repository.BoardRepository;
import com.devsplan.ketchup.board.service.BoardService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BoardControllerTests {

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private BoardService service;

    @DisplayName("자료실 게시글 목록조회")
    @Test
    void selectBoardList() {
        //given
        //when
        //then
    }

    @Test
    void selectBoardDetail() {
        //given
        //when
        //then
    }

    @Test
    void insertBoard() {
        //given
        //when
        //then
    }

    @Test
    void updateBoard() {
        //given
        //when
        //then
    }

    @Test
    void deleteBoard() {
        //given
        //when
        //then
    }





}
