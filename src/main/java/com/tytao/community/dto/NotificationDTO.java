package com.tytao.community.dto;

import com.tytao.community.model.User;
import lombok.Data;

@Data
public class NotificationDTO {
    private Long id;
    private Long gmtCreate;
    private Integer status;
    private Long notifierId;
    private String notifierName;
    private String outerTitle;
    private String typeName;
    private Long outerId;
    private Integer type;
}
