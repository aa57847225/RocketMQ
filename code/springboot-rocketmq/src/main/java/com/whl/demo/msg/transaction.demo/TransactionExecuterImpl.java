package com.whl.demo.msg.transaction.demo;

import org.apache.rocketmq.client.producer.LocalTransactionExecuter;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.common.message.Message;

/**
 * @program: springboot-rocketmq
 * @description:
 * @author: Mr.Wang
 * @create: 2019-03-20 11:45
 **/
public class TransactionExecuterImpl implements LocalTransactionExecuter {
    // private AtomicInteger transactionIndex = new AtomicInteger(1);

    //DB操作 应该带上事务 service -> dao
    //如果数据操作失败  需要回滚    同时返回RocketMQ一个失败消息  意味着 消费者无法消费到这条失败的消息
    //如果成功 就要返回一个rocketMQ成功的消息，意味着消费者将读取到这条消息
    //o就是attachment

    @Override
    public LocalTransactionState executeLocalTransactionBranch(final Message msg, final Object arg) {

        System.out.println("执行本地事务msg = " + new String(msg.getBody()));

        System.out.println("执行本地事务arg = " + arg);

        String tags = msg.getTags();

        if (tags.equals("transaction2")) {
            System.out.println("======我的操作============，失败了  -进行ROLLBACK");
            return LocalTransactionState.ROLLBACK_MESSAGE;
        }
        return LocalTransactionState.COMMIT_MESSAGE;
        // return LocalTransactionState.UNKNOW;
    }
}
