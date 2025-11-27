// src/main/java/com/ucampus/controller/TestController.java
package com.ucampus.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/api/test")
    public String test() {
        return "应用运行正常！";
    }

    @GetMapping("/")
    public String home() {
        return "U校园集后端服务运行中！";
    }
}