package com.devsplan.ketchup.member.dto;

public class PositionDTO {
    private String roleNo;
    private String roleName;
    private int roleLevel;
    private AuthorityDTO Authority;
    private char status;




    public PositionDTO() {
    }

    public PositionDTO(String roleNo, String roleName, int roleLevel, AuthorityDTO authority, char status) {
        this.roleNo = roleNo;
        this.roleName = roleName;
        this.roleLevel = roleLevel;
        Authority = authority;
        this.status = status;
    }

    public String getRoleNo() {
        return roleNo;
    }

    public void setRoleNo(String roleNo) {
        this.roleNo = roleNo;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public int getRoleLevel() {
        return roleLevel;
    }

    public void setRoleLevel(int roleLevel) {
        this.roleLevel = roleLevel;
    }

    public AuthorityDTO getAuthority() {
        return Authority;
    }

    public void setAuthority(AuthorityDTO authority) {
        Authority = authority;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "PositionDTO{" +
                "roleNo='" + roleNo + '\'' +
                ", roleName='" + roleName + '\'' +
                ", roleLevel=" + roleLevel +
                ", Authority=" + Authority +
                ", status=" + status +
                '}';
    }
}
