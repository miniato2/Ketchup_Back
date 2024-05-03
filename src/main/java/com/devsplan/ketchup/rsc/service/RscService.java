package com.devsplan.ketchup.rsc.service;

import com.devsplan.ketchup.reserve.dto.ResourceDTO;
import com.devsplan.ketchup.rsc.dto.RscDTO;
import com.devsplan.ketchup.rsc.entity.Rsc;
import com.devsplan.ketchup.rsc.repository.RscRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RscService {
    private final RscRepository rscRepository;

    public RscService(RscRepository rscRepository) {
        this.rscRepository = rscRepository;
    }

    @Transactional
    public void insertResource(RscDTO rscDTO) {
        Rsc resource = new Rsc(
                rscDTO.getRscCategory(),
                rscDTO.getRscName(),
                rscDTO.getRscInfo(),
                rscDTO.getRscCap()
        );

        rscRepository.save(resource);
    }
}
