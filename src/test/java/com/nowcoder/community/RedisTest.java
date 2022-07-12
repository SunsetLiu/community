package com.nowcoder.community;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;

import java.util.concurrent.TimeUnit;

@SpringBootTest(classes = CommunityApplication.class)
public class RedisTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testStrings(){
        String redisKey  = "test:count";

        redisTemplate.opsForValue().set(redisKey,1);
        System.out.println(redisTemplate.opsForValue().increment(redisKey));
        System.out.println(redisTemplate.opsForValue().increment(redisKey));
        System.out.println(redisTemplate.opsForValue().decrement(redisKey));
    }

    @Test
    public void testHashes(){
        String redisKey  = "test:user";

        redisTemplate.opsForHash().put(redisKey, "id", 1);
        redisTemplate.opsForHash().put(redisKey, "username", "法外张三");
        System.out.println(redisTemplate.opsForHash().size(redisKey));
        System.out.println(redisTemplate.opsForHash().get(redisKey, "id"));
        System.out.println(redisTemplate.opsForHash().get(redisKey, "username"));
    }

    @Test
    public void testSets(){
        String redisKey = "test:ids";

        redisTemplate.opsForSet().add(redisKey, "aaa", "bbb", "ccc", "ddd");
        System.out.println(redisTemplate.opsForSet().pop(redisKey));
        System.out.println(redisTemplate.opsForSet().members(redisKey));
    }

    @Test
    public void testLists(){
        String redisKey = "test:teachers";

        redisTemplate.opsForList().leftPush(redisKey, "111");
        redisTemplate.opsForList().leftPush(redisKey, "222");
        redisTemplate.opsForList().leftPush(redisKey, "333");

        System.out.println(redisTemplate.opsForList().size(redisKey));
        System.out.println(redisTemplate.opsForList().index(redisKey,2));
    }

    @Test
    public void testSortedSets(){
        String redisKey = "test:students";

        redisTemplate.opsForZSet().add(redisKey, "唐僧", 80);
        redisTemplate.opsForZSet().add(redisKey, "悟空", 90);
        redisTemplate.opsForZSet().add(redisKey, "八戒", 50);
        redisTemplate.opsForZSet().add(redisKey, "沙僧", 70);
        redisTemplate.opsForZSet().add(redisKey, "白龙马", 60);

        System.out.println(redisTemplate.opsForZSet().zCard(redisKey));
        System.out.println(redisTemplate.opsForZSet().score(redisKey, "八戒"));
        System.out.println(redisTemplate.opsForZSet().reverseRank(redisKey, "八戒"));
        System.out.println(redisTemplate.opsForZSet().reverseRange(redisKey, 0, 2));
    }

    @Test
    public void testKeys(){
        System.out.println(redisTemplate.hasKey("test:students"));
        redisTemplate.delete("test:students");
        System.out.println(redisTemplate.hasKey("test:students"));

        redisTemplate.expire("test:ids",10, TimeUnit.SECONDS);
    }

    /**
     * 批量发送命令，节约网络开销
     */
    @Test
    public void testBoundOperations(){
        String redisKey = "test:count";
        BoundValueOperations operations = redisTemplate.boundValueOps(redisKey);
        operations.increment();
        operations.increment();
        operations.increment();
        operations.increment();
        System.out.println(operations.get());

    }

    /**
     * 编程式事务
     */
    @Test
    public void testTransaction(){
        String redisKey = "test:tx";
        Object execute = redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                //启动事务
                operations.multi();


                redisTemplate.opsForList().leftPush(redisKey,"111");
                redisTemplate.opsForList().leftPush(redisKey,"222");
                redisTemplate.opsForList().leftPush(redisKey,"333");
                //提交事务之前查询是无效的
                System.out.println(redisTemplate.opsForList().size(redisKey));
                //提交事务
                return operations.exec();
            }
        });
        System.out.println(redisTemplate.opsForList().size(redisKey));
        System.out.println(execute);
    }
}
