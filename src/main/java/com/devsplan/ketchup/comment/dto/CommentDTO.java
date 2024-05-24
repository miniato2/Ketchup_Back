package com.devsplan.ketchup.comment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@ToString
public class CommentDTO {

    private int commentNo;                  //댓글 번호
    private int boardNo;                    //게시물번호
    private String memberNo;                //사번 (댓글 작성자)
    private String commentContent;          //댓글 내용
    private Timestamp commentCreateDttm;    //생성 시간
    private Timestamp commentUpdateDttm;    //업데이트 시간
    private Integer parentCommentNo;        // 부모 댓글 번호 (null이면 최상위 댓글)
    private String parentMemberNo;          // 부모 댓글의 작성자 사번
    private List<CommentDTO> replies;       // 답글 리스트

    public void setCommentCreateDttm(Timestamp timestamp) {
        this.commentCreateDttm = timestamp;
    }

    public void setCommentUpdateDttm(Timestamp timestamp) {
        this.commentUpdateDttm = timestamp;
    }

}
