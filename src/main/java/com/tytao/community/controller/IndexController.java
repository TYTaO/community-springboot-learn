package com.tytao.community.controller;

import com.tytao.community.dto.PaginationDTO;
import com.tytao.community.mapper.UserMapper;
import com.tytao.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {


    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String hello(Model model,
                        @RequestParam(name = "page", defaultValue = "1") Integer page,
                        @RequestParam(name = "size", defaultValue = "5") Integer size){
        // 查数据并显示
        PaginationDTO pagination = questionService.list(page, size);
        model.addAttribute("pagination", pagination);
        return "index";/**/
    }


}
