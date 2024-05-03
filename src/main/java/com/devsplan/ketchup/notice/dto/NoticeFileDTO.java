package com.devsplan.ketchup.notice.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class NoticeFileDTO {

    private int noticeNo;                // 공지 번호
    private int noticeFileNo;            // 공지 파일 번호
    private String noticeFileImgUrl;       // 공지 파일 경로

    public NoticeFileDTO(int noticeNo, String noticeFileImgUrl) {
        this.noticeNo = noticeNo;
        this.noticeFileImgUrl = noticeFileImgUrl;
    }
}
