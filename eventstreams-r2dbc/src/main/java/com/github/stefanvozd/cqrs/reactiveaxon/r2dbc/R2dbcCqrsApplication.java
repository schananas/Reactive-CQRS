package com.github.stefanvozd.cqrs.reactiveaxon.r2dbc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.github.stefanvozd.cqrs.reactiveaxon")
@SpringBootApplication
public class R2dbcCqrsApplication {

    public static void main(String[] args) {
        SpringApplication.run(R2dbcCqrsApplication.class, args);
    }

}
