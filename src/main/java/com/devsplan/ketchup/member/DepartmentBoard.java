package com.devsplan.ketchup.member;

import jakarta.persistence.*;

@Entity
@Table(name = "department")
public class DepartmentBoard {

    @Id
    @Column(name = "department_no")
    private int departmentNo;   // 부서번호

    @Column(name = "department_name")
    private String departmentName;  // 부서명

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no")
    private Member member;
}
