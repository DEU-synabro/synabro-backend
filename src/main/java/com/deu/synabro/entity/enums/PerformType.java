package com.deu.synabro.entity.enums;

public enum PerformType {
    WAIT("대기중"),
    PERFORMING("진행중"),
    DONE("완료");

    private String value;

    PerformType(String value) { this.value = value; }

    public String getValue() { return this.value; }
}
