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

    protected Dep() {
    }

    public Dep(int depNo, String depName, String leader, int number, char status) {
        this.depNo = depNo;
        this.depName = depName;
        this.leader = leader;
        this.number = number;
        this.status = status;
    }

    public Dep depNo(int depNo){
        this.depNo = depNo;
        return this;
    }

    public Dep depName(String depName){
        this.depName = depName;
        return this;

    }

    public Dep leader(String leader){
        this.leader = leader;
        return this;

    }

    public Dep number(int number){
        this.number = number;
        return this;
    }

    public Dep status(char status){
        this.status= status;
        return this;

    }

    public Dep build() {
        return new Dep(depNo,depName,leader,number,status);
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