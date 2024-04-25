package com.devsplan.ketchup.common;


public enum Authority {

    LV1("LV1"), LV2("LV2"), LV3("LV3"), ALL("LV1,LV2,LV3");

    private String role;

    Authority(String role){
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }



    @Override
    public String toString() {
        return "Authority{" +
                "role='" + role + '\'' +
                '}';
    }
}
