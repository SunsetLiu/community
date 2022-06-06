package com.nowcoder.community.dao;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

/**
 * 演示手动注入Bean的实现类
 */
@Repository
@Primary
public class AlphaDaoMyBaitsImpl implements AlphaDao{
    @Override
    public String select() {
        return "AlphaDaoMyBaitsImpl";
    }
}
