package com.example.adplatform.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SystemUserVO {

    private Long id;
    private String username;
    private String nickname;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
