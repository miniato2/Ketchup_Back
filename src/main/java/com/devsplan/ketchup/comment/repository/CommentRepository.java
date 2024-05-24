package com.devsplan.ketchup.comment.repository;

import com.devsplan.ketchup.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

//    List<Comment> findByBoardNo(int boardNo);

}
