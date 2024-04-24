package com.devsplan.ketchup.approval.repository;

import com.devsplan.ketchup.approval.entity.AppLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppLineRepository extends JpaRepository<AppLine, Integer> {
}
