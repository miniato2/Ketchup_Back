package com.devsplan.ketchup.schedule.service;

import com.devsplan.ketchup.schedule.dto.DepartmentDTO;
import com.devsplan.ketchup.schedule.dto.ScheduleDTO;
import com.devsplan.ketchup.schedule.entity.Department;
import com.devsplan.ketchup.schedule.entity.Schedule;
import com.devsplan.ketchup.schedule.repository.DepartmentRepository;
import com.devsplan.ketchup.schedule.repository.ScheduleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final DepartmentRepository departmentRepository;

    public ScheduleService(ScheduleRepository scheduleRepository, DepartmentRepository departmentRepository) {
        this.scheduleRepository = scheduleRepository;
        this.departmentRepository = departmentRepository;
    }

      public List<ScheduleDTO> selectScheduleListByDepartment(int dptNo) {
        Department department = departmentRepository.findById(dptNo)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 부서를 찾을 수 없습니다: " + dptNo));
        List<Schedule> scheduleList = scheduleRepository.findByDepartment(department);
        return scheduleList.stream()
                .map(schedule -> {
                    ScheduleDTO scheduleDTO = new ScheduleDTO();
                    scheduleDTO.setSkdNo(schedule.getSkdNo());
                    scheduleDTO.setDptNo(new DepartmentDTO(schedule.getDepartment().getDptNo()));
                    scheduleDTO.setSkdName(schedule.getSkdName());
                    scheduleDTO.setSkdStartDttm(schedule.getSkdStartDttm());
                    scheduleDTO.setSkdEndDttm(schedule.getSkdEndDttm());
                    scheduleDTO.setSkdLocation(schedule.getSkdLocation());
                    scheduleDTO.setSkdMemo(schedule.getSkdMemo());
                    return scheduleDTO;
                }).collect(Collectors.toList());
    }

    public ScheduleDTO selectScheduleDetail(int dptNo, int skdNo) {
        Department department = departmentRepository.findById(dptNo)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 부서를 찾을 수 없습니다: " + dptNo));

        Schedule scheduleDetail = scheduleRepository.findByDepartmentAndSkdNo(department, skdNo);

        if (scheduleDetail != null) {
            ScheduleDTO scheduleDTO = new ScheduleDTO();
            scheduleDTO.setSkdNo(scheduleDetail.getSkdNo());
            scheduleDTO.setDptNo(new DepartmentDTO(scheduleDetail.getDepartment().getDptNo()));
            scheduleDTO.setSkdName(scheduleDetail.getSkdName());
            scheduleDTO.setSkdStartDttm(scheduleDetail.getSkdStartDttm());
            scheduleDTO.setSkdEndDttm(scheduleDetail.getSkdEndDttm());
            scheduleDTO.setSkdLocation(scheduleDetail.getSkdLocation());
            scheduleDTO.setSkdMemo(scheduleDetail.getSkdMemo());
            return scheduleDTO;
        } else {
            return null;
        }
    }

    @Transactional
    public void insertSchedule(ScheduleDTO newSchedule) {

        System.out.println("newSchedule 서비스 = " + newSchedule);

        DepartmentDTO departmentDTO = newSchedule.getDptNo();
        Department department = new Department(departmentDTO.getDptNo());

        // 부서 정보 먼저 저장
        departmentRepository.save(department);

        // 부서 정보 먼저 저장 후 일정 정보 저장
        Schedule schedule = new Schedule(
                newSchedule.getSkdNo(),
                department,
                newSchedule.getSkdName(),
                newSchedule.getSkdStartDttm(),
                newSchedule.getSkdEndDttm(),
                newSchedule.getSkdLocation(),
                newSchedule.getSkdMemo()
        );

        scheduleRepository.save(schedule);
    }

    @Transactional
    public void updateSchedule(int skdNo, ScheduleDTO updateSchedule) {
        Schedule foundSchedule = scheduleRepository.findById((long) skdNo).orElseThrow(IllegalArgumentException::new);

        Schedule updatedSchedule = new Schedule.Builder()
                .skdNo(foundSchedule.getSkdNo())
                .department(foundSchedule.getDepartment())
                .skdName(updateSchedule.getSkdName() != null ? updateSchedule.getSkdName() : foundSchedule.getSkdName())
                .skdStartDttm(updateSchedule.getSkdStartDttm() != null ? updateSchedule.getSkdStartDttm() : foundSchedule.getSkdStartDttm())
                .skdEndDttm(updateSchedule.getSkdEndDttm() != null ? updateSchedule.getSkdEndDttm() : foundSchedule.getSkdEndDttm())
                .skdLocation(updateSchedule.getSkdLocation() != null ? updateSchedule.getSkdLocation() : foundSchedule.getSkdLocation())
                .skdMemo(updateSchedule.getSkdMemo() != null ? updateSchedule.getSkdMemo() : foundSchedule.getSkdMemo())
                .build();

        // 새로운 Schedule 객체 저장
        scheduleRepository.save(updatedSchedule);
    }

    @Transactional
    public void deleteById(int skdNo) {
        scheduleRepository.deleteById((long) skdNo);
    }
}
