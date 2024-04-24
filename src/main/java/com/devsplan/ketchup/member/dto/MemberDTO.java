package com.devsplan.ketchup.member.dto;

public class MemberDTO {

    private int memberNo;
    private String memberPW;
    private String phone;
    private String birthDate;
    private char gender;
    private String address;
    private String privateEmail;
    private String companyEmail;
    private DepDTO department;
    private PositionDTO role;
    private String account;
    private char status;
    private String imgUrl;


    public MemberDTO() {
    }

    public MemberDTO(int memberNo, String memberPW, String phone, String birthDate, char gender, String address, String privateEmail, String companyEmail, DepDTO department, PositionDTO role, String account, char status, String imgUrl) {
        this.memberNo = memberNo;
        this.memberPW = memberPW;
        this.phone = phone;
        this.birthDate = birthDate;
        this.gender = gender;
        this.address = address;
        this.privateEmail = privateEmail;
        this.companyEmail = companyEmail;
        this.department = department;
        this.role = role;
        this.account = account;
        this.status = status;
        this.imgUrl = imgUrl;
    }

    public int getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(int memberNo) {
        this.memberNo = memberNo;
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

    public DepDTO getDepartment() {
        return department;
    }

    public void setDepartment(DepDTO department) {
        this.department = department;
    }

    public PositionDTO getRole() {
        return role;
    }

    public void setRole(PositionDTO role) {
        this.role = role;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public String toString() {
        return "MemberDTO{" +
                "memberNo=" + memberNo +
                ", memberPW='" + memberPW + '\'' +
                ", phone='" + phone + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", gender=" + gender +
                ", address='" + address + '\'' +
                ", privateEmail='" + privateEmail + '\'' +
                ", companyEmail='" + companyEmail + '\'' +
                ", department=" + department +
                ", role=" + role +
                ", account='" + account + '\'' +
                ", status=" + status +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }
}
