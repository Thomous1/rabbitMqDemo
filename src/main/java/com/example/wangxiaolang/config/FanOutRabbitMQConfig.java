package com.example.wangxiaolang.config;

import javax.annotation.Resource;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 广播模式
 * @author wangzuoyu1
 * @description
 */
@Configuration
public class FanOutRabbitMQConfig {

    @Bean(name = RabbitMQConfig.RABBITMQ_DEMO_TOPIC_A)
    public Queue rabbitmqDemoFanoutQueueA() {
        /**
         * 1、name:    队列名称
         * 2、durable: 是否持久化
         * 3、exclusive: 是否独享、排外的。如果设置为true，定义为排他队列。则只有创建者可以使用此队列。也就是private私有的。
         * 4、autoDelete: 是否自动删除。也就是临时队列。当最后一个消费者断开连接后，会自动删除。
         * */
        return new Queue(RabbitMQConfig.RABBITMQ_DEMO_TOPIC_A, true, false, false);
    }

    @Bean(name = RabbitMQConfig.RABBITMQ_DEMO_TOPIC_B)
    public Queue rabbitmqDemoFanoutQueueB() {
        /**
         * 1、name:    队列名称
         * 2、durable: 是否持久化
         * 3、exclusive: 是否独享、排外的。如果设置为true，定义为排他队列。则只有创建者可以使用此队列。也就是private私有的。
         * 4、autoDelete: 是否自动删除。也就是临时队列。当最后一个消费者断开连接后，会自动删除。
         * */
        return new Queue(RabbitMQConfig.RABBITMQ_DEMO_TOPIC_B, true, false, false);
    }

    @Bean
    public FanoutExchange rabbitmqDemoFanoutExchange() {
        //Direct交换机
        return new FanoutExchange(RabbitMQConfig.RABBITMQ_DEMO_FANOUT_EXCHANGE, true, false);
    }

    @Bean
    public Binding bindFanOutA() {
        //链式写法，绑定交换机和队列，并设置匹配键
        return BindingBuilder
            //绑定队列
            .bind(rabbitmqDemoFanoutQueueA())
            //到交换机
            .to(rabbitmqDemoFanoutExchange());
    }

    @Bean
    public Binding bindFanOutB() {
        //链式写法，绑定交换机和队列，并设置匹配键
        return BindingBuilder
            //绑定队列
            .bind(rabbitmqDemoFanoutQueueB())
            //到交换机
            .to(rabbitmqDemoFanoutExchange());
    }
}
