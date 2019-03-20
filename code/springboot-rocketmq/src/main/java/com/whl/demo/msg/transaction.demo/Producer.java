package com.whl.demo.msg.transaction.demo;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionCheckListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;

/**
 * @program: springboot-rocketmq
 * @description:
 * @author: Mr.Wang
 * @create: 2019-03-20 11:44
 **/
public class Producer {

    public static void main(String[] args) throws MQClientException, InterruptedException {

        TransactionCheckListener transactionCheckListener = new TransactionCheckListenerImpl();
        TransactionMQProducer producer = new TransactionMQProducer("transaction_Producer");
        producer.setNamesrvAddr("192.168.100.145:9876;192.168.100.146:9876;192.168.100.149:9876;192.168.100.239:9876");
        // 事务回查最小并发数
        producer.setCheckThreadPoolMinSize(2);
        // 事务回查最大并发数
        producer.setCheckThreadPoolMaxSize(2);
        // 队列数
        producer.setCheckRequestHoldMax(2000);
        producer.setTransactionCheckListener(transactionCheckListener);
        producer.start();

        // String[] tags = new String[] { "TagA", "TagB", "TagC", "TagD", "TagE"
        // };
        TransactionExecuterImpl tranExecuter = new TransactionExecuterImpl();
        for (int i = 1; i <= 2; i++) {
            try {
                Message msg = new Message("TopicTransactionTest", "transaction" + i, "KEY" + i,
                        ("Hello RocketMQ " + i).getBytes());
                SendResult sendResult = producer.sendMessageInTransaction(msg, tranExecuter, null);
                System.out.println(sendResult);

                Thread.sleep(10);
            } catch (MQClientException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < 100000; i++) {
            Thread.sleep(1000);
        }

        producer.shutdown();

    }

}
