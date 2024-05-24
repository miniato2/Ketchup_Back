package com.devsplan.ketchup.comment.service;

import com.devsplan.ketchup.board.entity.Board;
import com.devsplan.ketchup.board.repository.BoardRepository;
import com.devsplan.ketchup.comment.dto.CommentDTO;
import com.devsplan.ketchup.comment.entity.Comment;
import com.devsplan.ketchup.comment.repository.CommentRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommentService {
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CommentService(BoardRepository boardRepository, CommentRepository commentRepository, ModelMapper modelMapper) {
        this.boardRepository = boardRepository;
        this.commentRepository = commentRepository;
        this.modelMapper = modelMapper;
    }

    /* 댓글 조회 */
    public List<CommentDTO> selectCommentList(int boardNo) {
        try {
            // 게시물에 해당하는 모든 댓글 가져오기
//            List<Comment> comments = commentRepository.findByBoardNo(boardNo);

            // 댓글 DTO 리스트로 변환
//            return comments.stream()
//                    .map(comment -> modelMapper.map(comment, CommentDTO.class))
//                    .collect(Collectors.toList());
            return null;
        } catch (Exception e) {
            log.error("댓글 조회 중 오류 발생: " + e.getMessage(), e);
            throw new RuntimeException("댓글 조회 중 오류 발생");
        }
    }

    /* 게시물 댓글 등록 */
    @Transactional
    public Object insertComment(int boardNo, CommentDTO commentDTO, String memberNo) {
        try {
            // 해당 게시물 가져오기
            Board board = boardRepository.findById(boardNo).orElseThrow(IllegalArgumentException::new);

            commentDTO.setCommentCreateDttm(new Timestamp(System.currentTimeMillis()));
            commentDTO.setMemberNo(memberNo);


            // 댓글 생성
            Comment comment = modelMapper.map(commentDTO, Comment.class);
            comment.board(board);

            if (commentDTO.getParentCommentNo() != null) {
                Comment parentComment = commentRepository.findById(commentDTO.getParentCommentNo())
                        .orElseThrow(IllegalArgumentException::new);
                comment.parentComment(parentComment);
            }

            Comment savedComment = commentRepository.save(comment);

            return modelMapper.map(savedComment, CommentDTO.class);
        } catch (Exception e) {
            log.error("Failed to add comment to board: " + e.getMessage(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new RuntimeException("Failed to add comment to board");
        }
    }

}
