package com.devsplan.ketchup.board.entity;

import jakarta.persistence.*;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Entity
@Table(name = "board")
public class Board {

    @Id
    @Column(name = "board_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int boardNo;

    @Column(name = "board_title", nullable = false)
    private String boardTitle;

    @Column(name = "board_file_no")
    private int boardFileNo;

    @Column(name = "board_content", nullable = false)
    private String boardContent;

    @Column(name = "board_dttm", nullable = false)
    private LocalDateTime boardDttm;

    protected Board() {}

    public Board(int boardNo, String boardTitle, String boardContent, LocalDateTime boardDttm) {
        this.boardNo = boardNo;
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.boardDttm = boardDttm;
    }

    public Board(int boardNo, String boardTitle, int boardFileNo, String boardContent, LocalDateTime boardDttm) {
        this.boardNo = boardNo;
        this.boardTitle = boardTitle;
        this.boardFileNo = boardFileNo;
        this.boardContent = boardContent;
        this.boardDttm = boardDttm;
    }

    public int getBoardNo() {
        return boardNo;
    }

    public String getBoardTitle() {
        return boardTitle;
    }

    public int getBoardFileNo() {
        return boardFileNo;
    }

    public String getBoardContent() {
        return boardContent;
    }

    public LocalDateTime getBoardDttm() {
        return boardDttm;
    }
}
