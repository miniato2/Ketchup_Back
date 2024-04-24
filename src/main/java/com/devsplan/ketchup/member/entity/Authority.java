package com.devsplan.ketchup.member.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "TBL_AUTHORITY")
public class Authority {


    @Id
    @Column(name="AUTHORITY_NO")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int authorityNo;

    @Column(name="AUTHORITY_NAME")
    private String authorityName;  //권한명 (ex: ROLE_USER, ROLE_ADMIN)

    protected Authority() {
    }
}
