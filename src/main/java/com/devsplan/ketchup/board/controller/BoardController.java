package com.devsplan.ketchup.board.controller;

import com.devsplan.ketchup.board.dto.BoardDTO;
import com.devsplan.ketchup.board.service.BoardService;
import com.devsplan.ketchup.common.Pagenation;
import com.devsplan.ketchup.common.PagingButton;
import com.devsplan.ketchup.common.ResponseDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    @Value("${jwt.key}")
    private String jwtSecret;

    public BoardController(BoardService boardService){
        this.boardService = boardService;
    }

    /* 게시물 목록 조회(부서, 페이징) */
    @GetMapping
    public ResponseEntity<ResponseDTO> selectBoardList(@PageableDefault(sort = "boardCreateDttm", direction = Sort.Direction.DESC) Pageable pageable
                                                        , @RequestParam(required = false) String title
                                                        , @RequestHeader("Authorization") String token) {

        try {
            Page<BoardDTO> boardList;
            PagingButton paging;

            // "Bearer " 이후의 토큰 값만 추출
            String jwtToken = token.substring(7);

            // 토큰 파싱하여 클레임 추출
            Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwtToken).getBody();

            // 클레임에서 depNo 추출
            Integer depNo = claims.get("depNo", Integer.class);
            System.out.println("depNo : " + depNo);

                if (depNo == null) {
                    // 부서 번호가 없는 경우 모든 부서의 자료실 정보를 조회합니다.
                    boardList = boardService.selectAllBoards(pageable, title);
                } else {
                    // 특정 부서의 자료실 정보를 조회합니다.
                    boardList = boardService.selectBoardList(depNo, pageable, title);
                    System.out.println("depNo : " + depNo);
                }

                paging = Pagenation.getPagingButtonInfo(boardList);
                return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "목록 조회 성공", paging));

        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDTO("토큰이 만료되었습니다."));
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDTO("유효하지 않은 토큰입니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류", null));
        }
    }

    /* 게시물 상세 조회 */
    @GetMapping("/{boardNo}")
    public ResponseEntity<ResponseDTO> selectBoardDetail(@PathVariable("boardNo") int boardNo, @RequestHeader("Authorization") String token) {

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

            // "Bearer " 이후의 토큰 값만 추출
            String jwtToken = token.substring(7);

            // 토큰 파싱하여 클레임 추출
            Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwtToken).getBody();

            // 클레임에서 depNo 추출
            Integer depNo = claims.get("depNo", Integer.class);

            // 게시물 상세 정보 조회
            BoardDTO boardDTO = boardService.selectBoardDetail(boardNo);
            if (boardDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO(HttpStatus.NOT_FOUND, "게시물을 찾을 수 없습니다.", null));
            }

            // 해당 게시물을 볼 수 있는지 확인
            if (boardDTO.getDepartmentNo() != depNo) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ResponseDTO(HttpStatus.FORBIDDEN, "해당 게시물에 접근할 수 있는 권한이 없습니다.", null));
            }

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new ResponseDTO(HttpStatus.OK, "상세 조회 성공", boardDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류", null));
        }
    }

    /* 게시물 등록 */
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ResponseDTO> insertBoard(@RequestPart("boardInfo") BoardDTO boardDTO
                                                   , @RequestPart(required = false, name="files") List<MultipartFile> files
                                                   , @RequestHeader("Authorization") String token) throws IOException {
        try {
            // "Bearer " 이후의 토큰 값만 추출
            String jwtToken = token.substring(7);

            // 토큰 파싱하여 클레임 추출
            Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwtToken).getBody();

            // 클레임에서 depNo 추출
            String memberNo = claims.get("memberNo", String.class);
            Integer depNo = claims.get("depNo", Integer.class);

            System.out.println("memberNo : " + memberNo);
            System.out.println("depNo : " + depNo);
            System.out.println("File Content: " + new String(files.toString().getBytes(), StandardCharsets.UTF_8));

            boardDTO.setMemberNo(memberNo);
            boardDTO.setDepartmentNo(depNo);

            // 게시물 등록 요청한 회원의 부서 번호와 게시물에 등록하려는 부서 번호가 일치하는지 확인
            if (boardDTO.getDepartmentNo() != depNo) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ResponseDTO(HttpStatus.FORBIDDEN, "해당 부서의 게시물만 등록할 수 있습니다.", null));
            }

//             파일이 첨부되었는지 여부에 따라 서비스 메서드 호출 방식을 변경
            if (files != null && !files.isEmpty()) {

            boardService.insertBoardWithFile(boardDTO, files);
            } else {

                boardService.insertBoard(boardDTO);
            }
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "게시물 등록 성공", null));

        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDTO("토큰이 만료되었습니다."));
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDTO("유효하지 않은 토큰입니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류", null));
        }


    }


    /* 게시물 수정 */
    @PutMapping("/{boardNo}")
    public ResponseEntity<ResponseDTO> updateBoard(@PathVariable int boardNo
                                                    , @RequestPart("boardInfo") BoardDTO boardInfo
                                                    , @RequestPart("files") List<MultipartFile> files
                                                    , @RequestHeader("Authorization") String token) {

        // "Bearer " 이후의 토큰 값만 추출
        String jwtToken = token.substring(7);

        // 토큰 파싱하여 클레임 추출
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwtToken).getBody();

        // 클레임에서 depNo 추출
        String memberNo = claims.get("memberNo", String.class);

        System.out.println("memberNo : " + memberNo);



        // 파일이 첨부되었는지 여부에 따라 서비스 메서드 호출 방식을 변경
        if (files != null && !files.isEmpty()) {
            boardService.updateBoardWithFile(boardNo, boardInfo, files, memberNo);
        } else {
            boardService.updateBoard(boardNo, boardInfo, memberNo);
        }

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "게시물 수정 성공", null));
    }


    /* 게시물 삭제 */
    @DeleteMapping("/{boardNo}")
    public ResponseEntity<ResponseDTO> deleteBoard(@PathVariable int boardNo, @RequestHeader("Authorization") String token) {

        try {
            // "Bearer " 이후의 토큰 값만 추출
            String jwtToken = token.substring(7);

            // 토큰 파싱하여 클레임 추출
            Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwtToken).getBody();

            // 클레임에서 회원 번호 추출
            String memberNo = claims.get("memberNo", String.class);

            // 게시물 삭제 시도
            boolean isDeleted = boardService.deleteBoard(boardNo, memberNo);

            if(isDeleted) {
                return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "삭제 성공", isDeleted));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO("서버 오류"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "게시물을 찾을 수 없습니다.", null));
        }
    }
}
