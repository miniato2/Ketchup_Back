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

    public List<ScheduleDTO> selectScheduleList(int dptNo) {
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

    public List<ScheduleDTO> selectScheduleDetail(int dptNo, int skdNo) {
        Department department = departmentRepository.findById(dptNo)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 부서를 찾을 수 없습니다: " + dptNo));

        List<Schedule> scheduleDetail = scheduleRepository.findByDepartmentAndSkdNo(department, skdNo);
        return scheduleDetail.stream()
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

    @Transactional
    public void insertSchedule(ScheduleDTO newSchedule) {

        DepartmentDTO departmentDTO = newSchedule.getDptNo();
        Department department = new Department(departmentDTO.getDptNo());

        // 부서 정보 먼저 저장
        departmentRepository.save(department);

        // 테스트 이후 부서가 없는 일정을 등록하는 것을 방지하기 위해 부서 정보 저장하지 않을 예정
//        Department department = departmentRepository.findById(newSchedule.getDptNo().getDptNo())
//                .orElseThrow(() -> new IllegalArgumentException("해당 부서를 찾을 수 없습니다."));

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
    public void updateSchedule(ScheduleDTO updateSchedule) {
        Schedule foundSchedule = scheduleRepository.findById((long) updateSchedule.getSkdNo()).orElseThrow(IllegalArgumentException::new);

        if (updateSchedule.getSkdName() != null) {
            foundSchedule.skdName(updateSchedule.getSkdName());
        }
        if (updateSchedule.getSkdStartDttm() != null) {
            foundSchedule.skdStartDttm(updateSchedule.getSkdStartDttm());
        }
        if (updateSchedule.getSkdEndDttm() != null) {
            foundSchedule.skdEndDttm(updateSchedule.getSkdEndDttm());
        }
        if (updateSchedule.getSkdLocation() != null) {
            foundSchedule.skdLocation(updateSchedule.getSkdLocation());
        }
        if (updateSchedule.getSkdMemo() != null) {
            foundSchedule.skdMemo(updateSchedule.getSkdMemo());
        }

        scheduleRepository.save(foundSchedule.builder());
    }

    @Transactional
    public void deleteById(int skdNo) {
        scheduleRepository.deleteById((long) skdNo);
    }
}
