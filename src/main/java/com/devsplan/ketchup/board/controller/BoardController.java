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

    public  BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    /* 게시글 목록 조회 */
    @GetMapping("/boards")
    public ResponseEntity<ResponseDTO> selectBoardList(
            @RequestParam(name = "departmentno", required = false) int departmentNo,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "page", defaultValue = "0") int pageNo,
            @RequestParam(name = "size", defaultValue = "10") int pageSize
    ) {
//        /boards?departmentno={departmentno}&title={title}&page={pageno}

//        List<BoardDTO> boardList;
//        if (departmentNo != null && title != null) {
//            // 부서별 자료실에서 특정 제목으로 검색하는 경우
//            boardList = boardService.getBoardListByDepartmentAndTitle(departmentNo, title, pageNo, pageSize);
//        } else if (departmentNo != null) {
//            // 특정 부서의 게시글 목록을 조회하는 경우
//            boardList = boardService.getBoardListByDepartment(departmentNo, pageNo, pageSize);
//        } else {
//            // 모든 부서의 게시글 목록을 조회하는 경우
//            boardList = boardService.getAllBoardList(pageNo, pageSize);
//        }
//        return ResponseEntity.ok(ResponseDTO.builder().data(boardList).build());
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

        //        return "boards/detail-board";
    }

    /* 게시글 등록 */
    @PostMapping("/insert-board")
    public ResponseEntity<ResponseDTO> insertBoard(@RequestBody BoardDTO boardDTO, MultipartFile boardFile, Principal principal) {

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "게시글 등록 성공"
                        , boardService.insertBoard(boardDTO, boardFile)));
    //        return "boards/insert-board";
    }

    /* 게시글 수정*/
    @PutMapping("/{boardNo}")
    public ResponseEntity<ResponseDTO> updateBoard(@PathVariable int boardNo, MultipartFile files, Principal principal) {


        return null;
//            return "redirect:/boards/detail-board";
    }

    /* 게시글 삭제 */
    @DeleteMapping("/{boardNo}")
    public ResponseEntity<ResponseDTO> deleteBoard() {

        return null;

    //        return  "redirect:/boards/list";
    }

}
