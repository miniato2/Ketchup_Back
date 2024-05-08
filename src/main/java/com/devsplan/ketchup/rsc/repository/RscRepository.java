package com.devsplan.ketchup.rsc.repository;

import com.devsplan.ketchup.rsc.entity.Rsc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RscRepository extends JpaRepository<Rsc, Integer> {
    List<Rsc> findByRscCategory(String part);
    Rsc findByRscNo(int rscNo);
    int deleteByRscNo(int rscNo);
}
