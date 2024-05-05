package com.devsplan.ketchup.reserve.controller;

import com.devsplan.ketchup.reserve.dto.ReserveDTO;
import com.devsplan.ketchup.reserve.dto.ResourceDTO;
import com.devsplan.ketchup.reserve.entity.Resource;
import com.devsplan.ketchup.reserve.repository.ReserveRepository;
import com.devsplan.ketchup.reserve.repository.ResourceRepository;
import com.devsplan.ketchup.reserve.service.ReserveService;
import com.devsplan.ketchup.schedule.dto.DepartmentDTO;
import com.devsplan.ketchup.schedule.dto.ScheduleDTO;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/reserves")
@Slf4j
public class ReserveController {

    private final ReserveRepository reserveRepository;
    private final ReserveService reserveService;
    private final ResourceRepository resourceRepository;
    List<ReserveDTO> reserve;

    public ReserveController(ReserveRepository reserveRepository, ReserveService reserveService, ResourceRepository resourceRepository) {
        this.reserveRepository = reserveRepository;
        this.reserveService = reserveService;
        this.resourceRepository = resourceRepository;
        reserve = new ArrayList<>();

        // LocalDateTime 파싱을 위한 DateTimeFormatter 생성
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd a h시 m분", Locale.KOREAN);

        // 날짜 및 시간 정보를 문자열에서 LocalDateTime으로 변환하여 ScheduleDTO 객체를 생성
        LocalDateTime rsvStartDttm = LocalDateTime.parse("2024-05-04 오후 4시 30분", formatter);
        LocalDateTime rsvEndDttm = LocalDateTime.parse("2024-05-04 오후 7시 30분", formatter);

        reserve.add(new ReserveDTO(6, "ReserveController에서 생성하는 사용 목적", rsvStartDttm, rsvEndDttm, new ResourceDTO(1, "회의실", "회의실 B", "본관 4층 401호", 20, true, "Smart TV, 빔프로젝터, 책상, 의자, 단상 비치, 경복궁 뷰")));
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
    @PostMapping()
    public ResponseEntity<?> insertReserve(@RequestBody ReserveDTO newReserve) {
        log.debug("newReserve: {}", newReserve);

        if (newReserve.getResources() == null) {
            return ResponseEntity.badRequest().body("Resource information is missing.");
        }
        Optional<Resource> resourceOptional = resourceRepository.findById((long) newReserve.getResources().getRscNo());

        if (!resourceOptional.isPresent()) {
            return ResponseEntity.badRequest().body("해당 리소스를 찾을 수 없습니다.");
        }
        Resource resource = resourceOptional.get();

        reserveService.insertReserve(newReserve, resource);

        return ResponseEntity.created(URI.create("/reserves/" + newReserve.getRsvNo())).build();
    }



}
