package com.devsplan.ketchup.notice.dto;

import lombok.*;

@AllArgsConstructor
@Setter
@Getter
@ToString
public class NoticeFileDTO {

    private int noticeNo;                // 공지 번호
    private int noticeFileNo;            // 공지 파일 번호
    private String noticeFileName;       // 공지 파일명
    private String noticeFilePath;       // 공지 파일 경로
    private String noticeOriginName;     // 공지 원본 파일명
    private Long noticeFileSize;         // 공지 파일 사이즈
    private String noticeFileType;       // 공지 파일 타입

    public NoticeFileDTO(int noticeNo, String noticeFileName) {
        this.noticeNo = noticeNo;
        this.noticeFileName = noticeFileName;
    }

    public NoticeFileDTO() {
    }
    ////
}
