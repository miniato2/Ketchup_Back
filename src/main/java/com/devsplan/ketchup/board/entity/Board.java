package com.devsplan.ketchup.board.entity;

import com.devsplan.ketchup.member.DepartmentBoard;
import com.devsplan.ketchup.member.Member;
import jakarta.persistence.*;
import lombok.Builder;
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

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<BoardFile> boardFiles;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberNo")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departmentNo")
    private DepartmentBoard departmentBoard;

    @Column(name = "board_create_dttm", nullable = false)
//    @CreatedDate
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

    public Board boardFiles(List<BoardFile> val) {
        this.boardFiles = val;
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

    public Board(int boardNo, String boardTitle, List<BoardFile> boardFiles, String boardContent, LocalDateTime boardCreateDttm) {
        this.boardNo = boardNo;
        this.boardTitle = boardTitle;
        this.boardFiles = boardFiles;
        this.boardContent = boardContent;
        this.boardCreateDttm = boardCreateDttm;
    }

    public int getBoardNo() {
        return boardNo;
    }

    public String getBoardTitle() {
        return boardTitle;
    }

    public List<BoardFile> getBoardFiles() {
        return boardFiles;
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

    public Board(int boardNo, String boardTitle, String boardContent, List<BoardFile> boardFiles, Member member, DepartmentBoard departmentBoard, LocalDateTime boardCreateDttm, LocalDateTime boardUpdateDttm) {
        this.boardNo = boardNo;
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.boardFiles = boardFiles;
        this.member = member;
        this.departmentBoard = departmentBoard;
        this.boardCreateDttm = boardCreateDttm;
        this.boardUpdateDttm = boardUpdateDttm;
    }
}
