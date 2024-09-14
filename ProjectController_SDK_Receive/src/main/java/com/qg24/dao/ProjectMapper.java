package com.qg24.dao;

import com.qg24.po.entity.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ProjectMapper {

    // 更新报警数，报警率
    int updateErrorRateAndNum(@Param("errorNum")int errorNum,@Param("errorRate")double errorRate,@Param("projectId")int projectId);

    // 获取所有项目数据
    ProjectPresentationData getProjectData(@Param("projectId")int projectId);

    // 更新后台报错数
    int updateBackendErr(@Param("errorNum")int errorNum,@Param("projectId")int projectId);

    // 更新前端报错数
    int updateFrontErr(@Param("errorNum")int errorNum,@Param("projectId")int projectId);

    // 更新移动报错数
    int updateMobileErr(@Param("errorNum")int errorNum, @Param("projectId")int projectId);

    // 更新项目访问数
    @Update("update project_presentation_data set visits = visits + #{accessCount}, total_visits = total_visits + #{accessCount} where project_id = #{projectId} and date = (" +
            "select MAX(date) from project_presentation_data where project_id = #{projectId})")
    void updateProjectAccessCount(@Param("accessCount")int accessCount, @Param("projectId")int projectId);

}
