package com.devsplan.ketchup.board.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class BoardFileDTO {
    private int boardNo;                // 게시물 번호
    private int boardFileNo;            // 게시물 파일 번호
    private String boardFileName;       // 게시물 파일명
    private String boardFilePath;       // 게시물 파일 경로
    private String boardOriginName;       // 게시물 원본 파일명
    private Long boardFileSize;         // 파일 사이즈
    private String fileType;            // 파일 타입

//
//    public BoardFileDTO() {
//    }
//
//    public BoardFileDTO(Long boardFileNo, String boardFileName, String boardFilePath, String boardOrigName) {
//        this.boardFileNo = boardFileNo;
//        this.boardFileName = boardFileName;
//        this.boardFilePath = boardFilePath;
//        this.boardOrigName = boardOrigName;
//    }
//
//    public Long getBoardFileNo() {
//        return boardFileNo;
//    }
//
//    public void setBoardFileNo(Long boardFileNo) {
//        this.boardFileNo = boardFileNo;
//    }
//
//    public String getBoardFileName() {
//        return boardFileName;
//    }
//
//    public void setBoardFileName(String boardFileName) {
//        this.boardFileName = boardFileName;
//    }
//
//    public String getBoardFilePath() {
//        return boardFilePath;
//    }
//
//    public void setBoardFilePath(String boardFilePath) {
//        this.boardFilePath = boardFilePath;
//    }
//
//    public String getBoardOrigName() {
//        return boardOrigName;
//    }
//
//    public void setBoardOrigName(String boardOrigName) {
//        this.boardOrigName = boardOrigName;
//    }



}
