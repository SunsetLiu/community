package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Message;
import org.apache.ibatis.annotations.Mapper;

/**
 * 消息的数据层
 */
@Mapper
public interface MessageMapper {

    //新增消息
    int insertMessage(Message message);

}
