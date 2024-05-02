package com.devsplan.ketchup.member.dto;

public class MemberDTO {

    private String memberNo;
    private String memberName;
    private String memberPW;
    private String phone;
    private String birthDate;
    private char gender;
    private String address;
    private String privateEmail;
    private String companyEmail;
    private DepDTO department;
    private PositionDTO position;
    private String account;
    private String status;
    private String imgUrl;


    public MemberDTO() {
    }

    public MemberDTO(String memberNo, String memberName, String memberPW, String phone, String birthDate, char gender, String address, String privateEmail, String companyEmail, DepDTO department, PositionDTO position, String account, String status, String imgUrl) {
        this.memberNo = memberNo;
        this.memberName = memberName;
        this.memberPW = memberPW;
        this.phone = phone;
        this.birthDate = birthDate;
        this.gender = gender;
        this.address = address;
        this.privateEmail = privateEmail;
        this.companyEmail = companyEmail;
        this.department = department;
        this.position = position;
        this.account = account;
        this.status = status;
        this.imgUrl = imgUrl;
    }

    // 이진우 테스트
    public MemberDTO(String memberNo) {
    }

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

    public DepDTO getDepartment() {
        return department;
    }

    public void setDepartment(DepDTO department) {
        this.department = department;
    }

    public PositionDTO getPosition() {
        return position;
    }

    public void setPosition(PositionDTO position) {
        this.position = position;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getStatus() {
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

    @Override
    public String toString() {
        return "MemberDTO{" +
                "memberNo=" + memberNo +
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
                ", status=" + status +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }
}
