package com.devsplan.ketchup.schedule.dto;

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
    private LocalDateTime skdStartDttm;
    private LocalDateTime skdEndDttm;
    private String skdLocation;
    private String skdMemo;

}
