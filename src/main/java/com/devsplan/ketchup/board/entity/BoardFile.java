package com.devsplan.ketchup.board.entity;

import jakarta.persistence.*;
import lombok.Builder;

@Entity
@Table(name = "tbl_board_file")
@Builder(toBuilder = true)
public class BoardFile {

    @Id
    @Column(name = "board_file_No")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int boardFileNo;

    @Column(name = "board_file_name", nullable = false)
    private String boardFileName;

    @Column(name = "board_file_path", nullable = false)
    private String boardFilePath;

    @Column(name = "board_origin_name", nullable = false)
    private String boardOriginName;

    @Column(name = "board_file_size", nullable = false)
    private Long boardFileSize;              // 파일 사이즈

    @Column(name = "file_type", nullable = false)
    private String fileType;            // 파일 타입

//    @OnDelete(action = OnDeleteAction.CASCADE)
    @Column(name = "board_no", nullable = false) // 여기에 추가
    private int boardNo;

//    @OneToMany(mappedBy = "board",cascade = CascadeType.ALL, orphanRemoval = true)
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    private List<BoardFile> boardFiles;


    public BoardFile boardFileNo(int val) {
        this.boardFileNo = val;
        return this;
    }

    public BoardFile boardNo(int val) {
        this.boardNo = val;
        return this;
    }

    public BoardFile boardFileName(String val) {
        this.boardFileName = val;
        return this;
    }

    public BoardFile boardFilePath(String val) {
        this.boardFilePath = val;
        return this;
    }

    public BoardFile boardOriginName(String val) {
        this.boardOriginName = val;
        return this;
    }

    public BoardFile boardFileSize(Long val) {
        this.boardFileSize = val;
        return this;
    }

    public BoardFile fileType(String val) {
        this.fileType = val;
        return this;
    }

    protected BoardFile() {}

    public BoardFile(int boardFileNo, String boardFileName, String boardFilePath, String boardOriginName, Long boardFileSize, String fileType, int boardNo) {
        this.boardFileNo = boardFileNo;
        this.boardFileName = boardFileName;
        this.boardFilePath = boardFilePath;
        this.boardOriginName = boardOriginName;
        this.boardFileSize = boardFileSize;
        this.fileType = fileType;
        this.boardNo = boardNo;
    }

    public String getBoardFilePath() {
        return boardFilePath;
    }
    public String getBoardFileName() {
        return boardFileName;
    }

    @Override
    public String toString() {
        return "BoardFile{" +
                "boardFileNo=" + boardFileNo +
                ", boardFileName='" + boardFileName + '\'' +
                ", boardFilePath='" + boardFilePath + '\'' +
                ", boardOriginName='" + boardOriginName + '\'' +
                ", boardFileSize=" + boardFileSize +
                ", fileType='" + fileType + '\'' +
                ", boardNo=" + boardNo +
                '}';
    }
}
