package com.qg24.Mq;

import com.alibaba.fastjson.JSON;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.concurrent.CountDownLatch;

/**
 * 同步发送消息
 */
public class ASyncLogProducer {
    private SendStatus status;
    public boolean sendToRQ(String tags,Object message) throws MQClientException, MQBrokerException, RemotingException, InterruptedException {
        //创建消息生产者，指定组名
        DefaultMQProducer producer = new DefaultMQProducer("DefaultCluster");
        //指定nameserver的地址
        producer.setNamesrvAddr("10.21.56.118:9876");
        producer.setSendMsgTimeout(30000); // 设置超时时间为 30 秒
        //启动producer
        producer.start();
        //创建消息对象,指定topic，tag和消息体,将message转为json字符串
        Message msg = new Message("logs_topic", tags, JSON.toJSONString(message).getBytes());
        // 使用 CountDownLatch 等待异步回调完成
        CountDownLatch countDownLatch = new CountDownLatch(1);
        //发送消息
        producer.send(msg, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                //发送成功
                status = sendResult.getSendStatus();
                countDownLatch.countDown();
            }

            @Override
            public void onException(Throwable throwable) {
                System.out.println("错误消息:"+throwable);
                status = null;
                countDownLatch.countDown();
            }
        });
        // 等待异步回调完成
        countDownLatch.await();
        //关闭生产者
        producer.shutdown();
        return status.equals(SendStatus.SEND_OK);
    }
}
