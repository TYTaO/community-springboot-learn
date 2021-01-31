package com.tytao.community.mapper;

import com.tytao.community.model.Question;
import com.tytao.community.model.QuestionExample;
import org.apache.ibatis.annotations.Param;

public interface QuestionExtMapper {

    int incView(Question record);
    int incCommentCount(Question record);
}
