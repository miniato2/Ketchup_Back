package com.devsplan.ketchup.board.repository;

import com.devsplan.ketchup.board.entity.Board;
import com.devsplan.ketchup.board.entity.BoardFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardFileRepository extends JpaRepository <BoardFile, Integer> {
    List<BoardFile> findByBoardNo(int boardNo);
}
