package com.devsplan.ketchup.common;

import com.devsplan.ketchup.schedule.entity.Department;
import com.devsplan.ketchup.schedule.entity.Schedule;

import java.time.LocalDateTime;

public class ScheduleBuilder {
    private int skdNo;

    private Department department;

    private String skdName;

    private LocalDateTime skdStartDttm;

    private LocalDateTime skdEndDttm;

    private String skdLocation;

    private String skdMemo;

    public ScheduleBuilder(int skdNo, Department department, String skdName, LocalDateTime skdStartDttm, LocalDateTime skdEndDttm, String skdLocation, String skdMemo) {
        this.skdNo = skdNo;
        this.department = department;
        this.skdName = skdName;
        this.skdStartDttm = skdStartDttm;
        this.skdEndDttm = skdEndDttm;
        this.skdLocation = skdLocation;
        this.skdMemo = skdMemo;
    }

    public ScheduleBuilder(Schedule schedule) {
        this.skdNo = schedule.getSkdNo();
        this.department = schedule.getDepartment();
        this.skdName = schedule.getSkdName();
        this.skdStartDttm = schedule.getSkdStartDttm();
        this.skdEndDttm = schedule.getSkdEndDttm();
        this.skdLocation = schedule.getSkdLocation();
        this.skdMemo = schedule.getSkdMemo();
    }

    public ScheduleBuilder skdNo(int val) {
        this.skdNo = val;
        return this;
    }

    public ScheduleBuilder department(Department val) {
        this.department = val;
        return this;
    }

    public ScheduleBuilder skdName(String val) {
        this.skdName = val;
        return this;
    }

    public ScheduleBuilder skdStartDttm(LocalDateTime val) {
        this.skdStartDttm = val;
        return this;
    }

    public ScheduleBuilder skdEndDttm(LocalDateTime val) {
        this.skdEndDttm = val;
        return this;
    }

    public ScheduleBuilder skdLocation(String val) {
        this.skdLocation = val;
        return this;
    }

    public ScheduleBuilder skdMemo(String val) {
        this.skdMemo = val;
        return this;
    }

    public Schedule builder() {
        return new Schedule(skdNo, department, skdName, skdStartDttm, skdEndDttm, skdLocation, skdMemo);
    }
}
