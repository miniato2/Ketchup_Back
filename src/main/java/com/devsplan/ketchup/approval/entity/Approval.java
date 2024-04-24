package com.devsplan.ketchup.approval.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
@Entity
@Table(name = "TBL_APPROVAL")
public class Approval {

    @Id
    @Column(name = "APPROVAL_NO", nullable = false, unique = true)
    private int approvalNo;
    @Column(name = "APP_MEMBER_NO", nullable = false)
    private int appMemberNo;
    @Column(name = "FORM_NO", nullable = false)
    private int formNo;
    @Column(name = "APP_TITLE", nullable = false)
    private String appTitle;
    @Column(name = "APP_CONTENTS",columnDefinition = "LONGTEXT", nullable = false)
    private String appContents;
    @Column(name = "APP_DATE", nullable = false)
    private String appDate;
    @Column(name = "APP_FINAL_DATE")
    private String appFinalDate;
    @Column(name = "APP_STATUS", nullable = false)
    private String appStatus;
    //대기 진행 회수 반려 완료

    protected Approval(){}

}
