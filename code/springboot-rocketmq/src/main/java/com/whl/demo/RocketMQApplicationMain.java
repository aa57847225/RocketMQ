package com.whl.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Date 18/2/5 16:39
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.whl.demo"})
public class RocketMQApplicationMain {

    public static void main(String[] args) {
        SpringApplication.run(RocketMQApplicationMain.class, args);
    }

}
