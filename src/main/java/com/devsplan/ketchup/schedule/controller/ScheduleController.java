package com.devsplan.ketchup.schedule.controller;

import com.devsplan.ketchup.schedule.dto.DepartmentDTO;
import com.devsplan.ketchup.schedule.dto.ScheduleDTO;
import com.devsplan.ketchup.schedule.service.ScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/schedules")
@Slf4j
public class ScheduleController {
    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    // 부서별 일정 목록 조회
    @GetMapping("/department/{dptNo}")
    public ResponseEntity<ResponseMessage> selectScheduleList(@PathVariable int dptNo) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        List<ScheduleDTO> foundSchedules = scheduleService.selectScheduleListByDepartment(dptNo);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("schedule", foundSchedules);

        return ResponseEntity.ok().headers(headers).body(new ResponseMessage(200, "조회 성공", responseMap));
    }


    // 부서별 일정 상세 조회
    @GetMapping("/department/{dptNo}/schedules/{skdNo}")
    public ResponseEntity<ResponseMessage> selectScheduleDetail(@PathVariable int dptNo, @PathVariable int skdNo) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        ScheduleDTO foundSchedule = scheduleService.selectScheduleDetail(dptNo, skdNo);

        if (foundSchedule != null) {
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("schedule", foundSchedule);
            return ResponseEntity.ok().headers(headers)
                    .body(new ResponseMessage(200, "조회 성공", responseMap));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseMessage(404, "일정을 찾을 수 없습니다.", null));
        }
    }

    // 부서별 일정 등록
    @PostMapping("/schedules")
    public ResponseEntity<?> insertSchedule(@RequestBody ScheduleDTO newSchedule) {
        System.out.println("newSchedule 🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥 = " + newSchedule);
        scheduleService.insertSchedule(newSchedule);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 부서별 일정 수정
    @PutMapping("/schedules/{skdNo}")
    public ResponseEntity<?> updateSchedule(@PathVariable int skdNo, @RequestBody ScheduleDTO updateSchedule) {
        try {
            scheduleService.updateSchedule(skdNo, updateSchedule);
            String uri = "/schedules/" + skdNo;
            return ResponseEntity.ok().header(HttpHeaders.LOCATION, uri).body("일정이 성공적으로 수정되었습니다. URI: " + uri);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 부서별 일정 삭제
    @DeleteMapping("/schedules/{skdNo}")
    public ResponseEntity<?> deleteSchedule(@PathVariable int skdNo) {
        try {
            scheduleService.deleteById(skdNo);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}