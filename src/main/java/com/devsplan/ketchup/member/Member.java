package com.devsplan.ketchup.member;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "member")
public class Member {

    @Id
    @Column(name = "member_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int memberNo;   // 사번

    @Column(name = "member_name")
    private String memberName;  // 이름

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<DepartmentBoard> departmentBoards;   // 부서번호

}
