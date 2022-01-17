package com.deu.synabro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SynabroApplication {
    public static void main(String[] args) {
        SpringApplication.run(SynabroApplication.class, args);
    }
}
