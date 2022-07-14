package com.deu.synabro.entity.enums;

public enum ApplyOption {
    member("회원"),
    non_member("수혜자");

    private String applyOption;

    ApplyOption(String applyOption) { this.applyOption = applyOption; }
    public String getApplyOption() { return this.applyOption; }
}
