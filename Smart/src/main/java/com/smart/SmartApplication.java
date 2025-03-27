package com.smart; // 🚀 패키지 확인!

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = "com.smart")
public class SmartApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmartApplication.class, args);
    }
}
