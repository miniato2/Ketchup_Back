package com.devsplan.ketchup.reserve.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "TBL_RESERVE")
public class Reserve {

    @Id
    @Column(name = "RSV_NO")
    private int rsvNo;

    @Column(name = "RSC_CATEGORY")
    private String rscCategory;

    @Column(name = "RSC_NAME")
    private String rscName;

    @Column(name = "RSC_INFO")
    private String rscInfo;

    @Column(name = "RSC_CAP")
    private int rscCap;

    @Column(name = "RSC_IS_AVAILABLE")
    private boolean rscIsAvailable;

    @Column(name = "RSV_DESCR")
    private String rsvDescr;

    @Column(name = "RSV_START_DTTM")
    private LocalDateTime rsvStartDttm;

    @Column(name = "RSV_END_DTTM")
    private LocalDateTime rsvEndDttim;

    @Column(name = "MEMBER_NAME")
    private String memberName;

    protected Reserve() {}
}
