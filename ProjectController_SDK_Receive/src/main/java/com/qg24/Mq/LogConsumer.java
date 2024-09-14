package com.qg24.Mq;

import com.alibaba.fastjson2.JSON;
import com.qg24.po.dto.*;
import com.qg24.service.SDKService;
import lombok.Synchronized;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 消费者，用消息的tag来区分消息
 */
@Component
public class LogConsumer {
    @Autowired
    private SDKService sdkService;

    private static final int MAX_RETRY_NUM = 3; //设置最大重试次数
    public LogConsumer() throws Exception{
        //1.创建消费者
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("DefaultCluster");
        //2.指定nameserver地址
        consumer.setNamesrvAddr("10.21.56.118:9876");
        //3.订阅主题和tag
        consumer.subscribe("logs_topic","*");
        //设置消息拉取间隔
        consumer.setPullInterval(100);
        //设置并行消费
        // 设置并发消费的线程数
        consumer.setConsumeThreadMin(20);
        consumer.setConsumeThreadMax(50);
        //4.设置回调函数
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            //接收消息内容
            @Override
            public synchronized ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                for (int i = 0; i < list.size(); i++) {
                    MessageExt msg = list.get(i);
                    try {
                        if (msg.getTags().equals("backend_accessLog")) {
                            // 处理后台请求日志
                            List<BackendRequestInfoDTO> backendRequestInfoDTOList = new ArrayList<>();
                            // 将拿到的数据反序列化为原数据
                            String json = new String(msg.getBody(), StandardCharsets.UTF_8);
                            backendRequestInfoDTOList = JSON.parseArray(json, BackendRequestInfoDTO.class);
                            sdkService.batchInsertBackendRequestInfo(backendRequestInfoDTOList);
                        } else if (msg.getTags().equals("backend_exceptionLog")) {
                            // 处理后台异常信息
                            List<BackendExceptionInfoDTO> backendExceptionInfoDTOList = new ArrayList<>();
                            // 反序列化
                            String json = new String(msg.getBody(), StandardCharsets.UTF_8);
                            backendExceptionInfoDTOList = JSON.parseArray(json, BackendExceptionInfoDTO.class);
                            sdkService.batchInsertBackendExceptionInfo(backendExceptionInfoDTOList);
                        } else if (msg.getTags().equals("backend_log")) {
                            // 处理后台异常信息
                            List<BackendLogDTO> backendLogDTOList = new ArrayList<>();
                            // 反序列化
                            String json = new String(msg.getBody(), StandardCharsets.UTF_8);
                            backendLogDTOList = JSON.parseArray(json, BackendLogDTO.class);
                            sdkService.batchInsertBackendLog(backendLogDTOList);
                        } else if (msg.getTags().equals("front_performanceLog")) {
                            // 处理前端性能信息
                            List<FrontLogDTO> frontLogDTOList = new ArrayList<>();
                            // 反序列化
                            String json = new String(msg.getBody(), StandardCharsets.UTF_8);
                            frontLogDTOList = JSON.parseArray(json, FrontLogDTO.class);
                            sdkService.batchInsertFrontPerformanceLog(frontLogDTOList);
                        } else if (msg.getTags().equals("front_exceptionLog")) {
                            // 处理前端异常信息
                            List<FrontLogDTO> frontLogDTOList = new ArrayList<>();
                            // 反序列化
                            String json = new String(msg.getBody(), StandardCharsets.UTF_8);
                            frontLogDTOList = JSON.parseArray(json, FrontLogDTO.class);
                            sdkService.batchInsertFrontExceptionLog(frontLogDTOList);
                        } else if (msg.getTags().equals("mobile_performanceLog")) {
                            // 处理移动性能消息
                            List<MobilePerformanceLogDTO> mobilePerformanceLogDTOList = new ArrayList<>();
                            // 反序列化
                            String json = new String(msg.getBody(), StandardCharsets.UTF_8);
                            mobilePerformanceLogDTOList = JSON.parseArray(json, MobilePerformanceLogDTO.class);
                            sdkService.batchInsertMobilePerformanceLog(mobilePerformanceLogDTOList);
                        } else if (msg.getTags().equals("mobile_exceptionLog")) {
                            // 处理移动异常消息
                            List<MobileExceptionDTO> mobileExceptionDTOList = new ArrayList<>();
                            // 反序列化
                            String json = new String(msg.getBody(), StandardCharsets.UTF_8);
                            mobileExceptionDTOList = JSON.parseArray(json, MobileExceptionDTO.class);
                            sdkService.batchInsertMobileExceptionLog(mobileExceptionDTOList);
                        }
                    } catch (Exception e) {
                        // 捕获异常并检测尝试次数 返回 RECONSUME_LATER，以便消息可以重新消费
                        int reTryNum = msg.getReconsumeTimes();
                        if(reTryNum>=MAX_RETRY_NUM){
                            //超过最大重试次数,直接返回成功，不再处理该消息
                            System.out.println("Roll back error message: "+ e.getMessage());
                            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                        }
                        System.out.println("Error processing message: " + e.getMessage());
                        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                    }
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        //5.启动consumer
        consumer.start();
    }
}
