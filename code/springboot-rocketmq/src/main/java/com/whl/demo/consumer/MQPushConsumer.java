package com.whl.demo.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.whl.demo.entity.Student;
import com.whl.demo.mapper.StudentMapper;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.List;

@Component
public class MQPushConsumer implements MessageListenerConcurrently {

    private static final Logger LOGGER = LoggerFactory.getLogger(MQPushConsumer.class);

    @Value("${spring.rocketmq.namesrvAddr}")
    private String namesrvAddr;

    private final DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("TestRocketMQPushConsumer");


    @Resource
    private StudentMapper studentMapper;

    /**
     * 初始化
     *
     * @throws MQClientException
     */
    @PostConstruct
    public void start() {
        try {
            LOGGER.info("MQ：启动消费者");

            consumer.setNamesrvAddr(namesrvAddr);
            // 从消息队列头开始消费
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            // 集群消费模式
            consumer.setMessageModel(MessageModel.CLUSTERING);
            // 订阅主题 多个
            consumer.subscribe("TopicTest", "*");
            consumer.subscribe("student","*");
            // 注册消息监听器
            consumer.registerMessageListener(this);
            // 启动消费端
            consumer.start();
        } catch (MQClientException e) {
            LOGGER.error("MQ：启动消费者失败：{}-{}", e.getResponseCode(), e.getErrorMessage());
            throw new RuntimeException(e.getMessage(), e);
        }

    }

    /**
     * 消费消息
     * @param msgs
     * @param context
     * @return
     */
    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        int index = 0;
        try {
            for (; index < msgs.size(); index++) {
                MessageExt msg = msgs.get(index);
                // int reconsume = msg.getReconsumeTimes();
//                if(reconsume ==3){//消息已经重试了3次，如果不需要再次消费，则返回成功
//                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
//                }

                String messageBody = new String(msg.getBody(), RemotingHelper.DEFAULT_CHARSET);

                LOGGER.info("MQ：消费者接收新信息: {} {} {} {} {}", msg.getMsgId(), msg.getTopic(), msg.getTags(), msg.getKeys(), messageBody);

                if("student".equals(msg.getTags())){
                    Student student = JSONObject.parseObject(messageBody,Student.class);
                    Student s = studentMapper.findById(student.getsNo());
                    LOGGER.info("========student========"+ JSON.toJSONString(s));
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            if (index < msgs.size()) {
                context.setAckIndex(index + 1);
            }
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

    @PreDestroy
    public void stop() {
        if (consumer != null) {
            consumer.shutdown();
            LOGGER.error("MQ：关闭消费者");
        }
    }

}
