package com.devsplan.ketchup.member.entity;

import jakarta.persistence.*;

@Entity
@Table(name="TBL_DEP")
public class Dep {

    @Id
    @Column(name="DEP_NO")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int depNo;
    @Column(name="DEP_NAME")
    private String depName;
    @Column(name="DEP_LEADER")
    private String leader;
    @Column(name="DEP_NUMBER")
    private int number;
    @Column(name="DEP_STATUS")
    private char status;

    public Dep() {
    }

    public int getDepNo() {
        return depNo;
    }

    public String getDepName() {
        return depName;
    }

    public String getLeader() {
        return leader;
    }

    public int getNumber() {
        return number;
    }

    public char getStatus() {
        return status;
    }

    public void setDepNo(int depNo) {
        this.depNo = depNo;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Dep{" +
                "depNo=" + depNo +
                ", depName='" + depName + '\'' +
                ", leader='" + leader + '\'' +
                ", number=" + number +
                ", status=" + status +
                '}';
    }
}
