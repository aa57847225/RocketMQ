package com.whl.demo.controller;

import com.alibaba.fastjson.JSON;
import com.whl.demo.config.MQProducer;
import com.whl.demo.entity.Student;
import com.whl.demo.mapper.StudentMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("student")
public class StudentController {

    @Resource
    private MQProducer mqProducer;

    @Resource
    private StudentMapper studentMapper;

    @PostMapping(value = "/insert")
    public String sendMsg(@RequestBody Student student){
        Integer row =  studentMapper.insert(student);
        System.out.println("================row=============="+row);
        System.out.println("================studentId=============="+student.getsNo());
        mqProducer.sendMessage(JSON.toJSONString(student),"student","student","");
        return null;
    }
}
