package com.devsplan.ketchup.member.entity;

import jakarta.persistence.*;

@Entity
@Table(name="TBL_POSITION")
public class Position {

    @Id
    @Column(name="POSITION_NO")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String positionNo;
    @Column(name="POSITION_NAME")
    private String positionNAme;
    @Column(name="POSITION_LEVEL")
    private int positionLevel;
    @ManyToOne
    @JoinColumn(name = "AUTHORITY_NO")
    private Authority Authority;
    @Column(name="POSITION_STATUS")
    private char positionStatus;

    public Position() {
    }
}
