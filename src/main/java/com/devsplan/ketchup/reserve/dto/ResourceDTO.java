package com.devsplan.ketchup.reserve.dto;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResourceDTO {

    private int rscNo;
    private String rscCategory;
    private String rscName;
    private String rscInfo;
    private int rscCap;
    private boolean rscIsAvailable;
    private String rscDescr;

    public boolean getRscIsAvailable() {
        return rscIsAvailable;
    }
}
