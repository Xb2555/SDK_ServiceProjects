package com.qg24.dao;



import com.qg24.po.entity.Project;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SDKMapper {

    @Select("select * from project where project_id = #{projectId}")
    Project selectProjectById(@Param("projectId") int projectId);
}
