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
@Table(name = "TBL_FORM")
public class Form {
    @Id
    @Column(name = "FORM_NO", nullable = false)
    private int formNo;
    @Column(name = "FORM_NAME", nullable = false)
    private String formName;
    @Column(name = "FORM_CONTENTS",columnDefinition = "LONGTEXT", nullable = false)
    private String formContents;
    //longtext 타입

    protected Form(){}
}
