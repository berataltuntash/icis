package com.icis.demo;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.icis.demo", "com.icis.demo.Utils"})
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
    @PostConstruct
    public void init() {
        System.out.println("com.icis.demo.App started");
    }
}
