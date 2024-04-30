package com.devsplan.ketchup.notice.entity;

import jakarta.persistence.*;
import lombok.Builder;

@Entity
@Table(name = "tbl_notice_file")
@Builder(toBuilder = true)
public class NoticeFile {

    //
    //

    @Id
    @Column(name = "notice_file_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int noticeFileNo;            // 공지 파일 번호

    @Column(name = "notice_file_name", nullable = false)
    private String noticeFileName;       // 공지 파일명

    @Column(name = "notice_file_path", nullable = false)
    private String noticeFilePath;       // 공지 파일 경로

    @Column(name = "notice_origin_name", nullable = false)
    private String noticeOriginName;     // 공지 원본 파일명

    @Column(name = "notice_file_size", nullable = false)
    private Long noticeFileSize;         // 공지 파일 사이즈

    @Column(name = "notice_file_type", nullable = false)
    private String noticeFileType;       // 공지 파일 타입

    @Column(name = "notice_no", nullable = false)
    private int noticeNo;                // 공지 번호

    protected NoticeFile(){}

    public NoticeFile noticeFileName(String val) {
        this.noticeFileName = val;
        return this;
    }

    public NoticeFile noticeFilePath(String val) {
        this.noticeFilePath = val;
        return this;
    }

    public NoticeFile noticeOriginName(String val) {
        this.noticeOriginName = val;
        return this;
    }

    public NoticeFile noticeFileSize(Long val) {
        this.noticeFileSize = val;
        return this;
    }

    public NoticeFile noticeFileType(String val) {
        this.noticeFileType = val;
        return this;
    }

    public NoticeFile(int noticeFileNo, String noticeFileName, String noticeFilePath, String noticeOriginName, Long noticeFileSize, String noticeFileType, int noticeNo) {
        this.noticeFileNo = noticeFileNo;
        this.noticeFileName = noticeFileName;
        this.noticeFilePath = noticeFilePath;
        this.noticeOriginName = noticeOriginName;
        this.noticeFileSize = noticeFileSize;
        this.noticeFileType = noticeFileType;
        this.noticeNo = noticeNo;
    }

    public String getNoticeFilePath() {
        return noticeFilePath;
    }

}
