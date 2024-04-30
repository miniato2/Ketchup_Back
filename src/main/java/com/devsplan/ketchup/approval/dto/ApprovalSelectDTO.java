package com.devsplan.ketchup.approval.dto;

import com.devsplan.ketchup.member.dto.MemberDTO;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ApprovalSelectDTO {
    private int approvalNo;
    private MemberDTO member;
    private FormDTO form;
    private String appTitle;
    private String appContents;
    private String appDate;
    private String appFinalDate;
    private String appStatus;
    private String refusal;
    private List<AppFileDTO> appFileList;
    private List<AppLineDTO> appLineList;
    private List<RefLineDTO> refLineList;

    public ApprovalSelectDTO(MemberDTO member, FormDTO form, String appTitle, String appContents) {
        this.member = member;
        this.form = form;
        this.appTitle = appTitle;
        this.appContents = appContents;
    }
}
