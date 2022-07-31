package com.nowcoder.community;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * BlockingQueue的测试类
 */
public class BlockingQueueTest {

    public static void main(String[] args) {
        BlockingQueue queue = new ArrayBlockingQueue(10);
        new Thread(new Producer(queue)).start();
        new Thread(new Consumer(queue)).start();
        new Thread(new Consumer(queue)).start();
        new Thread(new Consumer(queue)).start();
    }
}

/**
 * 测试BlockingQueue的生产者
 */
class Producer implements Runnable{
    private BlockingQueue<Integer> queue;

    public Producer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            for(int i = 0;i < 100; i++){
                Thread.sleep(20);
                queue.put(i);
                System.out.println(Thread.currentThread().getName() + "生产的队列长度：" + queue.size());
            }

        }catch (Exception e){
            e.getStackTrace();
        }
    }
}

/**
 * 测试BlockingQueue的消费者
 */
class Consumer implements Runnable{
    private BlockingQueue<Integer> queue;
    public Consumer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try{
            while (true){
                Thread.sleep(new Random().nextInt(1000));
                        queue.take();
                        System.out.println(Thread.currentThread().getName() + "消费后队列长度：" + queue.size());
                        }
                        }catch (Exception e){
                        e.getStackTrace();
                        }
                        }
                        }