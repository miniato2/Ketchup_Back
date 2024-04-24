package com.devsplan.ketchup.member.entity;

import jakarta.persistence.*;

@Entity
@Table(name="TBL_DEP")
public class Dep {

    @Id
    @Column(name="DEP_NO")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String depNo;
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
}
