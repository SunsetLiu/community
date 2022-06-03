package com.nowcoder.community.dao;

import com.nowcoder.community.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
//这里添加这个，不然其他地方注入会飘红，虽然说不影响运行，但是不舒服
//@Component("nameSpance")
public interface UserMapper {
    User selectById(int id);

    User selectByName(String name);

    User selectByEmail(String email);

    int insertUser(User user);

    int updateStatus(int id , int status);

    int updateHeader(int id , String headerUrl);

    int updatePassword(int id , String password);
}
