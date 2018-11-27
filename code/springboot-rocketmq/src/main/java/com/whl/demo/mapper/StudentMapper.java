package com.whl.demo.mapper;

import com.whl.demo.entity.Student;

import java.util.List;

/**
 * @program: springboot-rocketmq
 * @description:
 * @author: Mr.Wang
 * @create: 2018-11-27 09:56
 **/
public interface StudentMapper {

    Integer insert(Student student);

    List<Student> findAll();

    Student findById(Integer sNo);
}
