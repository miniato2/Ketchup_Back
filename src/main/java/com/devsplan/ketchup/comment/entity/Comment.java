package com.devsplan.ketchup.comment.entity;

import com.devsplan.ketchup.board.entity.Board;
import jakarta.persistence.*;
import lombok.Builder;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_comment")
@Builder(toBuilder = true)
public class Comment {

    @Id
    @Column(name = "comment_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int commentNo;                  //댓글 번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_no", nullable = false)
    private Board board;                    // 게시물 번호

    @Column(name = "member_no", nullable = false)
    private String memberNo;                //사번 (댓글 작성자)

    @Column(name = "comment_content", nullable = false)
    private String commentContent;          //댓글 내용

    @Column(name = "comment_create_dttm", nullable = false)
    private Timestamp commentCreateDttm;    //생성 시간

    @Column(name = "comment_update_dttm")
    private Timestamp commentUpdateDttm;    //업데이트 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_no")
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> replies = new ArrayList<>();

    protected Comment() {}

    public Comment(int commentNo, Board board, String memberNo, String commentContent,
                   Timestamp commentCreateDttm, Timestamp commentUpdateDttm,
                   Comment parentComment, List<Comment> replies) {
        this.commentNo = commentNo;
        this.board = board;
        this.memberNo = memberNo;
        this.commentContent = commentContent;
        this.commentCreateDttm = commentCreateDttm;
        this.commentUpdateDttm = commentUpdateDttm;
        this.parentComment = parentComment;
        this.replies = replies;
    }

    public Comment board(Board val) {
        this.board = val;
        return this;
    }

    public Comment parentComment(Comment val) {
        this.parentComment = val;
        return this;
    }

}
