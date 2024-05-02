package com.devsplan.ketchup.rsc.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "tbl_rsc")
public class Rsc {
    @Id
    @Column(name = "RSC_NO", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rsc_no;

    @Column(name = "RSC_CATEGORY", nullable = false)
    private String rscCategory;

    @Column(name = "RSC_NAME", nullable = false)
    private String rscName;

    @Column(name = "RSC_INFO", nullable = false)
    private String rscInfo;

    @Column(name = "RSC_CAP", nullable = false)
    private int rscCap;

    @Column(name = "RSC_IS_AVAILABLE", nullable = false)
    private char rscIsAvailable;

    @Column(name = "RSC_DESCR")
    private String rscDescr;

    protected Rsc() {}

}
