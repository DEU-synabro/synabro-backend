package com.deu.synabro.entity.enums;

public enum ApprovalType {
    permit("허용"),
    wait("대기"),
    refuse("거절");

    private String value;

    ApprovalType(String value) { this.value = value; }

    public String getValue() { return this.value; }
}
