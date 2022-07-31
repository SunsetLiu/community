package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 评论的数据层
 */
@Mapper
public interface CommentMapper {
    List<Comment> selectCommentsByEntity(int entityId, int entityType, int offset, int limit);

    int selectCountByEntity(int entityId, int entityType);

    int insertComment(Comment comment);

    Comment selectCommentById(int Id);
}
