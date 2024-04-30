package com.devsplan.ketchup.reserve.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ReserveDTO {

    private int rsvNo;
    private String rscCategory;
    private String rscName;
    private String rscInfo;
    private int rscCap;
    private boolean rscIsAvailable;
    private String rsvDescr;
    private LocalDateTime rsvStartDttm;
    private LocalDateTime rsvEndDttim;
    private String memberName;
}
