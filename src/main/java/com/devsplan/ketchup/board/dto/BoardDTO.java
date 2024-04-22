package com.devsplan.ketchup.board.dto;

import java.time.LocalDateTime;

public class BoardDTO {

    private int boardNo;    // 게시물 번호
    private int memberNo;   // 사번
    private int departmentNo;   // 부서번호
    private String boardTitle;  // 게시물 제목
    private int boardFileNo;    // 게시물파일 번호
    private String boardfileUrl;    // 파일 업로드 url
    private String boardContent;    // 게시물 내용
    private LocalDateTime boardCreateDttm;    // 게시글 등록일시

    protected LocalDateTime boardUpdateDttm;    // 게시글 수정일시

    public BoardDTO() {}

    public BoardDTO(int boardNo,int departmentNo, int memberNo, String boardTitle, String boardContent, LocalDateTime boardCreateDttm) {
        this.boardNo = boardNo;
        this.departmentNo = departmentNo;
        this.memberNo = memberNo;
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.boardCreateDttm = boardCreateDttm;
    }

    public BoardDTO(int boardNo, int departmentNo, int memberNo, String boardTitle, int boardFileNo, String boardContent, LocalDateTime boardCreateDttm) {
        this.boardNo = boardNo;
        this.departmentNo = departmentNo;
        this.memberNo = memberNo;
        this.boardTitle = boardTitle;
        this.boardFileNo = boardFileNo;
        this.boardContent = boardContent;
        this.boardCreateDttm = boardCreateDttm;
    }

    public int getBoardNo() {
        return boardNo;
    }

    public int getDepartmentNo() {
        return departmentNo;
    }
    public int getMemberNo() {
        return memberNo;
    }

    public String getBoardTitle() {
        return boardTitle;
    }

    public int getBoardFileNo() {
        return boardFileNo;
    }

    public String getBoardfileUrl() {
        return boardfileUrl;
    }

    public String getBoardContent() {
        return boardContent;
    }

    public LocalDateTime getBoardCreateDttm() {
        return boardCreateDttm;
    }

    public void setBoardNo(int boardNo) {
        this.boardNo = boardNo;
    }

    public void setMemberNo(int memberNo) {
        this.memberNo = memberNo;
    }

    public void setDepartmentNo(int departmentNo) {
        this.departmentNo = departmentNo;
    }

    public void setBoardTitle(String boardTitle) {
        this.boardTitle = boardTitle;
    }

    public void setBoardFileNo(int boardFileNo) {
        this.boardFileNo = boardFileNo;
    }

    public void setBoardfileUrl(String boardfileUrl) {
        this.boardfileUrl = boardfileUrl;
    }

    public void setBoardContent(String boardContent) {
        this.boardContent = boardContent;
    }

    public void setBoardCreateDttm(LocalDateTime boardCreateDttm) {
        this.boardCreateDttm = boardCreateDttm;
    }
}
