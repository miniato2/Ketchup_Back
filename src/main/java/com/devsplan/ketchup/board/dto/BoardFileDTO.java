package com.devsplan.ketchup.board.dto;

public class BoardFileDTO {

    private Long boardFileNo;
    private String boardFileName;
    private String boardFilePath;
    private String boardOrigName;

    public BoardFileDTO() {
    }

    public BoardFileDTO(Long boardFileNo, String boardFileName, String boardFilePath, String boardOrigName) {
        this.boardFileNo = boardFileNo;
        this.boardFileName = boardFileName;
        this.boardFilePath = boardFilePath;
        this.boardOrigName = boardOrigName;
    }

    public Long getBoardFileNo() {
        return boardFileNo;
    }

    public void setBoardFileNo(Long boardFileNo) {
        this.boardFileNo = boardFileNo;
    }

    public String getBoardFileName() {
        return boardFileName;
    }

    public void setBoardFileName(String boardFileName) {
        this.boardFileName = boardFileName;
    }

    public String getBoardFilePath() {
        return boardFilePath;
    }

    public void setBoardFilePath(String boardFilePath) {
        this.boardFilePath = boardFilePath;
    }

    public String getBoardOrigName() {
        return boardOrigName;
    }

    public void setBoardOrigName(String boardOrigName) {
        this.boardOrigName = boardOrigName;
    }



}
