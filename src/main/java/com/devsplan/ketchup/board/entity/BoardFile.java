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

    @Column(name = "board_file_imgurl")
    private String boardFileImgUrl;

    @Column(name = "board_no", nullable = false) // 여기에 추가
    private int boardNo;

    public BoardFile boardFileNo(int val) {
        this.boardFileNo = val;
        return this;
    }

    public BoardFile boardFileImgUrl(String val) {
        this.boardFileImgUrl = val;
        return this;
    }

    public BoardFile boardNo(int val) {
        this.boardNo = val;
        return this;
    }

    protected BoardFile() {}

    public BoardFile(int boardFileNo, String boardFileImgUrl, int boardNo) {
        this.boardFileNo = boardFileNo;
        this.boardFileImgUrl = boardFileImgUrl;
        this.boardNo = boardNo;
    }


    public String getBoardFileImgUrl() {
        return boardFileImgUrl;
    }
}
