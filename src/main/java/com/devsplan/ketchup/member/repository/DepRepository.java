package com.devsplan.ketchup.member.repository;


import com.devsplan.ketchup.member.entity.Dep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepRepository extends JpaRepository<Dep, Integer> {
}
