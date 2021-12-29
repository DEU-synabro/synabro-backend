package com.deu.synabro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class SynabroApplication {

    public static void main(String[] args) {
        SpringApplication.run(SynabroApplication.class, args);
    }

    @GetMapping
    public String HelloWorld() {
        return "hello world";
    }
}
