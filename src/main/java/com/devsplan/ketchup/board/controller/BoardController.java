package com.devsplan.ketchup.board.controller;

import ch.qos.logback.core.pattern.util.RegularEscapeUtil;
import com.devsplan.ketchup.board.dto.BoardDTO;
import com.devsplan.ketchup.board.entity.Board;
import com.devsplan.ketchup.board.service.BoardService;
import com.devsplan.ketchup.common.ResponseDTO;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    /* 게시글 목록 조회 */

    /* 게시글 상세 조회 */


    /* 게시글 등록 */
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ResponseDTO> insertBoard(@RequestPart("boardInfo") BoardDTO boardDTO
                                                   , @RequestPart("files") List<MultipartFile> files) throws IOException {

        // 파일이 첨부되었는지 여부에 따라 서비스 메서드 호출 방식을 변경
        if (files != null && !files.isEmpty()) {
            boardService.insertBoardWithFile(boardDTO, files);
        } else {
            boardService.insertBoard(boardDTO);
        }

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "게시물 등록 성공", null));
    }


    /* 게시글 수정 */
    @PostMapping("/{boardNo}")
    public ResponseEntity<ResponseDTO> updateBoard(@PathVariable int boardNo
                                                    , @RequestPart("boardInfo") BoardDTO boardInfo
                                                    , @RequestPart("files") List<MultipartFile> file
                                                    , @RequestParam int memberNo) {

        // 파일이 첨부되었는지 여부에 따라 서비스 메서드 호출 방식을 변경
        if (file != null && !file.isEmpty()) {
            boardService.updateBoardWithFile(boardNo, boardInfo, file, memberNo);
        } else {
            boardService.updateBoard(boardNo, boardInfo, memberNo);
        }

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "게시물 등록 성공", null));

    }


    /* 게시글 삭제 */
    @DeleteMapping("/{boardNo}")
    public ResponseEntity<ResponseDTO> deleteBoard(@PathVariable int boardNo, @RequestParam int memberNo) {

        boolean isDeleted = boardService.deleteBoard(boardNo, memberNo);

        if(isDeleted) {
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "삭제 성공", isDeleted));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO("게시물을 찾을 수 없습니다."));
        }
    }
}
