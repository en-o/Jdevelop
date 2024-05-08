package cn.jdevelops.delay.core.service;

import cn.jdevelops.delay.core.entity.DelayQueueMessage;

import java.util.List;

/**
 * jdk延时队列  没有默认实现
 *  优点： 效率高，低延迟
 *  缺点： 宕机丢失（可以存数据库），不利于集群扩展
 * @author tnnn
 * @version V1.0
 * @date 2023-01-05 16:33
 */
public interface DelayService<T extends DelayQueueMessage> {

    /**
     * 生产延迟队列消息
     * @param delayMessage 消息
     */
    void produce(T delayMessage);

    /**
     * 生产延迟队列消息
     * @param delayMessage 消息
     */
    void produce(List<T> delayMessage);

    /**
     * 取消队列
     * @param delayMessage 消息
     */
    void cancel(String delayMessage);

    /**
     * 取消队列
     * @param delayMessage 消息
     */
    void cancel(T delayMessage);


    /**
     * 消费延迟队列数据
     */
    void consumeDelay();
}
