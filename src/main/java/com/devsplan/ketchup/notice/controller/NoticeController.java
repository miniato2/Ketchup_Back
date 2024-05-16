package com.devsplan.ketchup.notice.controller;

import com.devsplan.ketchup.common.*;
import com.devsplan.ketchup.notice.dto.NoticeDTO;
import com.devsplan.ketchup.notice.service.NoticeService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.devsplan.ketchup.util.TokenUtils.decryptToken;

@RestController
@RequestMapping("/notices")
@Slf4j
public class NoticeController {

    private final NoticeService noticeService;

    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    /* 공지 목록 조회(페이징, 제목검색) */
    @GetMapping
    public ResponseEntity<ResponseDTO> selectNoticeList(@RequestParam(name = "offset", defaultValue = "1") String offset
                                                        , @RequestParam(required = false) String title) {
        try {
            Criteria cri = new Criteria(Integer.parseInt(offset),10);
            PagingResponseDTO pagingResponseDTO = new PagingResponseDTO();

            Page<NoticeDTO> noticeList = noticeService.selectNoticeList(cri, title);
            pagingResponseDTO.setData(noticeList);

            pagingResponseDTO.setPageInfo(new PageDTO(cri, (int) noticeList.getTotalElements()));

            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "목록 조회 성공", pagingResponseDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류", null));
        }
    }

    /* 공지 상세 조회(파일 다운) */
    @GetMapping("/{noticeNo}")
    public ResponseEntity<ResponseDTO> selectNoticeDetail(@PathVariable("noticeNo") int noticeNo) {
        try {
            // 공지 상세 정보 조회
            NoticeDTO noticeDTO = noticeService.selectNoticeDetail(noticeNo);

            if (noticeDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(HttpStatus.NOT_FOUND, "공지사항을 찾을 수 없습니다.", null));
            }

            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "상세 조회 성공", noticeDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류", null));
        }
    }

    /* 공지 등록 */
    @PostMapping(consumes = {"multipart/form-data"})
    @PreAuthorize("hasAnyAuthority('LV3', 'LV2')")
    public ResponseEntity<ResponseDTO> insertNotice(@RequestPart("noticeDTO") NoticeDTO noticeDTO
                                                    , @RequestPart(value = "files", required = false) List<MultipartFile> files
                                                    , @RequestHeader("Authorization") String token) {
        try{
            String memberNo = decryptToken(token).get("memberNo", String.class);
            String authority = decryptToken(token).get("role").toString();
            System.out.println("[ authority ] : " + authority);

            Object data;
            if (files != null && !files.isEmpty()) {
                data = noticeService.insertNoticeWithFile(noticeDTO, files, memberNo);
            } else {
                data = noticeService.insertNotice(noticeDTO, memberNo);
            }

            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "공지 등록 성공", data));
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDTO("토큰이 만료되었습니다."));
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDTO("유효하지 않은 토큰입니다."));
        } catch (Exception e) {
            log.error("공지 등록 실패: {}", e.getMessage(), e);
            throw new RuntimeException("공지 등록 중 오류가 발생했습니다: " + e.getMessage()); // 수정된 부분
        }
    }

    /* 공지 수정 */
    @PutMapping("/{noticeNo}")
    @PreAuthorize("hasAnyAuthority('LV3', 'LV2')")
    public ResponseEntity<ResponseDTO> updateNotice(@PathVariable int noticeNo
                                                    , @RequestPart("noticeDTO")NoticeDTO noticeDTO
                                                    , @RequestPart(required = false, name = "files")List<MultipartFile> files
                                                    , @RequestHeader("Authorization") String token) {
        try {
            String memberNo = decryptToken(token).get("memberNo", String.class);

            Object data;
            // 파일이 첨부되었는지 여부에 따라 서비스 메서드 호출 방식을 변경
            if (files != null && !files.isEmpty()) {
                data = noticeService.updateNoticeWithFile(noticeNo, noticeDTO, files, memberNo);
            } else {
                data = noticeService.updateNotice(noticeNo, noticeDTO, memberNo);
            }

            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "공지 수정 성공", data));

        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDTO("토큰이 만료되었습니다."));
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDTO("유효하지 않은 토큰입니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류", null));
        }
    }

    /* 공지 삭제 */
    @DeleteMapping("/{noticeNo}")
    @PreAuthorize("hasAnyAuthority('LV3', 'LV2')")
    public ResponseEntity<ResponseDTO> deleteNotice(@PathVariable int noticeNo
                                                    , @RequestHeader("Authorization") String token) {
        try{
            String memberNo = decryptToken(token).get("memberNo", String.class);

            System.out.println("controller memberNo : " + memberNo);
            System.out.println("controller noticeNo : " + noticeNo);
            // 공지 삭제 시도
            Object data = noticeService.deleteNotice(noticeNo, memberNo);

            // 삭제가 성공한 경우
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "공지 삭제 성공", data));

        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDTO("토큰이 만료되었습니다."));
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDTO("유효하지 않은 토큰입니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류", null));
        }
    }
}
