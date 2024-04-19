package com.devsplan.ketchup.board.dto;

import java.time.LocalDateTime;

public class BoardDTO {

    private int boardNo;
    private String boardTitle;
    private int boardFileNo;
    private String boardContent;
    private LocalDateTime boardDttm;

    public BoardDTO() {
    }

    public BoardDTO(int boardNo, String boardTitle, int boardFileNo, String boardContent, LocalDateTime boardDttm) {
        this.boardNo = boardNo;
        this.boardTitle = boardTitle;
        this.boardFileNo = boardFileNo;
        this.boardContent = boardContent;
        this.boardDttm = boardDttm;
    }

    public int getBoardNo() {
        return boardNo;
    }

    public void setBoardNo(int boardNo) {
        this.boardNo = boardNo;
    }

    public String getBoardTitle() {
        return boardTitle;
    }

    public void setBoardTitle(String boardTitle) {
        this.boardTitle = boardTitle;
    }

    public int getBoardFileNo() {
        return boardFileNo;
    }

    public void setBoardFileNo(int boardFileNo) {
        this.boardFileNo = boardFileNo;
    }

    public String getBoardContent() {
        return boardContent;
    }

    public void setBoardContent(String boardContent) {
        this.boardContent = boardContent;
    }

    public LocalDateTime getBoardDttm() {
        return boardDttm;
    }

    public void setBoardDttm(LocalDateTime boardDttm) {
        this.boardDttm = boardDttm;
    }

    @Override
    public String toString() {
        return "BoardDTO{" +
                "boardNo=" + boardNo +
                ", boardTitle='" + boardTitle + '\'' +
                ", boardFileNo=" + boardFileNo +
                ", boardContent='" + boardContent + '\'' +
                ", boardDttm=" + boardDttm +
                '}';
    }
}
