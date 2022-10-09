package com.example.wangxiaolang.service.impl;

import com.example.wangxiaolang.config.RabbitMQConfig;
import com.example.wangxiaolang.config.RabbitTemplateConfig;
import com.example.wangxiaolang.service.RabbitMQService;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * RabbitMQService 实现类
 *
 * @author wangzuoyu1
 * @description
 */
@Service
public class RabbitMQServiceImpl implements RabbitMQService {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private RabbitTemplateConfig rabbitTemplateConfig;

    @Override
    public void sendMessage(String message) {
        String msgId = UUID.randomUUID().toString().replace("-", "").substring(0, 32);
        String sendTime = sdf.format(new Date());
        Map<String, Object> map = new HashMap<>();
        map.put("msgId", msgId);
        map.put("sendTime", sendTime);
        map.put("msg", message);
        // direct 模式发送消息，直接指定routing key 到指定的
        //rabbitTemplate.convertAndSend(RabbitMQConfig.RABBITMQ_DEMO_DIRECT_EXCHANGE, RabbitMQConfig.RABBITMQ_DEMO_DIRECT_ROUTING, map);
        // fanout 广播模式，只指定exchange
        rabbitTemplate.convertAndSend(RabbitMQConfig.RABBITMQ_DEMO_FANOUT_EXCHANGE, "", map);
    }

    @Override
    public void sendMessageSync(String msg) {
        String msgId = UUID.randomUUID().toString().replace("-", "").substring(0, 32);
        String sendTime = sdf.format(new Date());
        Map<String, Object> map = new HashMap<>();
        map.put("msgId", msgId);
        map.put("sendTime", sendTime);
        map.put("msg", msg);
        // direct 模式发送消息，直接指定routing key 到指定的
        //rabbitTemplate.convertAndSend(RabbitMQConfig.RABBITMQ_DEMO_DIRECT_EXCHANGE, RabbitMQConfig.RABBITMQ_DEMO_DIRECT_ROUTING, map);
        // fanout 广播模式，只指定exchange
        rabbitTemplateConfig.sendMsg(RabbitMQConfig.RABBITMQ_DEMO_FANOUT_EXCHANGE, "", map);
    }
}
