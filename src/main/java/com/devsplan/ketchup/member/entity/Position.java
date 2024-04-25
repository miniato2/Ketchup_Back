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
    private Authority authority;
    @Column(name="POSITION_STATUS")
    private char positionStatus;

    public Position() {
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

    public List<String> getRoleList(){
        if(this.authority.getRole().length() > 0){
            return Arrays.asList(this.authority.getRole().split(","));
        }
        return new ArrayList<>();
    }
}
