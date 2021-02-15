package com.tytao.community.controller;

import com.sun.deploy.net.HttpResponse;
import com.tytao.community.annotation.MetricTime;
import com.tytao.community.dto.PaginationDTO;
import com.tytao.community.mapper.UserMapper;
import com.tytao.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

@Controller
public class IndexController {


    @Autowired
    private QuestionService questionService;

    @MetricTime("hello")
    @GetMapping("/")
    public String hello(Model model,
                        @RequestParam(name = "page", defaultValue = "1") Integer page,
                        @RequestParam(name = "size", defaultValue = "5") Integer size,
                        @RequestParam(name = "search", required = false) String search
                        ){
        // 查数据并显示
        PaginationDTO pagination = questionService.list(search, page, size);
        model.addAttribute("pagination", pagination);
        model.addAttribute("search", search);
        return "index";/**/
    }
}
