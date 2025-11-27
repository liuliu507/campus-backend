// src/main/java/com/ucampus/ucampus_backend/UcampusBackendApplication.java
package com.ucampus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.ucampus.entity")  // 添加这行，扫描实体类
@EnableJpaRepositories("com.ucampus.repository")  // 添加这行，扫描Repository
public class UcampusBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(UcampusBackendApplication.class, args);
    }
}