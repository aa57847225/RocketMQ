package com.whl.demo.msg.transaction.demo;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * @program: springboot-rocketmq
 * @description:
 * @author: Mr.Wang
 * @create: 2019-03-20 11:41
 **/
public class Consumer {

    public static void main(String[] args) throws InterruptedException, MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("transaction_Consumer");
        /**       consumer.setNamesrvAddr("192.168.100.145:9876;192.168.100.146:9876;192.168.100.149:9876;192.168.100.239:9876"); */
        consumer.setNamesrvAddr("192.168.0.69:9876");
        consumer.setConsumeMessageBatchMaxSize(10);
        /**
         * 设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费<br>
         * 如果非第一次启动，那么按照上次消费的位置继续消费
         */
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

        consumer.subscribe("TopicTransactionTest", "*");

        consumer.registerMessageListener(new MessageListenerConcurrently() {

            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {

                try {

                    for (MessageExt msg : msgs) {
                        System.out.println(msg + ",内容：" + new String(msg.getBody()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    // 重试
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
                // 成功
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        consumer.start();

        System.out.println("transaction_Consumer Started.");
    }

}
