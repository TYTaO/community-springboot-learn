package com.tytao.community.controller;

import com.tytao.community.dto.CommentDTO;
import com.tytao.community.dto.QuestionDTO;
import com.tytao.community.mapper.QuestionMapper;
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
    public String question(@PathVariable(name = "id") Long id,
                           Model model){
        QuestionDTO questionDTO = questionService.getById(id);
        List<CommentDTO> commentDTOList =commentService.selectByQuestionId(id);

        // 累加阅读数
        questionService.incView(id);
        model.addAttribute("question", questionDTO);
        model.addAttribute("comments", commentDTOList);
        return "question";
    }
}
