package com.devsplan.ketchup.reserve.controller;

import com.devsplan.ketchup.reserve.dto.ReserveDTO;
import com.devsplan.ketchup.reserve.repository.ReserveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    //     자원 예약 목록 조회
    @GetMapping
    public ResponseEntity<ResponseMessage> selectReserveList(@RequestParam("category") String rscCategory,
                                                             @RequestParam("rsvDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate rsvDate) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        List<ReserveDTO> foundReserve = new ArrayList<>();
        for (ReserveDTO reserveDTO : reserve) {
            if (reserveDTO.getResources().getRscCategory().equals(rscCategory) &&
                    reserveDTO.getRsvStartDttm().toLocalDate().isEqual(rsvDate)) {
                foundReserve.add(reserveDTO);
            }
        }

        if (!foundReserve.isEmpty()) {
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("reserve", foundReserve);
            return ResponseEntity.ok().headers(headers).body(new ResponseMessage(200, "조회 성공", responseMap));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(404, "검색 조건과 일치하는 예약건을 찾을 수 없습니다.", null));
        }
    }


    // 자원 예약 등록
    @PostMapping("/reserves")
    public ResponseEntity<?> insertReserve(@RequestBody ReserveDTO newReserve) {
        System.out.println("newReserve: " + newReserve);

        reserve.add(newReserve);

        return ResponseEntity.created(URI.create("/reserves/reserves")).build();
    }


}
