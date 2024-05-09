package com.devsplan.ketchup.reserve.dto;

import com.devsplan.ketchup.rsc.dto.ResourceDTO;
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
    private String reserver;

    private ResourceDTO resources;

    public ReserveDTO() {
    }

    public ReserveDTO(int rsvNo, String rsvDescr, LocalDateTime rsvStartDttm, LocalDateTime rsvEndDttm, String reserver, ResourceDTO resources) {
        this.rsvNo = rsvNo;
        this.rsvDescr = rsvDescr;
        this.rsvStartDttm = rsvStartDttm;
        this.rsvEndDttm = rsvEndDttm;
        this.reserver = reserver;
        this.resources = resources;
    }

    public ReserveDTO(String rsvDescr, LocalDateTime rsvStartDttm, LocalDateTime rsvEndDttm, String reserver, ResourceDTO resourceDTO) {
        this.rsvDescr = rsvDescr;
        this.rsvStartDttm = rsvStartDttm;
        this.rsvEndDttm = rsvEndDttm;
        this.reserver = reserver;
        this.resources = resourceDTO;
    }

    public ReserveDTO(int rsvNo, String rsvDescr, LocalDateTime rsvStartDttm, LocalDateTime rsvEndDttm) {
        this.rsvNo = rsvNo;
        this.rsvDescr = rsvDescr;
        this.rsvStartDttm = rsvStartDttm;
        this.rsvEndDttm = rsvEndDttm;
    }
}
