package com.devsplan.ketchup.schedule.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@Entity
@Table(name = "TBL_SCHEDULE")
@AllArgsConstructor
@Getter
@ToString
public class Schedule {
    @Id
    @Column(name = "SKD_NO")
    private int skdNo;

    @ManyToOne
    @JoinColumn(name = "DPT_NO")
    private Department dptNo;

    @Column(name = "SKD_NAME")
    private String skdName;

    @Column(name = "SKD_START_DTTM")
    private Date skdStartDttm;

    @Column(name = "SKD_END_DTTM")
    private Date skdEndDttm;

    @Column(name = "SKD_LOCATION")
    private String skdLocation;

    @Column(name = "SKD_MEMO")
    private String skdMemo;

    protected Schedule() {}
}
