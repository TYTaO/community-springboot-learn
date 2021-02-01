package com.tytao.community.controller;

import com.tytao.community.dto.CommentDTO;
import com.tytao.community.dto.QuestionDTO;
import com.tytao.community.enums.CommentTypeEnum;
import com.tytao.community.exception.CustomizeErrorCode;
import com.tytao.community.exception.CustomizeException;
import com.tytao.community.model.Question;
import com.tytao.community.service.CommentService;
import com.tytao.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") String id,
                           Model model){
        Long questionId = null;
        try {
            questionId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new CustomizeException(CustomizeErrorCode.INVALID_INPUT);
        }
        QuestionDTO questionDTO = questionService.getById(questionId);
        List<Question> relatedQuestions = questionService.selectRelated(questionDTO);
        List<CommentDTO> commentDTOList =commentService.selectByParentId(questionId, CommentTypeEnum.QUESTION);

        // 累加阅读数
        questionService.incView(questionId);
        model.addAttribute("question", questionDTO);
        model.addAttribute("comments", commentDTOList);
        model.addAttribute("relatedQuestions", relatedQuestions);
        return "question";
    }
}
