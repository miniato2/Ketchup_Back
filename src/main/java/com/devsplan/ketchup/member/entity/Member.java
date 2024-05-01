package com.devsplan.ketchup.member.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "TBL_MEMBER")
public class Member {
    @Id
    @Column(name="MEMBER_NO")
    private String memberNo;
    @Column(name="MEMBER_NAME")
    private String memberName;
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
    private String status;
    @Column(name="MEMBER_IMGURL")
    private String imgUrl;

    @Column(name = "MEMBER_RESIGNDATETIME")
    private LocalDateTime resignDateTime;




    public String getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberPW() {
        return memberPW;
    }

    public void setMemberPW(String memberPW) {
        this.memberPW = memberPW;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrivateEmail() {
        return privateEmail;
    }

    public void setPrivateEmail(String privateEmail) {
        this.privateEmail = privateEmail;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    public Dep getDepartment() {
        return department;
    }

    public void setDepartment(Dep department) {
        this.department = department;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getState() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getResignDateTime() {
        return resignDateTime;
    }

    public void setResignDateTime(LocalDateTime resignDateTime) {
        this.resignDateTime = resignDateTime;
    }

    @Override
    public String toString() {
        return "Member{" +
                "memberNo='" + memberNo + '\'' +
                ", memberName='" + memberName + '\'' +
                ", memberPW='" + memberPW + '\'' +
                ", phone='" + phone + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", gender=" + gender +
                ", address='" + address + '\'' +
                ", privateEmail='" + privateEmail + '\'' +
                ", companyEmail='" + companyEmail + '\'' +
                ", department=" + department +
                ", position=" + position +
                ", account='" + account + '\'' +
                ", status='" + status + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }
}