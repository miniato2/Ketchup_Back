package com.devsplan.ketchup.board.entity;

import jakarta.persistence.*;
import lombok.Builder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tbl_board")
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

    @Column(name = "member_no")
    private int memberNo;

    @Column(name = "department_no")
    private int departmentNo;

    @Column(name = "board_create_dttm", nullable = false)
    @CreatedDate
    private Timestamp boardCreateDttm;

    @Column(name = "board_update_dttm")
    @LastModifiedDate
    protected Timestamp boardUpdateDttm;

    public Board boardTitle(String val) {
        this.boardTitle = val;
        return this;
    }
    public Board boardContent(String val) {
        this.boardContent = val;
        return this;
    }

//    public Board boardFileNo(int val) {
//        this.boardFileNo = val;
//        return this;
//    }

    public Board boardCreateDttm(Timestamp val) {
        this.boardCreateDttm = val;
        return this;
    }

    public Board boardUpdateDttm(Timestamp val) {
        this.boardUpdateDttm = val;
        return this;
    }
//    public Board boardFiles(List<BoardFile> val) {
//        this.boardFiles = val;
//        return this;
//    }

    public Board(int boardNo, String boardTitle, String boardContent, int memberNo, int departmentNo, Timestamp boardCreateDttm, Timestamp boardUpdateDttm/*, List<BoardFile> boardFiles*/) {
        this.boardNo = boardNo;
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.memberNo = memberNo;
        this.departmentNo = departmentNo;
        this.boardCreateDttm = boardCreateDttm;
        this.boardUpdateDttm = boardUpdateDttm;
//        this.boardFiles = boardFiles;
    }

    protected Board() {}

    public int getBoardNo() {
        return boardNo;
    }

    public String getBoardTitle() {
        return boardTitle;
    }

//    public int getBoardFileNo() {
//        return boardFileNo;
//    }

    public String getBoardContent() {
        return boardContent;
    }

    public Timestamp getBoardCreateDttm() {
        return boardCreateDttm;
    }

    public int getMemberNo() {
        return memberNo;
    }

    public int getDepartmentNo() {
        return departmentNo;
    }

//    public List<BoardFile> getBoardFiles() {
//        return boardFiles;
//    }

    public Timestamp getBoardUpdateDttm() {
        return boardUpdateDttm;
    }

    @Override
    public String toString() {
        return "Board{" +
                "boardNo=" + boardNo +
                ", boardTitle='" + boardTitle + '\'' +
                ", boardContent='" + boardContent + '\'' +
//                ", boardFileNo=" + boardFileNo +
                ", memberNo=" + memberNo +
                ", departmentNo=" + departmentNo +
                ", boardCreateDttm=" + boardCreateDttm +
                ", boardUpdateDttm=" + boardUpdateDttm +
//                ", boardFiles=" + boardFiles +
                '}';
    }



}
