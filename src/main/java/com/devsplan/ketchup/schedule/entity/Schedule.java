package com.devsplan.ketchup.schedule.entity;

import com.devsplan.ketchup.common.ScheduleBuilder;
import com.devsplan.ketchup.schedule.dto.ScheduleDTO;
import com.fasterxml.jackson.core.JsonFactory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "TBL_SCHEDULE")
public class Schedule {
    @Id
    @Column(name = "SKD_NO")
    private int skdNo;

    @ManyToOne
    @JoinColumn(name = "DPT_NO")
    private Department department;

    @Column(name = "SKD_NAME")
    private String skdName;

    @Column(name = "SKD_START_DTTM")
    private LocalDateTime skdStartDttm;

    @Column(name = "SKD_END_DTTM")
    private LocalDateTime skdEndDttm;

    @Column(name = "SKD_LOCATION")
    private String skdLocation;

    @Column(name = "SKD_MEMO")
    private String skdMemo;

    public Schedule skdNo(int val) {
        this.skdNo = val;
        return this;
    }

    public Schedule department(Department val) {
        this.department = val;
        return this;
    }

    public Schedule skdName(String val) {
        this.skdName = val;
        return this;
    }

    public Schedule skdStartDttm(LocalDateTime val) {
        this.skdStartDttm = val;
        return this;
    }

    public Schedule skdEndDttm(LocalDateTime val) {
        this.skdEndDttm = val;
        return this;
    }

    public Schedule skdLocation(String val) {
        this.skdLocation = val;
        return this;
    }

    public Schedule skdMemo(String val) {
        this.skdMemo = val;
        return this;
    }

    public Schedule builder() {
        return new Schedule(skdNo, department, skdName, skdStartDttm, skdEndDttm, skdLocation, skdMemo);
    }

    protected Schedule() {}

    public Schedule(int skdNo, Department department, String skdName, LocalDateTime skdStartDttm, LocalDateTime skdEndDttm, String skdLocation, String skdMemo) {
        this.skdNo = skdNo;
        this.department = department;
        this.skdName = skdName;
        this.skdStartDttm = skdStartDttm;
        this.skdEndDttm = skdEndDttm;
        this.skdLocation = skdLocation;
        this.skdMemo = skdMemo;
    }

    public int getSkdNo() {
        return skdNo;
    }

    public Department getDepartment() {
        return department;
    }

    public String getSkdName() {
        return skdName;
    }

    public LocalDateTime getSkdStartDttm() {
        return skdStartDttm;
    }

    public LocalDateTime getSkdEndDttm() {
        return skdEndDttm;
    }

    public String getSkdLocation() {
        return skdLocation;
    }

    public String getSkdMemo() {
        return skdMemo;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "skdNo=" + skdNo +
                ", department=" + department +
                ", skdName='" + skdName + '\'' +
                ", skdStartDttm=" + skdStartDttm +
                ", skdEndDttm=" + skdEndDttm +
                ", skdLocation='" + skdLocation + '\'' +
                ", skdMemo='" + skdMemo + '\'' +
                '}';
    }
}
