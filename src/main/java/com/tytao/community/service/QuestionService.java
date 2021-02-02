package com.tytao.community.service;

import com.tytao.community.dto.PaginationDTO;
import com.tytao.community.dto.QuestionDTO;
import com.tytao.community.dto.QuestionQueryDTO;
import com.tytao.community.exception.CustomizeErrorCode;
import com.tytao.community.exception.CustomizeException;
import com.tytao.community.mapper.QuestionExtMapper;
import com.tytao.community.mapper.QuestionMapper;
import com.tytao.community.mapper.UserMapper;
import com.tytao.community.model.Question;
import com.tytao.community.model.QuestionExample;
import com.tytao.community.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionExtMapper questionExtMapper;

    public PaginationDTO<QuestionDTO> list(String search, Integer page, Integer size) {

        String regexSearch = null;
        if (StringUtils.isNotBlank(search)){
            String[] tags = StringUtils.split(search, " ");
            // 转成正则用的表达式
            regexSearch = Arrays.stream(tags).collect(Collectors.joining("|"));
        }
        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setSearch(regexSearch);
        Integer totalCount = (int)questionExtMapper.countBySearch(questionQueryDTO);
        PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO();
        paginationDTO.setPagination(totalCount, page, size);
        if (page <1){
            page = 1;
        }
        if (page > paginationDTO.getTotalPage()){
            page = paginationDTO.getTotalPage();
        }
        Integer offset = page < 1 ? 0 : size * (page - 1);
        questionQueryDTO.setPage(offset);
        questionQueryDTO.setSize(size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        List<Question> questionList = questionExtMapper.selectBySearch(questionQueryDTO);
        for (Question question : questionList) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setData(questionDTOList);
        return paginationDTO;
    }

    public PaginationDTO list(Long userId, Integer page, Integer size) {
        PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO();
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(userId);
        Integer totalCount = (int)questionMapper.countByExample(questionExample);
        paginationDTO.setPagination(totalCount, page, size);
        if (page > paginationDTO.getTotalPage()){
            page = paginationDTO.getTotalPage();
        }
        if (page <1){
            page = 1;
        }
        Integer offset = size * (page - 1);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        QuestionExample questionExampleById = new QuestionExample();
        questionExampleById.createCriteria().andCreatorEqualTo(userId);
        List<Question> questionList = questionMapper.selectByExampleWithBLOBsWithRowbounds(questionExampleById, new RowBounds(offset, size));
        for (Question question : questionList) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setData(questionDTOList);
        return paginationDTO;
    }

    public QuestionDTO getById(Long id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if (question == null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
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
            questionMapper.insert(question);
        } else {
            // 更新
            Question questionUpdate = new Question();
            questionUpdate.setGmtModified(System.currentTimeMillis());
            questionUpdate.setDescription(question.getDescription());
            questionUpdate.setTag(question.getTag());
            questionUpdate.setTitle(question.getTitle());
            QuestionExample example = new QuestionExample();
            example.createCriteria().andIdEqualTo(question.getId());
            int updateRes = questionMapper.updateByExampleSelective(questionUpdate, example);
            if (updateRes != 1){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }


        }
    }

    public void incView(Long id) {
        Question updateQuestion = new Question();
        updateQuestion.setViewCount(1);
        updateQuestion.setId(id);
        QuestionExample example = new QuestionExample();
        example.createCriteria().andIdEqualTo(id);
        questionExtMapper.incView(updateQuestion);
    }

    public List<Question> selectRelated(QuestionDTO questionDTO) {
        if (StringUtils.isBlank(questionDTO.getTag())){
            return new ArrayList<>();
        }
        String[] tags = StringUtils.split(questionDTO.getTag(), '/');
        String regexpTag = Arrays.stream(tags).collect(Collectors.joining("|"));
        Question question = new Question();
        question.setId(questionDTO.getId());
        question.setTag(regexpTag);
        List<Question> questions = questionExtMapper.selectRelated(question);
        return questions;
    }
}
