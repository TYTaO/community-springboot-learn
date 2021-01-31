package com.tytao.community.controller;

import com.tytao.community.dto.CommentCreateDTO;
import com.tytao.community.dto.ResultDTO;
import com.tytao.community.exception.CustomizeErrorCode;
import com.tytao.community.mapper.CommentMapper;
import com.tytao.community.model.Comment;
import com.tytao.community.model.User;
import com.tytao.community.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CommentController {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private CommentService commentService;

    @ResponseBody
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO,
                       HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResultDTO.errorOf(CustomizeErrorCode.NOT_LOGIN);
        }
        Comment newComment = new Comment();
        newComment.setContent(commentCreateDTO.getContent());
        newComment.setParentId(commentCreateDTO.getParentId());
        newComment.setType(commentCreateDTO.getType());
        newComment.setCommentator(user.getId());//先这样
        newComment.setCommentCount((long) 0);
        newComment.setLikeCount((long) 0);
        newComment.setGmtCreate(System.currentTimeMillis());
        newComment.setGmtModified(newComment.getGmtCreate());
        commentService.insert(newComment);
        return ResultDTO.okOf();
    }
}
