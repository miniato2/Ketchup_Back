package com.devsplan.ketchup.member.repository;


import com.devsplan.ketchup.member.entity.Dep;
import com.devsplan.ketchup.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepRepository extends JpaRepository<Dep, Integer> {
  

    Dep findDepByDepNo(int rDepNo);

    List<Dep> findByStatus(char y);
}
