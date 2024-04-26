package com.devsplan.ketchup.board.repository;

import com.devsplan.ketchup.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository <Board, Integer> {
    Page<Board> findByDepartmentNo(int departmentNo, Pageable pageable);
    Page<Board> findByDepartmentNoAndBoardTitleContaining(int departmentNo, Pageable pageable, String title);
}
