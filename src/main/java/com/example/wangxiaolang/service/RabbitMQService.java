package com.example.wangxiaolang.service;

/**
 * RabbitMQService interface
 *
 * @author wangzuoyu1
 * @description
 */
public interface RabbitMQService {
    void sendMessage(String message);

    void sendMessageSync(String msg);
}
