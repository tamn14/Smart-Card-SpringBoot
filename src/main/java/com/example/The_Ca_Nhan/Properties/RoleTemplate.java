package com.example.The_Ca_Nhan.Properties;

public enum RoleTemplate {
    USER("USER"),
    ADMIN("ADMIN");

    private final String value;

    RoleTemplate(String value) {
        this.value = value;
    }


    public String getValue() {
        return value;
    }
}
