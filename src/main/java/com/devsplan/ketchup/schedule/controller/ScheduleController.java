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
    private List<ScheduleDTO> schedule;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
        schedule = new ArrayList<>();

        // LocalDateTime 파싱을 위한 DateTimeFormatter 생성
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd a h시 m분", Locale.KOREAN);

        // 날짜 및 시간 정보를 문자열에서 LocalDateTime으로 변환하여 ScheduleDTO 객체를 생성
        LocalDateTime startDateTime = LocalDateTime.parse("2024-04-22 오후 4시 30분", formatter);
        LocalDateTime endDateTime = LocalDateTime.parse("2024-04-22 오후 7시 30분", formatter);

        schedule.add(new ScheduleDTO(1, new DepartmentDTO(1), "해외바이어 미팅", startDateTime, endDateTime, "코스트코 광명점 4층 ㅂㅈㅎ 부장실", "Q4 제안서, 제안 샘플 20종 지참"));
    }

    // 부서별 일정 목록 조회
    @GetMapping("/department/{dptNo}")
    public ResponseEntity<ResponseMessage> selectScheduleList(@PathVariable int dptNo) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        List<ScheduleDTO> foundSchedules = new ArrayList<>();
        for (ScheduleDTO scheduleDTO : schedule) {
            if (scheduleDTO.getDptNo().getDptNo() == dptNo) {   // scheduleDTO.getDptNo().getDptNo()에서 첫번째 getDptNo()는 ScheduleDTO에 있는 dptNo에 접근. 두번째 getDptNo()는 DepartmentDTO에 접근해서 부서 번호를 가져옴.
                foundSchedules.add(scheduleDTO);
            }
        }

        if (!foundSchedules.isEmpty()) {
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("schedule", foundSchedules);
            return ResponseEntity.ok().headers(headers).body(new ResponseMessage(200, "조회 성공", responseMap));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(404, "페이지를 찾을 수 없습니다.", null));
        }
    }

    // 부서별 일정 상세 조회
    @GetMapping("/department/{dptNo}/schedules/{skdNo}")
    public ResponseEntity<ResponseMessage> selectScheduleDetail(@PathVariable int dptNo, @PathVariable int skdNo) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        ScheduleDTO foundSchedule = null;
        for (int i = 0; i < schedule.size(); i++) {
            ScheduleDTO scheduleDTO = schedule.get(i);
            if (scheduleDTO.getDptNo().getDptNo() == dptNo && scheduleDTO.getSkdNo() == skdNo) {
                foundSchedule = scheduleDTO;
                break;
            }
        }

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
        System.out.println("newSchedule = " + newSchedule);

        int lastSkdNo = schedule.get(schedule.size() - 1).getSkdNo();
        newSchedule.setSkdNo(lastSkdNo + 1);

        schedule.add(newSchedule);

        return ResponseEntity.created(URI.create("/schedules/" + schedule.get(schedule.size() - 1)
                             .getSkdNo()))
                             .build();
    }

    // 부서별 일정 수정
    @PutMapping("/schedules/{skdNo}")
    public ResponseEntity<?> modifySchedule(@PathVariable int skdNo, @RequestBody ScheduleDTO modifySchedule) {
        ScheduleDTO foundSchedule = schedule.stream()
                                            .filter(schedule -> schedule.getSkdNo() == skdNo)
                                            .findFirst()    // 첫번째 요소를 반환하거나, 값이 없는 경우 Optional.empty()를 반환함. 궁극적으로 NullPointException 방지해줌.
                                            .orElse(null);

        if (foundSchedule != null) {
            foundSchedule.setSkdName(modifySchedule.getSkdName());
            foundSchedule.setSkdStartDttm(modifySchedule.getSkdStartDttm());
            foundSchedule.setSkdEndDttm(modifySchedule.getSkdEndDttm());
            foundSchedule.setSkdLocation(modifySchedule.getSkdLocation());
            foundSchedule.setSkdMemo(modifySchedule.getSkdMemo());

            String uri = "/schedules/" + skdNo;
            return ResponseEntity.ok().header(HttpHeaders.LOCATION, uri).body("일정이 성공적으로 수정되었습니다. URI: " + uri);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 부서별 일정 삭제
    @DeleteMapping("/schedules/{skdNo}")
    public ResponseEntity<?> deleteSchedule(@PathVariable int skdNo) {
        Optional<ScheduleDTO> foundScheduleOptional = schedule.stream() // 값의 존재여부가 불확실할때 사용하는 래퍼 클래스. NullPointException 방지할 수 있음.
                                                       .filter(schedule -> schedule.getSkdNo() == skdNo)
                                                       .findFirst();
        if (foundScheduleOptional.isPresent()) {
            ScheduleDTO foundSchedule = foundScheduleOptional.get();
            schedule.remove(foundSchedule);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}