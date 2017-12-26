package com.sinochem.yunlian.upm.sso.mapper;

import com.sinochem.yunlian.upm.sso.domain.AclApplication;
import com.sinochem.yunlian.upm.sso.domain.AclApplicationExample;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface AclApplicationMapper {
    int countByExample(AclApplicationExample example);

    int deleteByExample(AclApplicationExample example);

    int deleteByPrimaryKey(String id);

    int insert(AclApplication record);

    int insertSelective(AclApplication record);

    List<AclApplication> selectByExampleWithRowbounds(AclApplicationExample example, RowBounds rowBounds);

    List<AclApplication> selectByExample(AclApplicationExample example);

    AclApplication selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") AclApplication record, @Param("example") AclApplicationExample example);

    int updateByExample(@Param("record") AclApplication record, @Param("example") AclApplicationExample example);

    int updateByPrimaryKeySelective(AclApplication record);

    int updateByPrimaryKey(AclApplication record);
}