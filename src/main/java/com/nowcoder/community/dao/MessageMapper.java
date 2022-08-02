package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 消息的数据层
 */
@Mapper
public interface MessageMapper {

    //新增消息
    int insertMessage(Message message);

    //查询某个主题下，最新的通知消息
    Message selectLatestNotice(int toId,String topic);

    //查询某个主题下，通知信息的数量
    int selectNoticeCount(int toId,String topic);

    //查询某个主题下，未数的数量
    int selectNoticeUnreadCount(int toId,String topic);

    //已读状态修改
    int updateStatus(List<Integer> ids, int status);

    //查询某个主题下，系统信息的详细内容
    List<Message> selectNotices(int toId, String topic, int offset, int limit);

}
