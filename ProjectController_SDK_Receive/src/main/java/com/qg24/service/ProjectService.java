package com.qg24.service;

import com.qg24.po.dto.*;
import com.qg24.po.result.PageBean;
import com.qg24.po.vo.ProjectDetailedInfoVO;
import com.qg24.po.vo.ProjectDisplayVO;
import com.qg24.po.vo.UserOwnMonitorForProjectVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ProjectService {

    void updateErrorNumAndRate(int insertNum,int projectId);

    void updateErrorRate(int insertNum,int projectId);

    void updateBackendErrorNum(int insertNum, int projectId);

    void updateFrontErrorNum(int insertNum, int projectId);

    void updateMobileErrorNum(int insertNum, int projectId);
}
