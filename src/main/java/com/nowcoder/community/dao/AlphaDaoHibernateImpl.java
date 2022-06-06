package com.nowcoder.community.dao;

import org.springframework.stereotype.Repository;

/**
 * 演示手动注入Bean的实现类
 */
@Repository("alphaHibernate")
public class AlphaDaoHibernateImpl implements AlphaDao{
    @Override
    public String select() {
        return "AlphaDaoHibernateImpl";
    }
}
