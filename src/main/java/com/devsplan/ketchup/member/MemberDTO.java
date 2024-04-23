package com.devsplan.ketchup.member;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MemberDTO {

    private int memberNo;   // 사번
    private String memberName;  // 이름
    private int departmentNo;   // 부서번호

    public int getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(int memberNo) {
        this.memberNo = memberNo;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public int getDepartmentNo() {
        return departmentNo;
    }

    public void setDepartmentNo(int departmentNo) {
        this.departmentNo = departmentNo;
    }
}
