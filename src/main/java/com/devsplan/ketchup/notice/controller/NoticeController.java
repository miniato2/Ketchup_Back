package com.devsplan.ketchup.notice.controller;

import com.devsplan.ketchup.common.*;
import com.devsplan.ketchup.notice.dto.NoticeDTO;
import com.devsplan.ketchup.notice.service.NoticeService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.devsplan.ketchup.util.TokenUtils.decryptToken;

@RestController
@RequestMapping("/notices")
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
    public ResponseEntity<ResponseDTO> insertNotice(@RequestPart("noticeDTO")NoticeDTO noticeDTO
                                                    , @RequestPart(required = false)List<MultipartFile> files
                                                    , @RequestHeader("Authorization") String token) {
        try{
            String memberNo = decryptToken(token).get("memberNo", String.class);
            String authority = decryptToken(token).get("role").toString();

            System.out.println("[ authority ] : " + authority);

            // LV3 또는 LV2 권한을 가진 사원만 공지 등록 가능
            if (!authority.equals("LV3") && !authority.equals("LV2")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseDTO(HttpStatus.FORBIDDEN, "LV3 또는 LV2 권한자만 공지를 등록할 수 있습니다.", null));
            }

            // 파일이 첨부되었는지 여부에 따라 서비스 메서드 호출 방식을 변경
            if (files != null && !files.isEmpty()) {
                noticeService.insertNoticeWithFile(noticeDTO, files, memberNo);
            } else {
                noticeService.insertNotice(noticeDTO, memberNo);
            }

            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "공지 등록 성공", null));
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDTO("토큰이 만료되었습니다."));
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDTO("유효하지 않은 토큰입니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류", null));
        }
    }

    /* 공지 수정 */
    @PutMapping("/{noticeNo}")
    public ResponseEntity<ResponseDTO> updateNotice(@PathVariable int noticeNo
                                                    , @RequestPart("noticeDTO")NoticeDTO noticeDTO
                                                    , @RequestPart(required = false, name = "files")List<MultipartFile> files
                                                    , @RequestHeader("Authorization") String token) {
        try {
            String memberNo = decryptToken(token).get("memberNo", String.class);
            String authority = decryptToken(token).get("role").toString();

            // LV3 또는 LV2 권한을 가진 권한자 또는 해당 공지의 등록자만 공지 수정 가능
            if ("LV3".equals(authority) || "LV2".equals(authority)) {
                // 파일이 첨부되었는지 여부에 따라 서비스 메서드 호출 방식을 변경
                if (files != null && !files.isEmpty()) {
                    noticeService.updateNoticeWithFile(noticeNo, noticeDTO, files, memberNo);
                } else {
                    noticeService.updateNotice(noticeNo, noticeDTO, memberNo);
                }

                return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "공지 수정 성공", null));
            } else {
                // 권한이 없는 경우
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ResponseDTO(HttpStatus.FORBIDDEN, "LV3 또는 LV2 권한자 또는 공지 등록자만 공지를 수정할 수 있습니다.", null));
            }
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
    public ResponseEntity<ResponseDTO> deleteNotice(@PathVariable int noticeNo
                                                    , @RequestHeader("Authorization") String token) {
        try{
            String memberNo = decryptToken(token).get("memberNo", String.class);
            String authority = decryptToken(token).get("role").toString();

            // 권한이 LV3 또는 LV2이거나 작성자와 일치하는 경우에만 삭제 시도
            if ("LV3".equals(authority) || "LV2".equals(authority)) {
                // 공지 삭제 시도
                boolean isDeleted = noticeService.deleteNotice(noticeNo, memberNo);

                // 삭제가 성공한 경우
                if (isDeleted) {
                    return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "공지 삭제 성공", null));
                } else {
                    // 공지를 찾을 수 없는 경우
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(HttpStatus.NOT_FOUND, "공지를 찾을 수 없습니다.", null));
                }
            } else {
                // 권한이 없는 경우
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ResponseDTO(HttpStatus.FORBIDDEN, "삭제 권한이 없습니다.", null));
            }

        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDTO("토큰이 만료되었습니다."));
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDTO("유효하지 않은 토큰입니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류", null));
        }
    }
}
