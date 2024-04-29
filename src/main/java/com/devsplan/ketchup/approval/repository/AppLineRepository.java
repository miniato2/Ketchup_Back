package com.devsplan.ketchup.approval.repository;

import com.devsplan.ketchup.approval.entity.AppLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AppLineRepository extends JpaRepository<AppLine, Integer> {

    @Query("select a.approvalNo from AppLine a where a.alMemberNo = :memberNo" )
    List<Integer> findAppNoByMemberNo(int memberNo);
}
