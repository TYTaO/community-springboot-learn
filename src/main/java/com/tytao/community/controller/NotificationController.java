package com.tytao.community.controller;

import com.tytao.community.dto.CommentDTO;
import com.tytao.community.dto.NotificationDTO;
import com.tytao.community.dto.QuestionDTO;
import com.tytao.community.enums.CommentTypeEnum;
import com.tytao.community.enums.NotificationTypeEnum;
import com.tytao.community.exception.CustomizeErrorCode;
import com.tytao.community.exception.CustomizeException;
import com.tytao.community.model.Notification;
import com.tytao.community.model.Question;
import com.tytao.community.model.User;
import com.tytao.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/notification/{id}")
    public String notification(@PathVariable(name = "id") Long id,
                               HttpServletRequest request){
        User user =(User)request.getSession().getAttribute("user");
        if (user == null){
            return"redirect:/";
        }
        Notification notification = notificationService.read(id, user);
        if (NotificationTypeEnum.REPLY_COMMENT.getType() == notification.getType()
                || NotificationTypeEnum.REPLY_QUESTION.getType() == notification.getType()) {
            return "redirect:/question/" + notification.getOuterId();
        } else {
            return "redirect:/";
        }

    }
}
