package com.devsplan.ketchup.member;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DepartmentDTO {

    private int departmentNo;   // 부서번호
    private String departmentName;  // 부서명

    public int getDepartmentNo() {
        return departmentNo;
    }

    public void setDepartmentNo(int departmentNo) {
        this.departmentNo = departmentNo;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
}
