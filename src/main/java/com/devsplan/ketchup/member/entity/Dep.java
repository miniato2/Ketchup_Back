package com.devsplan.ketchup.member.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="TBL_DEP")
public class Dep {

    @Id
    @Column(name="DEP_NO")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int depNo;
    @Column(name="DEP_NAME")
    private String depName;
    @Column(name="DEP_STATUS")
    private char status;
    @Transient // 데이터베이스에 매핑되지 않는 임시 필드로 지정
    private long memberCount;




    protected Dep() {
    }

    public Dep(int depNo, String depName, char status) {
        this.depNo = depNo;
        this.depName = depName;
        this.status = status;


    }

    public Dep(int depNo, String depName, long memberCount) {
        this.depNo = depNo;
        this.depName = depName;
        this.memberCount = memberCount;
    }

    public Dep(int depNo, String depName, char status, long memberCount) {
        this.depNo = depNo;
        this.depName = depName;
        this.status = status;
        this.memberCount = memberCount;
    }

    public Dep depNo(int depNo){
        this.depNo = depNo;
        return this;
    }

    public Dep depName(String depName){
        this.depName = depName;
        return this;

    }



    public Dep status(char status){
        this.status= status;
        return this;

    }

    public long getMemberCount() {
        return memberCount;
    }

    public Dep build() {
        return new Dep(depNo,depName,status);
    }

    public int getDepNo() {
        return depNo;
    }

    public String getDepName() {
        return depName;
    }

    public char getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Dep{" +
                "depNo=" + depNo +
                ", depName='" + depName + '\'' +
                ", status=" + status +
                ", memberCount=" + memberCount +
                '}';
    }
}