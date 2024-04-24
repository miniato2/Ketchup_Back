package com.devsplan.ketchup.board.entity;

import jakarta.persistence.*;
import lombok.Builder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "board")
@Builder(toBuilder = true)
public class Board {

    @Id
    @Column(name = "board_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int boardNo;

    @Column(name = "board_title", nullable = false,  length = 150)
    private String boardTitle;

    @Column(name = "board_content", nullable = false)
    private String boardContent;

    @Column(name = "board_file_no")
    private int boardFileNo;

    @Column(name = "member_no")
    private int memberNo;

    @Column(name = "department_no")
    private int departmentNo;

    @Column(name = "board_create_dttm", nullable = false)
    @CreatedDate
    private LocalDateTime boardCreateDttm;

    @Column(name = "board_update_dttm")
    @LastModifiedDate
    protected LocalDateTime boardUpdateDttm;

    public Board boardTitle(String val) {
        this.boardTitle = val;
        return this;
    }
    public Board boardContent(String val) {
        this.boardContent = val;
        return this;
    }

    public Board boardFileNo(int val) {
        this.boardFileNo = val;
        return this;
    }

    public Board boardCreateDttm(LocalDateTime val) {
        this.boardCreateDttm = val;
        return this;
    }

    public Board boardUpdateDttm(LocalDateTime val) {
        this.boardUpdateDttm = val;
        return this;
    }

    protected Board() {}

    public Board(int boardNo, String boardTitle, String boardContent, LocalDateTime boardCreateDttm) {
        this.boardNo = boardNo;
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.boardCreateDttm = boardCreateDttm;
    }

    public Board(int boardNo, String boardTitle, int boardFileNo, String boardContent, LocalDateTime boardCreateDttm) {
        this.boardNo = boardNo;
        this.boardTitle = boardTitle;
        this.boardFileNo = boardFileNo;
        this.boardContent = boardContent;
        this.boardCreateDttm = boardCreateDttm;
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

    public LocalDateTime getBoardCreateDttm() {
        return boardCreateDttm;
    }

    public LocalDateTime getBoardUpdateDttm() {
        return boardUpdateDttm;
    }

    public Board(int boardNo, String boardTitle, String boardContent, int boardFileNo, int memberNo, int departmentNo, LocalDateTime boardCreateDttm, LocalDateTime boardUpdateDttm) {
        this.boardNo = boardNo;
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.boardFileNo = boardFileNo;
        this.memberNo = memberNo;
        this.departmentNo = departmentNo;
        this.boardCreateDttm = boardCreateDttm;
        this.boardUpdateDttm = boardUpdateDttm;
    }
}
