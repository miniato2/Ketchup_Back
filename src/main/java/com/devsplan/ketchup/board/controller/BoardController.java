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

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    /* 게시글 목록 조회 */
    @GetMapping("/boards")
    public ResponseEntity<ResponseDTO> selectBoardList() {

        return null;
    }


    /* 게시글 검색 */
    @GetMapping("/boards/{title}")
    public ResponseEntity<ResponseDTO> selectSearchboard(@PathVariable String title) {
        return ResponseEntity
                .ok()
                .body(new ResponseDTO(HttpStatus.OK, "조회 성공", boardService.selectBoardList(title)));
    }

    /* 게시글 상세 조회 */
    @GetMapping("/{boardNo}")
    public ResponseEntity<ResponseDTO> selectBoardDetail() {
        return null;
    }

    /* 게시글 등록 */
    @PostMapping("/insert-board")
    public ResponseEntity<ResponseDTO> insertBoard(@RequestBody BoardDTO boardDTO, MultipartFile boardFile, Principal principal) {

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "게시글 등록 성공"
                        , boardService.insertBoard(boardDTO, boardFile)));
    }

    /* 게시글 수정*/
    @PutMapping("/{boardNo}")
    public ResponseEntity<ResponseDTO> updateBoard(@PathVariable int boardNo, MultipartFile files, Principal principal) {

        return null;
    }

    /* 게시글 삭제 */
    @DeleteMapping("/{boardNo}")
    public ResponseEntity<ResponseDTO> deleteBoard() {

        return null;
    }

}
