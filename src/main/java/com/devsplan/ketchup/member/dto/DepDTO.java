package com.devsplan.ketchup.member.dto;

public class DepDTO {
    private String depNo;
    private String depName;
    private String leader;
    private int count;
    private char status;

    public DepDTO() {
    }

    public DepDTO(String depNo, String depName, String leader, int count, char status) {
        this.depNo = depNo;
        this.depName = depName;
        this.leader = leader;
        this.count = count;
        this.status = status;
    }

    public String getDepNo() {
        return depNo;
    }

    public void setDepNo(String depNo) {
        this.depNo = depNo;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "DepDTO{" +
                "depNo='" + depNo + '\'' +
                ", depName='" + depName + '\'' +
                ", leader='" + leader + '\'' +
                ", count=" + count +
                ", status=" + status +
                '}';
    }
}
