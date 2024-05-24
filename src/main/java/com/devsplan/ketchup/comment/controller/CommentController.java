package com.devsplan.ketchup.comment.controller;

import com.devsplan.ketchup.comment.dto.CommentDTO;
import com.devsplan.ketchup.comment.service.CommentService;
import com.devsplan.ketchup.common.ResponseDTO;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.devsplan.ketchup.util.TokenUtils.decryptToken;

@Slf4j
@RestController
@RequestMapping("/boards")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /* 댓글 조회 */
    @GetMapping("/{boardNo}/comments")
    public ResponseEntity<ResponseDTO> getCommentsByBoardNo(@PathVariable int boardNo) {
        try {

            List<CommentDTO> commentList = commentService.selectCommentList(boardNo);
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "댓글 조회 성공", commentList));
        } catch (Exception e) {
            log.error("댓글 조회 중 오류 발생: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "댓글 조회 중 오류 발생", null));
        }
    }

    /* 댓글 등록 */
    @PostMapping("/{boardNo}/comments")
    public ResponseEntity<ResponseDTO> insertComment(@PathVariable("boardNo") int boardNo
            ,@RequestBody CommentDTO commentDTO
            ,@RequestHeader("Authorization") String token) {
        try {
            String memberNo = decryptToken(token).get("memberNo", String.class);

            Object data = commentService.insertComment(boardNo, commentDTO, memberNo);

            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "댓글 작성 성공", data));
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDTO("토큰이 만료되었습니다."));
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDTO("유효하지 않은 토큰입니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류", null));
        }
    }

}
