package com.sinochem.yunlian.upm.admin.mapper;

import com.sinochem.yunlian.upm.admin.domain.RoleResourceData;
import com.sinochem.yunlian.upm.admin.domain.RoleResourceDataExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface RoleResourceDataMapper {
    int countByExample(RoleResourceDataExample example);

    int deleteByExample(RoleResourceDataExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(RoleResourceData record);

    int insertSelective(RoleResourceData record);

    List<RoleResourceData> selectByExampleWithRowbounds(RoleResourceDataExample example, RowBounds rowBounds);

    List<RoleResourceData> selectByExample(RoleResourceDataExample example);

    RoleResourceData selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") RoleResourceData record, @Param("example") RoleResourceDataExample example);

    int updateByExample(@Param("record") RoleResourceData record, @Param("example") RoleResourceDataExample example);

    int updateByPrimaryKeySelective(RoleResourceData record);

    int updateByPrimaryKey(RoleResourceData record);
}