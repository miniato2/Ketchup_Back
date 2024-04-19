package com.devsplan.ketchup.schedule.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;

@Entity
@Table(name = "schedule")
public class Schedule {
    @Id
    @Column(name = "schedule_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int scheduleNo;

    @Column(name = "schedule_memo")
    private String scheduleMemo;

    protected Schedule() {
    }

    public Schedule(int scheduleNo, String scheduleMemo) {
        this.scheduleNo = scheduleNo;
        this.scheduleMemo = scheduleMemo;
    }

    public int getScheduleNo() {
        return scheduleNo;
    }

    public String getScheduleMemo() {
        return scheduleMemo;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "scheduleNo=" + scheduleNo +
                ", scheduleMemo='" + scheduleMemo + '\'' +
                '}';
    }
}
