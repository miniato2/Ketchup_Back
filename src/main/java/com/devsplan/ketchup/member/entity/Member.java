package com.devsplan.ketchup.member.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "TBL_MEMBER")
public class Member {


    @Id
    @Column(name="MEMBER_NO")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int memberNo;
    @Column(name="MEMBER_PW")
    private String memberPW;
    @Column(name="MEMBER_PHONE")
    private String phone;
    @Column(name="MEMBER_BIRTHDATE")
    private String birthDate;
    @Column(name="MEMBER_GENDER")
    private char gender;
    @Column(name="MEMBER_ADDRESS")
    private String address;
    @Column(name="MEMBER_PRIVATEEMAIL")
    private String privateEmail;
    @Column(name="MEMBER_COMPANYEMAIL")
    private String companyEmail;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name= "DEP_NO")
    private Dep department;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name= "POSITION_NO")
    private Position position;
    @Column(name="MEMBER_ACCOUNT")
    private String account;
    @Column(name="MEMBER_STATUS")
    private char status;
    @Column(name="MEMBER_IMGURL")
    private String imgUrl;

    protected Member() {

    }


}
