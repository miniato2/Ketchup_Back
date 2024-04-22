package com.devsplan.ketchup.schedule.repository;

import com.devsplan.ketchup.schedule.entity.Schedule;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {

//    @PersistenceContext
//    private EntityManager manager;

}
