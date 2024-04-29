package com.devsplan.ketchup.approval.entity;

import com.devsplan.ketchup.member.entity.Member;
import jakarta.persistence.*;

import java.util.List;

@Entity(name = "AppSelect")
@Table(name = "TBL_APPROVAL")
public class ApprovalSelect {
    @Id
    @Column(name = "APPROVAL_NO")
    private int approvalNo;
    @ManyToOne
    @JoinColumn(name = "MEMBER_NO")
    private Member member;
    @ManyToOne
    @JoinColumn(name = "FORM_NO")
    private Form form;
    @Column(name = "APP_TITLE")
    private String appTitle;
    @Column(name = "APP_CONTENTS")
    private String appContents;
    @Column(name = "APP_DATE")
    private String appDate;
    @Column(name = "APP_FINAL_DATE")
    private String appFinalDate;
    @Column(name = "APP_STATUS")
    private String appStatus;
    @Column(name = "APP_REFUSAL")
    private String refusal;
    @OneToMany
    @JoinColumn(name = "APPROVAL_NO")
    private List<AppFile> appFile;
    @OneToMany
    @JoinColumn(name = "APPROVAL_NO")
    private List<AppLine> appLineList;
    @OneToMany
    @JoinColumn(name = "APPROVAL_NO")
    private List<RefLine> refLineList;
}
