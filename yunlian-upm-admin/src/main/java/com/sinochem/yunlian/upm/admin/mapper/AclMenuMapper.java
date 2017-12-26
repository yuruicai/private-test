package com.sinochem.yunlian.upm.admin.mapper;

import com.sinochem.yunlian.upm.admin.domain.AclMenu;
import com.sinochem.yunlian.upm.admin.domain.AclMenuExample;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface AclMenuMapper {
    int deleteByExample(AclMenuExample example);

    int deleteByPrimaryKey(String id);

    int insert(AclMenu record);

    int insertSelective(AclMenu record);

    List<AclMenu> selectByExampleWithRowbounds(AclMenuExample example, RowBounds rowBounds);

    List<AclMenu> selectByExample(AclMenuExample example);

    AclMenu selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") AclMenu record, @Param("example") AclMenuExample example);

    int updateByExample(@Param("record") AclMenu record, @Param("example") AclMenuExample example);

    int updateByPrimaryKeySelective(AclMenu record);

    int updateByPrimaryKey(AclMenu record);
}