package com.devsplan.ketchup.schedule.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TBL_SCHEDULE")
public class Schedule {
    @Id
    @Column(name = "SKD_NO")
    private int skdNo;

    @ManyToOne
    @JoinColumn(name = "DPT_NO")
    private Department department;

    @Column(name = "SKD_NAME")
    private String skdName;

    @Column(name = "SKD_START_DTTM")
    private String skdStartDttm;

    @Column(name = "SKD_END_DTTM")
    private String skdEndDttm;

    @Column(name = "SKD_LOCATION")
    private String skdLocation;

    @Column(name = "SKD_MEMO")
    private String skdMemo;

    protected Schedule() {}

    public Schedule(int skdNo, Department department, String skdName, String skdStartDttm, String skdEndDttm, String skdLocation, String skdMemo) {
        this.skdNo = skdNo;
        this.department = department;
        this.skdName = skdName;
        this.skdStartDttm = skdStartDttm;
        this.skdEndDttm = skdEndDttm;
        this.skdLocation = skdLocation;
        this.skdMemo = skdMemo;
    }

    public int getSkdNo() {
        return skdNo;
    }

    public Department getDepartment() {
        return department;
    }

    public String getSkdName() {
        return skdName;
    }

    public String getSkdStartDttm() {
        return skdStartDttm;
    }

    public String getSkdEndDttm() {
        return skdEndDttm;
    }

    public String getSkdLocation() {
        return skdLocation;
    }

    public String getSkdMemo() {
        return skdMemo;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "skdNo=" + skdNo +
                ", department=" + department +
                ", skdName='" + skdName + '\'' +
                ", skdStartDttm=" + skdStartDttm +
                ", skdEndDttm=" + skdEndDttm +
                ", skdLocation='" + skdLocation + '\'' +
                ", skdMemo='" + skdMemo + '\'' +
                '}';
    }

    public static class Builder {
        private int skdNo;
        private Department department;
        private String skdName;
        private String skdLocation;
        private String skdMemo;
        private String skdStartDttm;
        private String skdEndDttm;

        public Builder skdNo(int skdNo) {
            this.skdNo = skdNo;
            return this;
        }

        public Builder department(Department department) {
            this.department = department;
            return this;
        }

        public Builder skdName(String skdName) {
            this.skdName = skdName;
            return this;
        }

        public Builder skdLocation(String skdLocation) {
            this.skdLocation = skdLocation;
            return this;
        }

        public Builder skdMemo(String skdMemo) {
            this.skdMemo = skdMemo;
            return this;
        }

        public Builder skdStartDttm(String skdStartDttm) {
            this.skdStartDttm = skdStartDttm;
            return this;
        }

        public Builder skdEndDttm(String skdEndDttm) {
            this.skdEndDttm = skdEndDttm;
            return this;
        }

        public Schedule build() {
            return new Schedule(this);
        }
    }

    private Schedule(Builder builder) {
        this.skdNo = builder.skdNo;
        this.department = builder.department;
        this.skdName = builder.skdName;
        this.skdLocation = builder.skdLocation;
        this.skdMemo = builder.skdMemo;
        this.skdStartDttm = builder.skdStartDttm;
        this.skdEndDttm = builder.skdEndDttm;
    }

}
