package com.sinochem.yunlian.upm.admin.mapper;

import com.sinochem.yunlian.upm.admin.domain.ResourceData;
import com.sinochem.yunlian.upm.admin.domain.ResourceDataExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface ResourceDataMapper {
    int countByExample(ResourceDataExample example);

    int deleteByExample(ResourceDataExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ResourceData record);

    int insertSelective(ResourceData record);

    List<ResourceData> selectByExampleWithRowbounds(ResourceDataExample example, RowBounds rowBounds);

    List<ResourceData> selectByExample(ResourceDataExample example);

    ResourceData selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ResourceData record, @Param("example") ResourceDataExample example);

    int updateByExample(@Param("record") ResourceData record, @Param("example") ResourceDataExample example);

    int updateByPrimaryKeySelective(ResourceData record);

    int updateByPrimaryKey(ResourceData record);
}