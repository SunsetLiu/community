package com.nowcoder.community.service;

import com.nowcoder.community.dao.AlphaDao;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Date;

/**
 * 演示的业务层
 */
@Service
//@Scope("prototype") //默认参数是单例singleton
public class AlphaService {

    @Autowired
    private AlphaDao alphaDao;

    @Autowired
    private UserService userService;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    public AlphaService(){
        System.out.println("实例化AlphaService");
    }

    @PostConstruct
    public void init(){
        System.out.println("初始化AlphaService");
    }

    @PreDestroy
    public void destroy(){
        System.out.println("销毁AlphaService");
    }

    public String find(){
        return alphaDao.select();
    }

    /**
     * 测试事务，声明式
     */
    //Propagation.REQUIRED:支持当前事物（外部事物），如果不存在则创建新的事物
    //Propagation.REQUIRES_NEW:创建一个新事物，并且暂停当前事物
    //Propagation.NESTED:如果当前存在事物（外部事物），则嵌套在该事物中执行（独立的提交和回滚），否则就会REQUIRED一样
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void save1(){
        //新增用户
        User user = new User();
        user.setCreateTime(new Date());
        user.setHeaderUrl("http://static.nowcoder.com/images/head/notify.png");
        user.setStatus(0);
        user.setType(0);
        user.setSalt(CommunityUtil.generateUUID().substring(0,5));
        user.setPassword(CommunityUtil.md5("123"+user.getSalt()));
        user.setEmail("2957450566@qq.com");
        user.setUsername("哈哈");
        userService.register(user);
        //新增贴子
        DiscussPost discussPost = new DiscussPost();
        discussPost.setUserId(user.getId());
        discussPost.setCreateTime(new Date());
        discussPost.setContent("欢迎您！");
        discussPost.setTitle("新人贴");
        discussPostService.addDiscussPost(discussPost);
        //制造报错
        Integer.valueOf("嗯哼");
    }

    /**
     * 测试事务，编程式
     */
    public Object save2(){
        //事务隔离级别
        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        //传播机制
        transactionTemplate.setPropagationBehavior(TransactionTemplate.PROPAGATION_REQUIRED);

        return transactionTemplate.execute(new TransactionCallback<Object>(){

            @Override
            public Object doInTransaction(TransactionStatus status) {
                //新增用户
                User user = new User();
                user.setCreateTime(new Date());
                user.setHeaderUrl("http://static.nowcoder.com/images/head/notify1.png");
                user.setStatus(0);
                user.setType(0);
                user.setSalt(CommunityUtil.generateUUID().substring(0,5));
                user.setPassword(CommunityUtil.md5("123456"+user.getSalt()));
                user.setEmail("2957450566@qq.com");
                user.setUsername("哈哈h");
                userService.register(user);
                //新增贴子
                DiscussPost discussPost = new DiscussPost();
                discussPost.setUserId(user.getId());
                discussPost.setCreateTime(new Date());
                discussPost.setContent("欢迎您ha！");
                discussPost.setTitle("新人贴ha");
                discussPostService.addDiscussPost(discussPost);
                //制造报错
                Integer.valueOf("嗯哼");
                return "ok";
            }
        });
    }
}
