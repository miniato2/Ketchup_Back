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
    // 기존 LocalDateTime 필드는 유지
    private String skdStartDttm;
    private String skdEndDttm;
    private String skdLocation;
    private String skdMemo;
    // 문자열 타입의 날짜와 시간 필드 추가
    private String skdStartDttmStr;
    private String skdEndDttmStr;

    public ScheduleDTO(int skdNo, DepartmentDTO dptNo, String skdName, String startDateTime, String endDateTime, String skdLocation, String skdMemo) {
        this.skdNo = skdNo;
        this.dptNo = dptNo;
        this.skdName = skdName;
        this.skdStartDttm = startDateTime;
        this.skdEndDttm = endDateTime;
        this.skdLocation = skdLocation;
        this.skdMemo = skdMemo;
    }
}
