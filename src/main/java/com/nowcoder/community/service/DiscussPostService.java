package com.nowcoder.community.service;

import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.entity.DiscussPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * 社区的业务层
 */
@Service
public class DiscussPostService {
    @Autowired
    private DiscussPostMapper discussPostMapper;

    /**
     * 查询所有贴子
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    public List<DiscussPost> selectDiscussPosts(int userId , int offset , int limit){
        return discussPostMapper.selectDiscussPosts(userId,offset,limit);
    }

    /**
     * 查询贴子数量
     * @param userId
     * @return
     */
    public int selectDiscussPostRows(int userId){
        return discussPostMapper.selectDiscussPostRows(userId);
    }

    /**
     * 新增贴子
     * @param discussPost
     * @return
     */
    public int addDiscussPost(DiscussPost discussPost){
        if(discussPost == null){
            throw new IllegalArgumentException("贴子不能为空");
        }
        // 转义HTML标记
        discussPost.setTitle(HtmlUtils.htmlEscape(discussPost.getTitle()));
        discussPost.setContent(HtmlUtils.htmlEscape(discussPost.getContent()));
        //过滤敏感词--由于还没开发，这步先不执行
        return discussPostMapper.insertDiscussPost(discussPost);
    }

    /**
     * 根据Id查询贴子详情
     * @param id
     * @return
     */
    public DiscussPost selectDiscussPostById(int id){
        return discussPostMapper.selectDiscussPostById(id);
    }

    /**
     * 更新贴子的评论数量
     * @param id
     * @param commentCount
     * @return
     */
    public int updateCommentCount(int id, int commentCount){
        return discussPostMapper.updateCommentCount(id, commentCount);
    }
}
