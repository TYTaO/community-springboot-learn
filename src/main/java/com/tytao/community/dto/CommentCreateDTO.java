package com.tytao.community.dto;

import lombok.Data;

/**
 * 创建回复时的回复 数据类型
 */
@Data
public class CommentCreateDTO {
    private Long parentId;
    private String content;
    private Integer type;
}
