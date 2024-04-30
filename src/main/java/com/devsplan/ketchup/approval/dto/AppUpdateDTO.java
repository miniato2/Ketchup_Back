package com.devsplan.ketchup.approval.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AppUpdateDTO {
    private int approvalNo;
    private String action;
    private String refusal;
}
