package com.devsplan.ketchup.board.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "board_file")
public class BoardFile {

    @Id
    @Column(name = "board_file_No")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardFileNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_No", nullable = false)
    private Board board;

    @Column(name = "board_file_name", nullable = false)
    private String boardFileName;

    @Column(name = "board_file_path", nullable = false)
    private String boardFilePath;

    @Column(name = "board_origin_name", nullable = false)
    private String boardOriginName;

    protected BoardFile() {}

    public BoardFile(Long boardFileNo, String boardFileName, String boardFilePath, String boardOriginName) {
        this.boardFileNo = boardFileNo;
        this.boardFileName = boardFileName;
        this.boardFilePath = boardFilePath;
        this.boardOriginName = boardOriginName;
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
