package com.devsplan.ketchup.notice.dto;

import com.devsplan.ketchup.board.dto.BoardFileDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@ToString
public class NoticeDTO {

    private int noticeNo;                        // 공지 번호
    private String memberNo;                     // 사번
    private char noticeFix;                      // 고정 공지
    private String noticeTitle;                  // 공지 제목
    private List<NoticeFileDTO> noticeFiles;     // 공지 파일들
    private String noticeContent;                // 공지 내용
    private Timestamp noticeCreateDttm;          // 공지 등록일시
    private Timestamp noticeUpdateDttm;          // 공지 수정일시
    private String noticeFilePath;               // 공지 파일 경로


    public NoticeDTO(int noticeNo, String memberNo, char noticeFix, String noticeTitle, String noticeContent, Timestamp noticeCreateDttm, String noticeFilePath) {
        this.noticeNo = noticeNo;
        this.memberNo = memberNo;
        this.noticeFix = noticeFix;
        this.noticeTitle = noticeTitle;
        this.noticeContent = noticeContent;
        this.noticeCreateDttm = noticeCreateDttm;
        this.noticeFilePath = noticeFilePath;
    }

    public NoticeDTO(int noticeNo, String memberNo, char noticeFix, String noticeTitle, String noticeContent, List<NoticeFileDTO> noticeFiles, Timestamp noticeCreateDttm, String noticeFilePath) {
        this.noticeNo = noticeNo;
        this.memberNo = memberNo;
        this.noticeFix = noticeFix;
        this.noticeTitle = noticeTitle;
        this.noticeContent = noticeContent;
        this.noticeFiles = noticeFiles;
        this.noticeCreateDttm = noticeCreateDttm;
        this.noticeFilePath = noticeFilePath;
    }


    public NoticeDTO(int noticeNo, String memberNo, char noticeFix, String noticeTitle, List<NoticeFileDTO> noticeFiles, String noticeContent, Timestamp noticeUpdateDttm, String noticeFilePath) {
        this.noticeNo = noticeNo;
        this.memberNo = memberNo;
        this.noticeFix = noticeFix;
        this.noticeTitle = noticeTitle;
        this.noticeFiles = noticeFiles;
        this.noticeContent = noticeContent;
        this.noticeUpdateDttm = noticeUpdateDttm;
        this.noticeFilePath = noticeFilePath;
    }
}
