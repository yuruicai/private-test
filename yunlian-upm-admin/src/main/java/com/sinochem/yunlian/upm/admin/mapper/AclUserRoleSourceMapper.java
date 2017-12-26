package com.sinochem.yunlian.upm.admin.mapper;

import com.sinochem.yunlian.upm.admin.domain.AclUserRoleSource;
import com.sinochem.yunlian.upm.admin.domain.AclUserRoleSourceExample;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface AclUserRoleSourceMapper {
    int countByExample(AclUserRoleSourceExample example);

    int deleteByExample(AclUserRoleSourceExample example);

    int deleteByPrimaryKey(String id);

    int insert(AclUserRoleSource record);

    int insertSelective(AclUserRoleSource record);

    List<AclUserRoleSource> selectByExampleWithRowbounds(AclUserRoleSourceExample example, RowBounds rowBounds);

    List<AclUserRoleSource> selectByExample(AclUserRoleSourceExample example);

    AclUserRoleSource selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") AclUserRoleSource record, @Param("example") AclUserRoleSourceExample example);

    int updateByExample(@Param("record") AclUserRoleSource record, @Param("example") AclUserRoleSourceExample example);

    int updateByPrimaryKeySelective(AclUserRoleSource record);

    int updateByPrimaryKey(AclUserRoleSource record);
}