package com.devsplan.ketchup.reserve.repository;

import com.devsplan.ketchup.reserve.entity.Reserve;
import com.devsplan.ketchup.rsc.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReserveRepository extends JpaRepository<Reserve, Long> {
    List<Reserve> findByResourcesRscCategoryAndRsvStartDttmBetween(String rscCategory, LocalDateTime startDateTime, LocalDateTime endDateTime);

    List<Reserve> findByResources(Resource resources);

    int deleteByRsvNo(int rsvNo);
}
