package com.tytao.community.controller;


import com.tytao.community.enums.NotificationTypeEnum;
import com.tytao.community.mapper.*;
import com.tytao.community.model.Comment;
import com.tytao.community.model.Thumb;
import com.tytao.community.model.ThumbExample;
import com.tytao.community.model.User;
import com.tytao.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ThumbController {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private ThumbMapper thumbMapper;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private CommentExtMapper commentExtMapper;

    @Transactional
    @ResponseBody
    @RequestMapping(value = "/thumb/{commentId}/{userId}/{questionId}", method = RequestMethod.GET)
    public String thumb(@PathVariable(name = "commentId" )Long commentId,
                        @PathVariable(name = "userId" )Long userId,
                        @PathVariable(name = "questionId" )Long questionId){
        ThumbExample thumbExample = new ThumbExample();
        thumbExample.createCriteria().andCreatorEqualTo(userId)
        .andParentidEqualTo(commentId);
        List<Thumb> thumbs = thumbMapper.selectByExample(thumbExample);
        Comment comment = commentMapper.selectByPrimaryKey(commentId);
        User likeUser = userMapper.selectByPrimaryKey(userId);
        Long likeCount = comment.getLikeCount();
        if (thumbs.size() != 0){
            // 已经点过赞了取消点赞
            thumbMapper.deleteByExample(thumbExample);
            comment.setLikeCount(1L);
            commentExtMapper.decLikeCount(comment);
            likeCount--;
        } else {
            // 点赞
            Thumb record = new Thumb();
            record.setCreator(userId);
            record.setParentid(commentId);
            thumbMapper.insert(record);
            comment.setLikeCount(1L);
            commentExtMapper.incLikeCount(comment);
            likeCount++;
            // 找到被点赞的用户
            User receiver = userMapper.selectByPrimaryKey(comment.getCommentator());
            notificationService.createLikeNotify(record, receiver.getId(), likeUser.getName(), comment.getContent(), questionId);
        }
        return ""+likeCount;
    }

}
