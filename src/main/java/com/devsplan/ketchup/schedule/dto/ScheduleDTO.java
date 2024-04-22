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
    private String skdName;
    private Date skdStartDttm;
    private Date skdEndDttm;
    private String skdLocation;
    private String skdMemo;
}
