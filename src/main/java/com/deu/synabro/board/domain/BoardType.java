package com.deu.synabro.board.domain;

public enum BoardType {
    notice ("공지사항");

    private String value;

    BoardType(String value){
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
