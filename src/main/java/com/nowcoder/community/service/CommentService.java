package com.nowcoder.community.service;

import com.nowcoder.community.dao.CommentMapper;
import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.util.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class CommentService implements CommunityConstant {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private DiscussPostService discussPostService;

    /**
     * 分页查询评论
     * @param entityId 评论id
     * @param entityType 评论类型
     * @param offset 分页
     * @param limit 分页
     * @return
     */
    public List<Comment> findCommentsByEntity(int entityId, int entityType, int offset, int limit){
        return commentMapper.selectCommentsByEntity(entityId, entityType, offset, limit);
    }

    /**
     * 查询评论数
     * @param entityId 评论id
     * @param entityType 评论类型
     * @return
     */
    public int findCountByEntity(int entityId, int entityType){
        return commentMapper.selectCountByEntity(entityId, entityType);
    }

    /**
     * 添加评论
     * @param comment
     * @return
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int addComment(Comment comment){
        if(comment == null){
            throw new IllegalArgumentException("参数不能为空");
        }

        //添加评论
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        // 占位。正常这里应该加敏感词过滤的
        int rows = commentMapper.insertComment(comment);

        //更新贴子的评论数
        if(comment.getEntityType() == ENTITY_TYPE_POST){
            int commentCount = commentMapper.selectCountByEntity(comment.getEntityId(), comment.getEntityType());
            discussPostService.updateCommentCount(comment.getEntityId(), commentCount);
        }
        return rows;
    }
}
