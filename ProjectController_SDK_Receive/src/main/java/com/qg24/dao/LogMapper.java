package com.qg24.dao;

import com.qg24.po.entity.*;
import com.qg24.po.vo.ShowLogNumberOneWeekForGroupVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LogMapper {

    //查询总后台异常项目日志数
    int getTotalBackendExceptionLogs(@Param("projectId")int projectId);


    //查询总后台访问日志
    int getTotalBackendRequestNum(@Param("projectId")int projectId);


    //查询前端总异常日志数
    int getFrontExceptionLogsNum(@Param("projectId")int projectId);


    //查询总前端性能日志
    int getTotalFrontPerformanceNum(@Param("projectId")int projectId);


    //查询移动总性能日志数
    int getMobilePerformanceNum(@Param("projectId")int projectId);


    //查询移动总异常日志数
    int getMobileExceptionNum(@Param("projectId")int projectId);


    //查询后台自定义日志总数
    int getTotalBackendLogInfo(@Param("projectId")int projectId);


    @Select("select project_id from project")
    List<Integer> getProjectIds();

    void insertProjectPresentationData(@Param("projectIds") List<Integer> projectIds);
}
