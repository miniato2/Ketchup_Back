package com.devsplan.ketchup.reserve.controller;

import com.devsplan.ketchup.reserve.dto.ReserveDTO;
import com.devsplan.ketchup.reserve.repository.ReserveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/reserves")
@Slf4j
public class ReserveController {

    private final ReserveRepository reserveRepository;
    List<ReserveDTO> reserve;

    public ReserveController(ReserveRepository reserveRepository) {
        this.reserveRepository = reserveRepository;
        reserve = new ArrayList<>();
    }

    @PostMapping("/reserves")
    public ResponseEntity<?> insertReserve(@RequestBody ReserveDTO newReserve) {
        System.out.println("newReserve: " + newReserve);

        reserve.add(newReserve);

        return ResponseEntity.created(URI.create("/reserves/reserves")).build();
    }

}
