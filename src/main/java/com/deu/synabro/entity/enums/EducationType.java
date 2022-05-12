package com.deu.synabro.entity.enums;

public enum EducationType {
    TODO ("할 일"),
    PROGRESS ("진행중"),
    DONE ("완료");

    private String value;

    EducationType(String value){
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
