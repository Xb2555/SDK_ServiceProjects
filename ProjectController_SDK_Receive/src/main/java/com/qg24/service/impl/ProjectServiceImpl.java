package com.qg24.service.impl;

import com.qg24.dao.LogMapper;
import com.qg24.dao.ProjectMapper;
import com.qg24.po.dto.*;
import com.qg24.po.entity.*;
import com.qg24.po.result.PageBean;
import com.qg24.po.vo.ProjectDetailedInfoVO;
import com.qg24.po.vo.ProjectDisplayVO;
import com.qg24.po.vo.UserOwnMonitorForProjectVO;
import com.qg24.service.LogService;
import com.qg24.service.ProjectService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class ProjectServiceImpl implements ProjectService {


    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private LogService logService;


    //更新报错数报错率
    @Override
    public void updateErrorNumAndRate(int insertNum,int projectId) {
        //拿到原始数据
        ProjectPresentationData projectPresentationData = projectMapper.getProjectData(projectId);
        //更新数据
        int errorNum = projectPresentationData.getErrorNumber()+insertNum;
        //获取所有的日志数
        int total = logService.getTotalLogNum(projectId)+insertNum;
        //更新报错率
        double errorRate = (double) errorNum / total;
        //TODO 若报错率大于阈值，提醒发布者和管理员
        //更新数据
        int count = projectMapper.updateErrorRateAndNum(errorNum,errorRate,projectId);
    }

    //更新报错率
    @Override
    public void updateErrorRate(int insertNum,int projectId) {
        //拿到原始数据
        ProjectPresentationData projectPresentationData = projectMapper.getProjectData(projectId);
        //更新数据
        int errorNum = projectPresentationData.getErrorNumber();
        //获取所有的日志数
        int total = logService.getTotalLogNum(projectId)+insertNum;
        //更新报错率
        double errorRate = (double) errorNum / total;
        //更新数据
        int count = projectMapper.updateErrorRateAndNum(errorNum,errorRate,projectId);
    }

    //更新后台报错数
    @Override
    public void updateBackendErrorNum(int insertNum, int projectId) {
        ProjectPresentationData data = projectMapper.getProjectData(projectId);
        int errorNum = data.getBackendErrorNumber()+insertNum;
        projectMapper.updateBackendErr(errorNum, projectId);
    }

    //更新前端报错数
    @Override
    public void updateFrontErrorNum(int insertNum, int projectId) {
        ProjectPresentationData data = projectMapper.getProjectData(projectId);
        int errorNum = data.getFrontErrorNumber()+insertNum;
        projectMapper.updateFrontErr(errorNum, projectId);
    }

    //更新移动报错数
    @Override
    public void updateMobileErrorNum(int insertNum, int projectId) {
        ProjectPresentationData data = projectMapper.getProjectData(projectId);
        int errorNum = data.getMobileErrorNumber()+insertNum;
        projectMapper.updateMobileErr(errorNum, projectId);
    }


}
