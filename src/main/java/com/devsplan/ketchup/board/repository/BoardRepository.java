package com.devsplan.ketchup.board.repository;

import com.devsplan.ketchup.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository <Board, Integer> {

//    Page<Board> findByDepartmentNoAndTitleContaining(int departmentNo, String title, Pageable pageable);

//    Page<Board> findByDepartmentNo(int departmentNo, Pageable pageable);

//    List<Board> findByBoardNameContaining(String title);
}
