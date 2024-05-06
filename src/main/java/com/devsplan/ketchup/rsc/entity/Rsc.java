package com.devsplan.ketchup.rsc.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "tbl_rsc")
@Getter
public class Rsc {
    @Id
    @Column(name = "RSC_NO", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rscNo;

    @Column(name = "RSC_CATEGORY", nullable = false)
    private String rscCategory;

    @Column(name = "RSC_NAME", nullable = false)
    private String rscName;

    @Column(name = "RSC_INFO", nullable = false)
    private String rscInfo;

    @Column(name = "RSC_CAP", nullable = false)
    private int rscCap;

    @Column(name = "RSC_IS_AVAILABLE", nullable = false)
    private boolean rscIsAvailable;

    @Column(name = "RSC_DESCR")
    private String rscDescr;

    protected Rsc() {}

    public Rsc rscIsAvailable(boolean val) {
        this.rscIsAvailable = val;
        return this;
    }

    public Rsc rscDescr(String val) {
        this.rscDescr = val;
        return this;
    }

    public Rsc(String rscCategory, String rscName, String rscInfo, int rscCap) {
        this.rscCategory = rscCategory;
        this.rscName = rscName;
        this.rscInfo = rscInfo;
        this.rscCap = rscCap;
    }
}
