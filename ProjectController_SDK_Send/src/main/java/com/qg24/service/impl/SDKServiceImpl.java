package com.qg24.service.impl;



import com.qg24.dao.SDKMapper;
import com.qg24.service.SDKService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SDKServiceImpl implements SDKService {

    @Autowired
    private SDKMapper sdkMapper;

    @Override
    public boolean connect(int projectId) {
        return sdkMapper.selectProjectById(projectId) != null;
    }

}
