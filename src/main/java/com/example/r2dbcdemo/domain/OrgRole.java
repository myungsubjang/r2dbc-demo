package com.example.r2dbcdemo.domain;

public enum OrgRole {
    MANAGER("01"), MEMBER("02");

    private String code;

    OrgRole(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
