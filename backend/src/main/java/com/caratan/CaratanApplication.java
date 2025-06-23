package com.caratan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.caratan"})
public class CaratanApplication {

    public static void main(String[] args) {
        SpringApplication.run(CaratanApplication.class, args);
    }
} 