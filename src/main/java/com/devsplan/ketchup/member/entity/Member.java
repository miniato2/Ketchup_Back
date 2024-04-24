package com.devsplan.ketchup.member.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "TBL_MEMBER")
public class Member {


    @Id
    @Column(name="MEMBER")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int memberNo;
    @Column(name="member_pw")
    private String memberPW;
    @Column(name="member_phone")
    private String phone;
    @Column(name="member_birthdate")
    private String birthDate;
    @Column(name="member_gender")
    private char gender;
    @Column(name="member_address")
    private String address;
    @Column(name="member_privateEmail")
    private String privateEmail;
    @Column(name="member_companyEmail")
    private String companyEmail;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name= "depNo")
    private Dep department;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name= "POSITION_NO")
    private Position position;
    private String account;
    private char status;
    private String imgUrl;

    protected Member() {

    }


}
