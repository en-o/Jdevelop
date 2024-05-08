package cn.jdevelops.delay.jdk;

import cn.jdevelops.delay.core.factory.DelayFactory;
import cn.jdevelops.delay.core.service.DelayService;
import cn.jdevelops.delay.jdk.constant.DelayQueueConstant;
import cn.jdevelops.delay.jdk.task.DelayTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * jdk延时队列执行方法
 * @author tnnn
 * @version V1.0
 * @date 2023-01-05 16:34
 */
@ConditionalOnMissingBean(DelayService.class)
@AutoConfiguration
public class JdkDelayService implements DelayService<DelayTask> {

    private static final Logger logger = LoggerFactory.getLogger(JdkDelayService.class);
    /**
     * 线程池
     */
    private static final String NAME = "JdkDelayMessageTask-thread-";
    private final AtomicInteger seq = new AtomicInteger(1);

    /**
     * 参考 <a href="https://tobebetterjavaer.com/thread/ScheduledThreadPoolExecutor.html#schedule">...</a>
     */
    private final ScheduledThreadPoolExecutor pool = new ScheduledThreadPoolExecutor(1, r ->
            new Thread(r, NAME + seq.getAndIncrement()));

    /**
     * 执行工厂
     */
    @Resource
    DelayFactory<DelayTask>  delayRunFactory;

    @Override
    public void produce(DelayTask delayMessage) {
        // 无法去重，管他的，执行方法时在sql语句加上添加吧
        DelayQueueConstant.DELAY_QUEUE.put(delayMessage);
    }

    @Override
    public void produce(List<DelayTask> delayMessage) {
        // 定义一个延时队列
        DelayQueue<DelayTask> queue = DelayQueueConstant.DELAY_QUEUE;
        // 无法去重，管他的，执行方法时在sql语句加上添加吧
        delayMessage.forEach(queue::put);
    }

    @Override
    public void cancel(String delayMessage) {
        logger.warn("===> jdk delay 暂不支持取消操作");
    }

    @Override
    public void cancel(DelayTask delayMessage) {
        logger.warn("===> jdk delay 暂不支持取消操作");
    }


    @Override
    public void consumeDelay() {
        // IllegalArgumentException 的话 initialDelay = 1， period = 1 直接写死
        long initialDelay =Math.round(Math.random()*10+10);
        long periodRound = Math.round(Math.random() * 10);
        long period = periodRound==0?1:periodRound;
        logger.info("开始消费jdk延时队列数据...");
        // 在initialDelay时长后第一次执行任务，以后每隔period时长，再次执行任务
        // 注意，固定速率和固定时延，传入的参数都是Runnable，也就是说这种定时任务是没有返回值的
        pool.scheduleAtFixedRate(()->{
            try {
                DelayQueue<DelayTask> queue  = DelayQueueConstant.DELAY_QUEUE;
                if (!queue.isEmpty()){
                    DelayTask delayTask = queue.take();
                    delayRunFactory.delayExecute(delayTask);
                }
            }catch (Exception e){
                logger.error("execute function error..",e);
            }
        }, initialDelay,period, TimeUnit.SECONDS);
    }
}
