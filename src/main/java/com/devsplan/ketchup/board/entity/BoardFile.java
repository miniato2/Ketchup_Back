package com.devsplan.ketchup.board.entity;

import jakarta.persistence.*;
import lombok.Builder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "board_file")
@Builder(toBuilder = true)
public class BoardFile {

    @Id
    @Column(name = "board_file_No")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardFileNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_no")
    @OnDelete(action = OnDeleteAction.CASCADE)          // 부모 엔티티(게시글)가 삭제될 때 자식 엔티티(이미지)도 함께 삭제
    private Board board;

    @Column(name = "board_file_name", nullable = false)
    private String boardFileName;

    @Column(name = "board_file_path", nullable = false)
    private String boardFilePath;

    @Column(name = "board_origin_name", nullable = false)
    private String boardOriginName;

    @Column(name = "board_file_size", nullable = false)
    private Long boardFileSize;              // 파일 사이즈

    public BoardFile boardFileNo(Long val) {
        this.boardFileNo = val;
        return this;
    }

    public BoardFile board(Board val) {
        this.board = val;
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

    public BoardFile() {}

    public BoardFile(Long boardFileNo, String boardFileName, String boardFilePath, String boardOriginName, Long boardFileSize) {
        this.boardFileNo = boardFileNo;
        this.boardFileName = boardFileName;
        this.boardFilePath = boardFilePath;
        this.boardOriginName = boardOriginName;
        this.boardFileSize = boardFileSize;
    }

    public BoardFile(Long boardFileNo, Board board, String boardFileName, String boardFilePath, String boardOriginName, Long boardFileSize) {
        this.boardFileNo = boardFileNo;
        this.board = board;
        this.boardFileName = boardFileName;
        this.boardFilePath = boardFilePath;
        this.boardOriginName = boardOriginName;
        this.boardFileSize = boardFileSize;
    }

    public Long getBoardFileNo() {
        return boardFileNo;
    }

    public String getBoardFileName() {
        return boardFileName;
    }

    public String getBoardFilePath() {
        return boardFilePath;
    }

    public String getBoardOriginName() {
        return boardOriginName;
    }


}
