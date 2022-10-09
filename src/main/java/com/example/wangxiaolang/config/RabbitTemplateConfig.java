package com.example.wangxiaolang.config;

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wangzuoyu1
 * @description
 */
@Component
public class RabbitTemplateConfig implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnCallback{

    private static final Logger log = LoggerFactory.getLogger(RabbitTemplateConfig.class.getName());

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitTemplateConfig(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        // 设置消息到达 exchange 时，要回调的方法，每个 RabbitTemplate 只支持一个 ConfirmCallback
        rabbitTemplate.setConfirmCallback(this);
        // 设置消息无法到达 queue 时，要回调的方法
        rabbitTemplate.setReturnCallback(this);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String msg) {
        if (ack) {
            log.info("消息投递成功,ID为: {}" + correlationData.getId());
            // todo 操作数据库，将 correlationId 这条消息状态改为投递成功
            return;
        }
        log.error("消息投递失败,ID为: " + correlationData.getId() + ",错误信息: " + msg);
    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        String correlationId = message.getMessageProperties().getHeaders().get("spring_returned_message_correlation").toString();

        log.error("没有找到对应队列，消息投递失败,ID为: {}, replyCode {} , replyText {}, exchange {} routingKey {}",
            correlationId, replyCode, replyText, exchange, routingKey);
    }

    /**
     * 发送消息
     *
     * @param exchange   交换机
     * @param routingKey 路由建
     * @param message    消息实体
     */
    public void sendMsg(String exchange, String routingKey, Object message) {
        // 构造包含消息的唯一id的对象，id 必须在该 channel 中始终唯一
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        log.info("ID为: {}", correlationData.getId());
        // todo 先将 message 入库，在将 message 的数据库ID 、message的消息id message的初始状态(发送中)等信息入库

        // 完成 数据落库，消息状态打标后，就可以安心发送 message
        rabbitTemplate.convertAndSend(exchange, routingKey, message, correlationData);

        try {
            log.info("发送消息的线程处于休眠状态， confirm 和 returnedMessage 方法依然处于异步监听状态");
            Thread.sleep(1000*15);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
