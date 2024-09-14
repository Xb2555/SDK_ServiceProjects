package com.qg24.controller;



import com.qg24.Mq.ASyncLogProducer;
import com.qg24.po.dto.*;
import com.qg24.po.entity.Result;
import com.qg24.service.SDKService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sdk")
public class SDKController {

    @Autowired
    private SDKService sdkService;
    private List<BackendLogDTO> backendLogDTOList;

    /**
     * <p>项目的后台服务器引入了sdk并且正常配置之后, 将会在应用初始化时尝试与管理平台建立连接
     * 建立连接时, 服务器将会携带项目ID访问这个接口</p>
     * <br>
     * <p>1. 检查连接是否正常</p>
     * <p>2. 检查项目是否正常在平台通过了审核</p>
     * <p>3. 当出现断连时, 所有项目的服务器将会进行断线重连, 也会访问这个接口</p>
     *
     * @param projectId 后台sdk传输过来的项目id
     * @return Result
     */
    @GetMapping("/backend/connect")
    public Result<?> connect(@RequestParam("projectId") int projectId){
        boolean b = sdkService.connect(projectId);
        if (b){
            return Result.success("CONNECT_SUCCESS");
        }else {
            return Result.error("INVALID_CONNECTION");
        }
    }

    @PostMapping("/backend/accessLog")
    public Result<?> batchInsertBackendRequestInfo(@RequestBody List<BackendRequestInfoDTO> backendRequestInfoDTOList) throws Exception {
        //发给rocketmq
        ASyncLogProducer producer = new ASyncLogProducer();
        boolean status = producer.sendToRQ("backend_accessLog",backendRequestInfoDTOList);
        //sdkService.batchInsertBackendRequestInfo(backendRequestInfoDTOList);
       if(status){
           return Result.success("success");
       }else {
           return Result.error("error");
       }
    }

    @PostMapping("/backend/exceptionLog")
    public Result<?> batchInsertBackendExceptionList(@RequestBody List<BackendExceptionInfoDTO> backendExceptionInfoDTOList) throws Exception {
        //发给rocketmq
        ASyncLogProducer producer = new ASyncLogProducer();
        boolean status = producer.sendToRQ("backend_exceptionLog",backendExceptionInfoDTOList);
        if(status){
            return Result.success("success");
        }else {
            return Result.error("error");
        }
//        sdkService.batchInsertBackendExceptionInfo(backendExceptionInfoDTOList);
    }

    @PostMapping("/backend/log")
    public Result<?> batchInsertBackendLog(@RequestBody List<BackendLogDTO> backendLogDTOList) throws Exception {
        this.backendLogDTOList = backendLogDTOList;
        //sdkService.batchInsertBackendLog(backendLogDTOList);
        //发给rocketmq
        ASyncLogProducer producer = new ASyncLogProducer();
        boolean status = producer.sendToRQ("backend_log",backendLogDTOList);
        if(status){
            return Result.success("success");
        }else {
            return Result.error("error");
        }
    }

    @PostMapping("/front/performanceLog")
    public Result<?> batchInsertFrontPerformanceLog(@RequestBody List<FrontLogDTO> frontLogDTOList) throws Exception{
        //sdkService.batchInsertFrontPerformanceLog(frontLogDTOList);
        //发给rocketmq
        ASyncLogProducer producer = new ASyncLogProducer();
        boolean status = producer.sendToRQ("front_performanceLog",frontLogDTOList);
        if(status){
            return Result.success("success");
        }else {
            return Result.error("error");
        }
    }

    @PostMapping("/front/exceptionLog")
    public Result<?> batchInsertFrontExceptionLog(@RequestBody List<FrontLogDTO> frontLogDTOList) throws Exception{
        //sdkService.batchInsertFrontExceptionLog(frontLogDTOList);
        //发给rocketmq
        ASyncLogProducer producer = new ASyncLogProducer();
        boolean status = producer.sendToRQ("front_exceptionLog",frontLogDTOList);
        if(status){
            return Result.success("success");
        }else {
            return Result.error("error");
        }
    }

    @PostMapping("/mobile/performanceLog")
    public Result<?> batchInsertMobilePerformanceLog(@RequestBody List<MobilePerformanceLogDTO> mobilePerformanceLogDTOList) throws Exception{
        //sdkService.batchInsertMobilePerformanceLog(mobilePerformanceLogDTOList);
        //发给rocketmq
        ASyncLogProducer producer = new ASyncLogProducer();
        boolean status = producer.sendToRQ("mobile_performanceLog",mobilePerformanceLogDTOList);
        if(status){
            return Result.success("success");
        }else {
            return Result.error("error");
        }
    }

    @PostMapping("/mobile/exceptionLog")
    public Result<?> batchInsertMobileExceptionLog(@RequestBody List<MobileExceptionDTO> mobileExceptionDTOList) throws Exception{
        //sdkService.batchInsertMobileExceptionLog(mobileExceptionDTOList);
        //发给rocketmq
        ASyncLogProducer producer = new ASyncLogProducer();
        boolean status = producer.sendToRQ("mobile_exceptionLog",mobileExceptionDTOList);
        if(status){
            return Result.success("success");
        }else {
            return Result.error("error");
        }
    }

}
