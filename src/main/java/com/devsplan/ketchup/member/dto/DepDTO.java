package com.devsplan.ketchup.member.dto;

public class DepDTO {
    private int depNo;
    private String depName;
    private String leader;
    private int number;
    private char status;

    public DepDTO() {
    }

    public DepDTO(int depNo) {
        this.depNo = depNo;
    }

    public DepDTO(int depNo, String depName, String leader, int number, char status) {
        this.depNo = depNo;
        this.depName = depName;
        this.leader = leader;
        this.number = number;
        this.status = status;
    }

    public int getDepNo() {
        return depNo;
    }

    public void setDepNo(int depNo) {
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

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
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
                ", number=" + number +
                ", status=" + status +
                '}';
    }
}