package com.devsplan.ketchup.member.dto;

public class AuthorityDTO {
    private int authorityNo;
    private String authorityName;

    public AuthorityDTO() {
    }

    public AuthorityDTO(int authorityNo, String authorityName) {
        this.authorityNo = authorityNo;
        this.authorityName = authorityName;
    }

    public int getAuthorityNo() {
        return authorityNo;
    }

    public void setAuthorityNo(int authorityNo) {
        this.authorityNo = authorityNo;
    }

    public String getAuthorityName() {
        return authorityName;
    }

    public void setAuthorityName(String authorityName) {
        this.authorityName = authorityName;
    }

    @Override
    public String toString() {
        return "AuthorityDTO{" +
                "authorityNo=" + authorityNo +
                ", authorityName='" + authorityName + '\'' +
                '}';
    }
}
