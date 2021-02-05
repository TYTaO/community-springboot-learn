package com.tytao.community.service;

import com.tytao.community.dto.NotificationDTO;
import com.tytao.community.dto.PaginationDTO;
import com.tytao.community.enums.CommentTypeEnum;
import com.tytao.community.enums.NotificationStatusEnum;
import com.tytao.community.enums.NotificationTypeEnum;
import com.tytao.community.exception.CustomizeErrorCode;
import com.tytao.community.exception.CustomizeException;
import com.tytao.community.mapper.CommentMapper;
import com.tytao.community.mapper.NotificationMapper;
import com.tytao.community.mapper.QuestionMapper;
import com.tytao.community.model.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private CommentMapper commentMapper;

    public PaginationDTO<NotificationDTO> list(Long id, Integer page, Integer size) {
        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO();
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andReceiverIdEqualTo(id);
        Integer totalCount = (int)notificationMapper.countByExample(notificationExample);
        paginationDTO.setPagination(totalCount, page, size);
        if (page > paginationDTO.getTotalPage()){
            page = paginationDTO.getTotalPage();
        }
        if (page <1){
            page = 1;
        }
        Integer offset = size * (page - 1);
        List<NotificationDTO> notificationDTOs = new ArrayList<>();
        notificationExample.setOrderByClause("gmt_create desc");
        List<Notification> notificationList = notificationMapper.selectByExampleWithRowbounds(notificationExample, new RowBounds(offset, size));
        for (Notification notification : notificationList) {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification, notificationDTO);
            notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
            notificationDTOs.add(notificationDTO);
        }
        paginationDTO.setData(notificationDTOs);
        return paginationDTO;
    }

    public Long unreadCount(Long userId) {
        NotificationExample example = new NotificationExample();
        example.createCriteria().andReceiverIdEqualTo(userId)
        .andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        return notificationMapper.countByExample(example);
    }

    public Notification read(Long notificationId, User user) {
        Notification notification = notificationMapper.selectByPrimaryKey(notificationId);
        if (notification == null){
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if (!Objects.equals(notification.getReceiverId(), user.getId())){
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);
        return notification;
    }

    // 创建回复通知
    public void createCommentNotify(Comment comment, Long receiverId, String notifierName, String outerTitle, NotificationTypeEnum replyCommentType) {
        if (comment.getCommentator() == receiverId){
            return;
        }
        Notification notification = new Notification();
        // outerId需要存储问题的id
        if (comment.getType() == CommentTypeEnum.QUESTION.getType()){
            notification.setOuterId(comment.getParentId());
        } else { // 回复的问题
            Comment commentParent = commentMapper.selectByPrimaryKey(comment.getParentId());
            notification.setOuterId(commentParent.getParentId());
        }
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setNotifierId(comment.getCommentator());
        notification.setReceiverId(receiverId);
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setType(replyCommentType.getType());
        notification.setNotifierName(notifierName);
        notification.setOuterTitle(outerTitle);
        notificationMapper.insert(notification);
    }

    public void createLikeNotify(Thumb thumb, Long receiverId, String notifierName, String outerTitle, Long questionId){
        if (thumb.getCreator() == receiverId){
            return;
        }
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setReceiverId(receiverId);
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setType(NotificationTypeEnum.Like_Comment.getType());
        notification.setNotifierName(notifierName);
        notification.setOuterTitle(outerTitle);
        notification.setOuterId(questionId);
        notification.setNotifierId(thumb.getCreator());
        notificationMapper.insert(notification);
    }
}
