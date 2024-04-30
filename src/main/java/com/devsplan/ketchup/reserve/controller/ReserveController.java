package com.devsplan.ketchup.reserve.controller;

import com.devsplan.ketchup.reserve.dto.ReserveDTO;
import com.devsplan.ketchup.reserve.repository.ReserveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/reserve")
@Slf4j
public class ReserveController {

    private final ReserveRepository reserveRepository;
    List<ReserveDTO> reserve;

    public ReserveController(ReserveRepository reserveRepository) {
        this.reserveRepository = reserveRepository;
        reserve = new ArrayList<>();
    }

}
