package com.devsplan.ketchup.board.controller;

import com.devsplan.ketchup.board.dto.BoardDTO;
import com.devsplan.ketchup.board.entity.Board;
import com.devsplan.ketchup.board.service.BoardService;
import com.devsplan.ketchup.common.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/")
public class BoardController {
//
//    private final BoardService boardService;
//
//    public BoardController(BoardService boardService) {
//        this.boardService = boardService;
//    }

}
