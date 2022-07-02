package com.nowcoder.community.Service;

import com.nowcoder.community.dao.CommentMapper;
import com.nowcoder.community.entity.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

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
}
