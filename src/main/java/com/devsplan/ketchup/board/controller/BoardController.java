package com.devsplan.ketchup.board.controller;

import ch.qos.logback.core.pattern.util.RegularEscapeUtil;
import com.devsplan.ketchup.board.dto.BoardDTO;
import com.devsplan.ketchup.board.entity.Board;
import com.devsplan.ketchup.board.service.BoardService;
import com.devsplan.ketchup.common.Pagenation;
import com.devsplan.ketchup.common.PagingButton;
import com.devsplan.ketchup.common.ResponseDTO;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    /* 게시글 목록 조회(부서, 페이징) */
    @GetMapping
    public ResponseEntity<ResponseDTO> selectBoardList(@RequestParam(required = false) Integer departmentNo
                                                        , @PageableDefault(page = 0, size = 10, sort = "boardCreateDttm", direction = Sort.Direction.DESC) Pageable pageable
                                                        , @RequestParam(required = false) String title) {


//                Page<BoardDTO> boardList = boardService.selectBoardList(departmentNo, pageable, title);
//                PagingButton paging = Pagenation.getPagingButtonInfo(boardList);
//                return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "목록 조회 성공", paging));
        try {
            Page<BoardDTO> boardList;
            PagingButton paging;

            if (departmentNo == null) {
                // 부서 번호가 없는 경우 모든 부서의 자료실 정보를 조회합니다.
                boardList = boardService.selectAllBoards(pageable, title);
                paging = Pagenation.getPagingButtonInfo(boardList);
            } else {
                // 특정 부서의 자료실 정보를 조회합니다.
                boardList = boardService.selectBoardList(departmentNo, pageable, title);
                paging = Pagenation.getPagingButtonInfo(boardList);
            }

            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "목록 조회 성공", paging));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류", null));
        }
    }

    /* 게시글 상세 조회 */
    @GetMapping("/{boardNo}")
    public ResponseEntity<ResponseDTO> selectBoardDetail(@PathVariable("boardNo") int boardNo) {

        return ResponseEntity.ok()
                .body(new ResponseDTO(HttpStatus.OK, "상세 조회 성공", boardService.selectBoardDetail(boardNo)));
    }


    /* 게시글 등록 */
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ResponseDTO> insertBoard(@RequestPart("boardInfo") BoardDTO boardDTO
                                                   , @RequestPart("files") List<MultipartFile> files) throws IOException {

//        boardDTO.setBoardCreateDttm(LocalDateTime.now());

        // 파일이 첨부되었는지 여부에 따라 서비스 메서드 호출 방식을 변경
        if (files != null && !files.isEmpty()) {
            boardService.insertBoardWithFile(boardDTO, files);
        } else {
            boardService.insertBoard(boardDTO);
        }

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "게시물 등록 성공", null));
    }


    /* 게시글 수정 */
    @PutMapping("/{boardNo}")
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

        /* , Principal principal
        // 현재 로그인한 사용자의 정보를 가져옵니다.
        String loggedInUsername = principal.getName();

        // 게시물 작성자의 정보를 가져옵니다. (여기서는 예시로 boardService에서 작성자 정보를 가져오는 메서드를 사용하도록 가정합니다.)
        String authorUsername = boardService.getAuthorUsernameByBoardNo(boardNo);

        // 작성자와 현재 로그인한 사용자가 일치하는지 확인합니다.
        if (loggedInUsername.equals(authorUsername)) {
            // 파일이 첨부되었는지 여부에 따라 서비스 메서드 호출 방식을 변경
            if (file != null && !file.isEmpty()) {
                boardService.updateBoardWithFile(boardNo, boardInfo, file);
            } else {
                boardService.updateBoard(boardNo, boardInfo);
            }
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "게시물 수정 성공", null));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseDTO("작성자만 게시물을 수정할 수 있습니다."));
        }

        */

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
