package com.example.wangxiaolang.controller;

import com.example.wangxiaolang.service.RabbitMQService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * rabbitController
 *
 * @author wangzuoyu1
 * @description
 */

@RestController
public class RabbitController {

    @Autowired
    private RabbitMQService rabbitMQService;

    /**
     * 发送消息
     * @author java技术爱好者
     */
    @GetMapping("/sendMsg")
    public String sendMsg(@RequestParam(name = "msg") String msg) throws Exception {
        try {
            rabbitMQService.sendMessage(msg);
            return "ok";
        }catch (Exception e) {
            return "error";
        }
    }

}
