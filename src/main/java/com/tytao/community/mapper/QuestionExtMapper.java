package com.tytao.community.mapper;

import com.tytao.community.dto.QuestionDTO;
import com.tytao.community.dto.QuestionQueryDTO;
import com.tytao.community.model.Question;
import com.tytao.community.model.QuestionExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QuestionExtMapper {

    int incView(Question record);
    int incCommentCount(Question record);
    List<Question> selectRelated(Question question);
    Integer countBySearch(QuestionQueryDTO questionDTO);
    List<Question> selectBySearch(QuestionQueryDTO questionQueryDTO);
}
