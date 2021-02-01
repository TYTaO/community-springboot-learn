package com.tytao.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode {

    QUESTION_NOT_FOUND(2001, "您找的问题不在呀亲~"),
    TARGET_PARAM_NOT_FOUND(2002, "未选中任何问题或评论进行回复"),
    NOT_LOGIN(2003, "未登录不能进行评论，请先登录哈"),
    SYS_ERROR(2004,"服务器冒烟了！！！"),
    TYPE_PARAM_WRONG(2005,"评论类型错误或不存在！"),
    COMMENT_NOT_FOUND(2006,"评论的评论不存在！"),
    CONTENT_IS_EMPTY(2007, "输入的内容不能为空"),
    READ_NOTIFICATION_FAIL(2008, "这个消息不属于你哦~"),
    NOTIFICATION_NOT_FOUND(2009, "无此通知~"),
    INVALID_INPUT(2011, "非法输入"),
    ;

    @Override
    public Integer getCode() {
        return code;
    }
    @Override
    public String getMessage() {
        return message;
    }



    private Integer code;
    private String message;

    CustomizeErrorCode(Integer code, String message) {
        this.message = message;
        this.code = code;
    }
}
