package com.devsplan.ketchup.board.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@Setter
@Getter
@ToString
public class BoardFileDTO {
    private int boardNo;                // 게시물 번호
    private String boardFileImgUrl;
    private int boardFileNo;            // 게시물 파일 번호
//    private String boardFileName;       // 게시물 파일명
//    private String boardFilePath;       // 게시물 파일 경로
//    private String boardOriginName;       // 게시물 원본 파일명
//    private Long boardFileSize;         // 파일 사이즈
//    private String fileType;            // 파일 타입


    public BoardFileDTO(int boardNo, String boardFileImgUrl) {
        this.boardNo = boardNo;
        this.boardFileImgUrl = boardFileImgUrl;
    }

    public BoardFileDTO(int boardNo, String boardFileImgUrl, int boardFileNo) {
        this.boardNo = boardNo;
        this.boardFileImgUrl = boardFileImgUrl;
        this.boardFileNo = boardFileNo;
    }
}
