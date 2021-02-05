package com.tytao.community.mapper;

import com.tytao.community.model.Comment;

public interface CommentExtMapper {
    int incCommentCount(Comment comment);

    int incLikeCount(Comment comment);

    int decLikeCount(Comment comment);
}