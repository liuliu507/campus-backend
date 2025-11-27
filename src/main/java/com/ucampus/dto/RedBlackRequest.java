// src/main/java/com/ucampus/dto/RedBlackRequest.java
package com.ucampus.dto;

import lombok.Data;

@Data
public class RedBlackRequest {
    private String title;        // 对应前端的 title
    private String content;      // 对应前端的 content  
    private String type;         // 对应前端的 type (red/black)
    private Long publisherId = 1L; // 暂时写死，后续从登录用户获取
}