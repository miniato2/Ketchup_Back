package com.devsplan.ketchup.approval.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AppInputDTO {
    private ApprovalDTO approval;
    private List<AppLineDTO> appLineDTOList;
    private List<RefLineDTO> refLineDTOList;
}
