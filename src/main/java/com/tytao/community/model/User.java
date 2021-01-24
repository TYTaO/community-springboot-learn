package com.tytao.community.model;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String accountId;
    private String token;
    private String name;
    private Long gmtCreate;
    private Long gmtModified;
}
