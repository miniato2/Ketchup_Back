package com.devsplan.ketchup.rsc.repository;

import com.devsplan.ketchup.rsc.entity.Rsc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RscRepository extends JpaRepository<Rsc, Integer> {

}
