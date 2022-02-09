package com.deu.synabro.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "authority")
@Getter
public class Authority {
    @Id
    @Column(name = "authority_name", length =50)
    private String authorityName;
}
