package com.devsplan.ketchup.board.controller;

import ch.qos.logback.core.pattern.util.RegularEscapeUtil;
import com.devsplan.ketchup.auth.filter.JwtAuthorizationFilter;
import com.devsplan.ketchup.board.dto.BoardDTO;
import com.devsplan.ketchup.board.entity.Board;
import com.devsplan.ketchup.board.entity.BoardFile;
import com.devsplan.ketchup.board.service.BoardService;
import com.devsplan.ketchup.common.AuthConstants;
import com.devsplan.ketchup.common.Pagenation;
import com.devsplan.ketchup.common.PagingButton;
import com.devsplan.ketchup.common.ResponseDTO;
import com.devsplan.ketchup.member.entity.Dep;
import com.devsplan.ketchup.member.entity.Member;
import com.devsplan.ketchup.member.service.MemberService;
import com.devsplan.ketchup.util.FileUtils;
import com.devsplan.ketchup.util.TokenUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;
    private final MemberService memberService;

    public BoardController(BoardService boardService, MemberService memberService){
        this.boardService = boardService;
        this.memberService = memberService;
    }

    /* 게시물 목록 조회(부서, 페이징) */
    @GetMapping
    public ResponseEntity<ResponseDTO> selectBoardList(@RequestParam(required = false) Integer departmentNo
                                                        , @PageableDefault(page = 0, size = 10, sort = "boardCreateDttm", direction = Sort.Direction.DESC) Pageable pageable
                                                        , @RequestParam(required = false) String title
                                                        , Principal principal) {

        try {
            Page<BoardDTO> boardList;
            PagingButton paging;

            // 현재 로그인한 사용자의 아이디를 가져옵니다.
            String loginMemberNo = principal.getName();
//            System.out.println("loginMemberNo : " + loginMemberNo);

            // 데이터베이스에서 현재 로그인한 사용자의 정보를 가져옵니다.
            Optional<Member> memberOptional = memberService.findMember(loginMemberNo);
//            System.out.println("memberOptional : " + memberOptional);

            // 현재 로그인한 사용자가 존재하고, 사용자 정보가 올바르게 가져와졌는지 확인합니다.
            if (memberOptional.isPresent()) {

                Integer userDepartmentNo = memberOptional.map(Member::getDepartment).map(Dep::getDepNo).orElse(null);
                System.out.println("userDepartmentNo : " + userDepartmentNo);
                System.out.println("departmentNo : " + departmentNo);

                if (departmentNo == null) {
                    // 부서 번호가 없는 경우 모든 부서의 자료실 정보를 조회합니다.
                    boardList = boardService.selectAllBoards(pageable, title);
                    paging = Pagenation.getPagingButtonInfo(boardList);

                } else {
                    // 특정 부서의 자료실 정보를 조회합니다.
                    System.out.println("departmentNo : " + departmentNo);
                    System.out.println("userDepartmentNo : " + userDepartmentNo);
                    boardList = boardService.selectBoardList(departmentNo, pageable, title);
                    paging = Pagenation.getPagingButtonInfo(boardList);
                }

                return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "목록 조회 성공", paging));
            } else {
                // 사용자 정보가 없는 경우에 대한 처리를 수행합니다.
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDTO("로그인한 사용자 정보를 가져올 수 없습니다."));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류", null));
        }
    }

    /* 게시물 상세 조회 */
    @GetMapping("/{boardNo}")
    public ResponseEntity<ResponseDTO> selectBoardDetail(@PathVariable("boardNo") int boardNo) throws IOException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        // 게시글 상세 정보 조회
        BoardDTO boardDTO = boardService.selectBoardDetail(boardNo);
        List<BoardFile> boardFiles = boardService.findBoardFilesByBoardNo(boardNo);

        Map<String, Object> result = new HashMap<>();
        result.put("board", boardDTO);

        // 파일 목록을 저장할 리스트
        List<Map<String, Object>> fileResponses = new ArrayList<>();

        for (BoardFile boardFile : boardFiles) {
            String filePath = boardFile.getBoardFilePath();
            byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));

            String fileName = boardFile.getBoardFileName();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");

            // 파일의 내용과 헤더를 클라이언트에 반환
            Map<String, Object> fileResponse = new HashMap<>();
            fileResponse.put("fileName", fileName);
            fileResponse.put("fileContent", Base64.getEncoder().encodeToString(fileBytes));
            fileResponses.add(fileResponse);
        }

        // 게시글과 파일 정보를 함께 반환
        result.put("files", fileResponses);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new ResponseDTO(HttpStatus.OK, "상세 조회 성공", boardService.selectBoardDetail(boardNo)));
    }


    /* 게시물 등록 */
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ResponseDTO> insertBoard(@RequestPart("boardInfo") BoardDTO boardDTO
                                                   , @RequestPart("files") List<MultipartFile> files
                                                    , Principal principal) throws IOException {

        // 현재 로그인한 사용자의 아이디를 가져옵니다.
//        String loggedInUsername = principal.getName();

        // 게시물 작성자로 현재 사용자의 아이디를 설정합니다.
//        int memberNo = userService.findMemberNoByUsername(loggedInUsername);
//        boardDTO.setMemberNo(loggedInUsername);

        // 파일이 첨부되었는지 여부에 따라 서비스 메서드 호출 방식을 변경
//        if (files != null && !files.isEmpty()) {
//            boardService.insertBoardWithFile(boardDTO, files);
//        } else {
//            boardService.insertBoard(boardDTO);
//        }
//
//        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "게시물 등록 성공", null));

        // 현재 로그인한 사용자의 아이디를 가져옵니다.
        String loginMemberNo = principal.getName();

        // 데이터베이스에서 현재 로그인한 사용자의 정보를 가져옵니다.
        Optional<Member> memberOptional = memberService.findMember(loginMemberNo);

        // 현재 로그인한 사용자가 존재하고, 사용자 정보가 올바르게 가져와졌는지 확인합니다.
        if (memberOptional.isPresent()) {
            // 게시물 작성자로 현재 사용자의 정보를 설정합니다.
            Member member = memberOptional.get();
            boardDTO.setMemberNo(member.getMemberNo());
            boardDTO.setDepartmentNo(member.getDepartment().getDepNo());

            // 파일이 첨부되었는지 여부에 따라 서비스 메서드 호출 방식을 변경
            if (files != null && !files.isEmpty()) {
                boardService.insertBoardWithFile(boardDTO, files);
            } else {
                boardService.insertBoard(boardDTO);
            }

            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "게시물 등록 성공", null));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDTO("로그인한 사용자 정보를 가져올 수 없습니다."));
        }

    }


    /* 게시물 수정 */
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


    /* 게시물 삭제 */
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
