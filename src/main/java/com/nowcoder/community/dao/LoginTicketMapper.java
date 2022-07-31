package com.nowcoder.community.dao;

import com.nowcoder.community.entity.LoginTicket;
import org.apache.ibatis.annotations.*;

/**
 * 注册的Mapping
 */
@Mapper
/**
 * 由于LoginTicketMapper的数据从数据库存储改为放在redis，所以采用注解@Deprecated将这个类不推荐使用
 */
@Deprecated
public interface LoginTicketMapper {

    @Insert({
            "insert into login_ticket (user_id, ticket, status, expired) "+
             "values(#{userId}, #{ticket}, #{status}, #{expired})"
    })
    @Options(useGeneratedKeys=true, keyProperty="id")
    int insertLoginTicket(LoginTicket loginTicket);


    @Update({
            "<script>" +
            "update login_ticket set status = #{status} " +
            "where ticket = #{ticket} " +
            "<if test=\"ticket != null\">" +
            "and 1 = 1 " +
            "</if> " +
            "</script>"
    })
    int updateStatus(String ticket, int status);


    @Select({
            "select id, user_id, ticket, status, expired from login_ticket " +
            "where ticket = #{ticket}  "
    })
    LoginTicket selectByTicket(String ticket);
}
