package com.devsplan.ketchup.reserve.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
@Entity
@Table(name = "TBL_RESERVE")
public class Reserve {

    @Id
    @Column(name = "RSV_NO", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rsvNo;

    @Column(name = "RSV_START_DTTM")
    private LocalDateTime rsvStartDttm;

    @Column(name = "RSV_END_DTTM")
    private LocalDateTime rsvEndDttm;

    @Column(name = "RSV_DESCR")
    private String rsvDescr;

    @ManyToOne
    @JoinColumn(name = "RESOURCES")
    private Resource resources;

    public Reserve() {
    }

    public Reserve(int rsvNo, String rsvDescr, LocalDateTime rsvStartDttm, LocalDateTime rsvEndDttm, Resource resource) {
        this.rsvNo = rsvNo;
        this.rsvDescr = rsvDescr;
        this.rsvStartDttm = rsvStartDttm;
        this.rsvEndDttm = rsvEndDttm;
        this.resources = resource;
    }

    public Reserve(int rsvNo, LocalDateTime rsvStartDttm, LocalDateTime rsvEndDttm, String rsvDescr) {
        this.rsvNo = rsvNo;
        this.rsvStartDttm = rsvStartDttm;
        this.rsvEndDttm = rsvEndDttm;
        this.rsvDescr = rsvDescr;
    }

    public void setResources(Resource resources) {
        this.resources = resources;
    }

    public Reserve rsvNo(int val) {
        this.rsvNo = val;
        return this;
    }

    public Reserve rsvStartDttm(LocalDateTime val) {
        this.rsvStartDttm = val;
        return this;
    }

    public Reserve rsvEndDttm(LocalDateTime val) {
        this.rsvEndDttm = val;
        return this;
    }

    public Reserve rsvDescr(String val) {
        this.rsvDescr = val;
        return this;
    }

    public Reserve builder() {
        return new Reserve(rsvNo, rsvStartDttm, rsvEndDttm, rsvDescr);
    }

}

