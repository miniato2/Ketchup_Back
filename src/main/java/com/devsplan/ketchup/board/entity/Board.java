package com.devsplan.ketchup.board.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "board")
public class Board {

    @Id
    @Column(name = "board_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int boardNo;

//    @Column(name = "member_no", nullable = false)
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "memberNo")
//    private Member member;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "departmentNo")
//    private Department department;


    @Column(name = "board_title", nullable = false,  length = 150)
    private String boardTitle;

//    @Column(name = "board_file_no")
//@JoinColumn(name = "boardFileNo")
    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<BoardFile> boardFiles;

    @Column(name = "board_content", nullable = false)
    private String boardContent;

    @Column(name = "board_create_dttm", nullable = false)
    @CreatedDate
    private LocalDateTime boardCreateDttm;

    @Column(name = "board_update_dttm")
    @LastModifiedDate
    protected LocalDateTime boardUpdateDttm;

    protected Board() {}

    public Board(int boardNo, String boardTitle, String boardContent, LocalDateTime boardCreateDttm) {
        this.boardNo = boardNo;
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.boardCreateDttm = boardCreateDttm;
    }

    public Board(int boardNo, String boardTitle, List<BoardFile> boardFiles, String boardContent, LocalDateTime boardDttm) {
        this.boardNo = boardNo;
        this.boardTitle = boardTitle;
        this.boardFiles = boardFiles;
        this.boardContent = boardContent;
        this.boardCreateDttm = boardDttm;
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

}
