package com.deu.synabro.entity.enums;

public enum ApplyOption {
    user("회원"),
    beneficiary("수혜자");

    private String applyOption;

    ApplyOption(String applyOption) { this.applyOption = applyOption; }
    public String getApplyOption() { return this.applyOption; }
}
