package com.devsplan.ketchup.board.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/content")
public class BoardController {

    @GetMapping("/boards")
    public String allBoard() {
        return "content/boards";
    }

    @GetMapping("/board-detail")
    public String selectBoardDetail() {
        return "content/board-detail";
    }

    @GetMapping("/insert-board")
    public String insertBoard() {
        return "content/insert-board";
    }

}
