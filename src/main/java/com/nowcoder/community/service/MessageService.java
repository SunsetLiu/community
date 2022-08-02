package com.nowcoder.community.service;

import com.nowcoder.community.dao.MessageMapper;
import com.nowcoder.community.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * 消息的业务层
 */
@Service
public class MessageService {

    @Autowired
    private MessageMapper messageMapper;

    /**
     * 添加消息
     *
     * @param message
     * @return
     */
    public int addMessage(Message message) {
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        //这里还缺少敏感词过滤
        int row = messageMapper.insertMessage(message);
        return row;
    }

    /**
     * 查询某个主题下，最新的通知消息
     *
     * @param toId
     * @param topic
     * @return
     */
    public Message findLatestNotice(int toId, String topic) {
        return messageMapper.selectLatestNotice(toId, topic);
    }

    /**
     * 查询某个主题下，通知信息的数量
     * @param toId
     * @param topic
     * @return
     */
    public int findNoticeCount(int toId, String topic) {
        return messageMapper.selectNoticeCount(toId, topic);
    }

    /**
     * 查询某个主题下，未数的数量
     * @param toId
     * @param topic
     * @return
     */
    public int findNoticeUnreadCount(int toId,String topic){
        return messageMapper.selectNoticeUnreadCount(toId, topic);
    }

    /**
     * 阅读信息
     * @param ids
     * @return
     */
    public int readMessage(List<Integer> ids){
        return messageMapper.updateStatus(ids, 1);
    }

    /**
     * 查询某个主题下，系统信息的详细内容
     * @param toId
     * @param topic
     * @param offset
     * @param limit
     * @return
     */
    public List<Message> findNotices(int toId, String topic, int offset, int limit){
        return messageMapper.selectNotices(toId, topic, offset, limit);
    }
}