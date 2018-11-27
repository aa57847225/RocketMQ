package com.whl.demo.controller;

import com.whl.demo.config.MQProducer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("test")
public class TestController {

    @Resource
    private MQProducer mqProducer;

    @PostMapping(value = "/send")
    public String sendMsg(){
        mqProducer.sendMessage("浮生是个水B","TopicTest","dafu","");
        return null;
    }



}
