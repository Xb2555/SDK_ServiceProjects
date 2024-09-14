package com.qg24.service.impl;

import com.qg24.dao.LogMapper;
import com.qg24.po.entity.*;
import com.qg24.po.result.PageBean;
import com.qg24.po.vo.*;
import com.qg24.service.LogService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LogServiceImpl implements LogService {
    @Autowired
    private LogMapper logMapper;

    @Override
    public int getTotalLogNum(int projectId) {
        return logMapper.getFrontExceptionLogsNum(projectId)+
                logMapper.getTotalBackendExceptionLogs(projectId)+
                logMapper.getTotalBackendLogInfo(projectId)+
                logMapper.getTotalBackendRequestNum(projectId)+
                logMapper.getTotalFrontPerformanceNum(projectId)+
                logMapper.getMobileExceptionNum(projectId)+
                logMapper.getMobilePerformanceNum(projectId);
    }

    @Scheduled(cron = "0 0 0 * * *")
    private void runEachDay() {
        List<Integer> projectIds = logMapper.getProjectIds();
        logMapper.insertProjectPresentationData(projectIds);
    }

}
