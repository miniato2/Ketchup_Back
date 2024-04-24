package com.devsplan.ketchup.approval.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ApprovalDTO {
    private int approvalNo; //기안번호
    private int appMember;   //기안자 사원번호
    private int formNo; //양식번호
    private String appTitle; //제목
    private String appContents; //내용
    private String appDate; //기안날짜
    private String appFinalDate; //완료날짜
    private String appStatus;  //상태

    private List<AppLineDTO> appLine; //결재선
    private List<String> fileURL; //파일 경로
    private String refusal; //반려 사유

    public ApprovalDTO(int appMember, int formNo, String appTitle, String appContents) {
        this.appMember = appMember;
        this.formNo = formNo;
        this.appTitle = appTitle;
        this.appContents = appContents;
    }
}
