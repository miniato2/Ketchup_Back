package com.devsplan.ketchup.schedule.dto;

import lombok.*;

import java.util.Date;

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
