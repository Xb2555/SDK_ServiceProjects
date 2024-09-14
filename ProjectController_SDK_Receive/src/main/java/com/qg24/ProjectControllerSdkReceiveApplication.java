package com.qg24;

import com.qg24.Mq.LogConsumer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ProjectControllerSdkReceiveApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ProjectControllerSdkReceiveApplication.class, args);
        new LogConsumer();
    }

}
