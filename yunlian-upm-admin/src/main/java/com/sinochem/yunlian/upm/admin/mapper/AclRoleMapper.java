package com.sinochem.yunlian.upm.admin.mapper;

import com.sinochem.yunlian.upm.admin.domain.AclRole;
import com.sinochem.yunlian.upm.admin.domain.AclRoleExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface AclRoleMapper {
    int countByExample(AclRoleExample example);

    int deleteByExample(AclRoleExample example);

    int deleteByPrimaryKey(String id);

    int insert(AclRole record);

    int insertSelective(AclRole record);

    List<AclRole> selectByExampleWithRowbounds(AclRoleExample example, RowBounds rowBounds);

    List<AclRole> selectByExample(AclRoleExample example);

    AclRole selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") AclRole record, @Param("example") AclRoleExample example);

    int updateByExample(@Param("record") AclRole record, @Param("example") AclRoleExample example);

    int updateByPrimaryKeySelective(AclRole record);

    int updateByPrimaryKey(AclRole record);
}