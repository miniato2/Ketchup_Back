package com.devsplan.ketchup.notice.entity;

import jakarta.persistence.*;
import lombok.Builder;

@Entity
@Table(name = "tbl_notice_file")
@Builder(toBuilder = true)
public class NoticeFile {

    @Id
    @Column(name = "notice_file_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int noticeFileNo;            // 공지 파일 번호

    @Column(name = "notice_no", nullable = false)
    private int noticeNo;                  // 공지 번호

    @Column(name = "notice_file_imgurl", nullable = false)
    private String noticeFileImgUrl;       // 공지 파일 경로

    protected NoticeFile(){}

    public NoticeFile noticeNo(int val) {
        this.noticeNo = val;
        return this;
    }

    public NoticeFile noticeFileImgUrl(String val) {
        this.noticeFileImgUrl = val;
        return this;
    }

    public NoticeFile(int noticeFileNo, int noticeNo, String noticeFileImgUrl) {
        this.noticeFileNo = noticeFileNo;
        this.noticeNo = noticeNo;
        this.noticeFileImgUrl = noticeFileImgUrl;
    }

    public String getNoticeFileImgUrl() {
        return noticeFileImgUrl;
    }

}
