package com.devsplan.ketchup.member.entity;


import com.devsplan.ketchup.common.Authority;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name="TBL_POSITION")
public class Position {

    @Id
    @Column(name="POSITION_NO")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int positionNo;
    @Column(name="POSITION_NAME")
    private String positionName;
    @Column(name="POSITION_LEVEL")
    private int positionLevel;
    @Column(name="POSITION_AUTHORITY")
    @Enumerated(value = EnumType.STRING)
    private Authority authority;
    @Column(name="POSITION_STATUS")
    private char positionStatus;

    public Position() {
    }

    public Position(Authority authority) {
    }


    public int getPositionNo() {
        return positionNo;
    }

    public String getPositionName() {
        return positionName;
    }

    public int getPositionLevel() {
        return positionLevel;
    }

    public Authority getAuthority() {
        return authority;
    }

    public char getPositionStatus() {
        return positionStatus;
    }

    public void setPositionNo(int positionNo) {
        this.positionNo = positionNo;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public void setPositionLevel(int positionLevel) {
        this.positionLevel = positionLevel;
    }

    public void setPositionStatus(char positionStatus) {
        this.positionStatus = positionStatus;
    }

    public List<String> getRoleList(){
        if(this.authority.getRole().length() > 0){
            return Arrays.asList(this.authority.getRole().split(","));
        }
        return new ArrayList<>();
    }

    public Position(int positionNo, String positionName, int positionLevel, Authority authority, char positionStatus) {
        this.positionNo = positionNo;
        this.positionName = positionName;
        this.positionLevel = positionLevel;
        this.authority = authority;
        this.positionStatus = positionStatus;
    }

    public void setAuthority(Authority authority) {
        this.authority = authority;
    }

    @Override
    public String toString() {
        return "Position{" +
                "positionNo=" + positionNo +
                ", positionName='" + positionName + '\'' +
                ", positionLevel=" + positionLevel +
                ", authority=" + authority +
                ", positionStatus=" + positionStatus +
                '}';
    }
}
