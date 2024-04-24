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
@Table(name = "TBL_REF_LINE")
public class RefLine {
    @Id
    @Column(name = "REF_LINE_NO" )
    private int refLineNo;
    @Column(name = "APPROVAL_NO")
    private int approvalNo;
    @Column(name = "REF_MEMBER_NO")
    private int refMemberNo;

    protected RefLine(){}
}
