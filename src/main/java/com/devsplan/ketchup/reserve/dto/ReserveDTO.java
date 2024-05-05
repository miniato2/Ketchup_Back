package com.devsplan.ketchup.reserve.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ReserveDTO {

    private int rsvNo;
    private String rsvDescr;
    private LocalDateTime rsvStartDttm;
    private LocalDateTime rsvEndDttm;

    private ResourceDTO resources;

    public ReserveDTO() {
    }

    public ReserveDTO(String rsvDescr, LocalDateTime rsvStartDttm, LocalDateTime rsvEndDttm, ResourceDTO resources) {
        this.rsvDescr = rsvDescr;
        this.rsvStartDttm = rsvStartDttm;
        this.rsvEndDttm = rsvEndDttm;
        this.resources = resources;
    }

    public ReserveDTO(int rsvNo, LocalDateTime rsvStartDttm, LocalDateTime rsvEndDttm, String rsvDescr) {
        this.rsvNo = rsvNo;
        this.rsvStartDttm = rsvStartDttm;
        this.rsvEndDttm = rsvEndDttm;
        this.rsvDescr = rsvDescr;
    }

    public ReserveDTO(int rsvNo, String rsvDescr, LocalDateTime rsvStartDttm, LocalDateTime rsvEndDttm, ResourceDTO resources) {
        this.rsvNo = rsvNo;
        this.rsvDescr = rsvDescr;
        this.rsvStartDttm = rsvStartDttm;
        this.rsvEndDttm = rsvEndDttm;
        this.resources = resources;
    }
}
