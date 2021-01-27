package com.tytao.community.service;

import com.tytao.community.dto.PaginationDTO;
import com.tytao.community.dto.QuestionDTO;
import com.tytao.community.mapper.QuestionMapper;
import com.tytao.community.mapper.UserMapper;
import com.tytao.community.model.Question;
import com.tytao.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionMapper questionMapper;

    public PaginationDTO list(Integer page, Integer size) {
        Integer totalCount = questionMapper.count();
        PaginationDTO paginationDTO = new PaginationDTO();
        paginationDTO.setPagination(totalCount, page, size);
        if (page <1){
            page = 1;
        }
        if (page > paginationDTO.getTotalPage()){
            page = paginationDTO.getTotalPage();
        }
        Integer offset = size * (page - 1);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        List<Question> questionList = questionMapper.list(offset, size);
        for (Question question : questionList) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOList);
        return paginationDTO;
    }

    public PaginationDTO list(Integer userId, Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = questionMapper.countByUserId(userId);
        paginationDTO.setPagination(totalCount, page, size);
        if (page > paginationDTO.getTotalPage()){
            page = paginationDTO.getTotalPage();
        }
        if (page <1){
            page = 1;
        }
        Integer offset = size * (page - 1);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        List<Question> questionList = questionMapper.listByUserId(userId, offset, size);
        for (Question question : questionList) {
            User user = userMapper.selectByPrimaryKey(question.getId());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOList);
        return paginationDTO;
    }

    public QuestionDTO getById(Integer id) {
        Question question = questionMapper.getById(id);
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if (question.getId() == null){
            // 插入
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setCommentCount(0);
            question.setLikeCount(0);
            question.setViewCount(0);
            questionMapper.create(question);
        } else {
            // 更新
            Question dbQuestion = questionMapper.getById(question.getId());
            question.setGmtModified(System.currentTimeMillis());
            question.setCommentCount(dbQuestion.getCommentCount());
            question.setLikeCount(dbQuestion.getLikeCount());
            question.setViewCount(dbQuestion.getViewCount());
            questionMapper.update(question);
        }
    }
}
