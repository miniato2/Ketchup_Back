package com.devsplan.ketchup.schedule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ScheduleDTO {
    private int skdNo;
    private DepartmentDTO dptNo;
    private String skdName;
    private String skdStartDttm;
    private String skdEndDttm;
    private String skdLocation;
    private String skdMemo;
}
