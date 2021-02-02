package com.tytao.community.dto;

import lombok.Data;

@Data
public class GithubUser {
    private String login; // name
    private Long id;
    private String bio;
    private String avatarUrl;
}
