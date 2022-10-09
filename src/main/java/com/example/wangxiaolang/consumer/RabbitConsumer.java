package com.example.wangxiaolang.consumer;

import com.example.wangxiaolang.config.RabbitMQConfig;
import java.util.Map;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author wangzuoyu1
 * @description
 */

//@RabbitListener(queues = {RabbitMQConfig.RABBITMQ_DEMO_TOPIC})
@Component
@RabbitListener(queuesToDeclare = @Queue(RabbitMQConfig.RABBITMQ_DEMO_TOPIC)) // 没有则创建
public class RabbitConsumer {

    @RabbitHandler
    public void consumer(Map map) {
        System.out.println("消费到的message：" + map.toString());
    }
}
