package com.devsplan.ketchup.approval.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
@Entity
@Table(name = "TBL_APPLINE")
public class AppLine {
    @Id
    @Column(name = "APPLINE_NO", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int appLineNo;
    @Column(name = "APPROVAL_NO", nullable = false)
    private int approvalNo; //기안 번호
    @Column(name = "MEMBER_NO", nullable = false)
    private int alMemberNo; //사원 번호
    @Column(name = "AL_SEQUENCE", nullable = false)
    private int alSequence; //순서
    @Column(name = "AL_TYPE", nullable = false)
    private String alType; //구분 (일반, 전결)
    @Column(name = "AL_DATE")
    private String alDate; //결재일자

    protected AppLine(){}
}
