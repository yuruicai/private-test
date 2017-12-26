package com.sinochem.yunlian.upm.admin.mapper;

import com.sinochem.yunlian.upm.admin.domain.AclRolePermissionRlt;
import com.sinochem.yunlian.upm.admin.domain.AclRolePermissionRltExample;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface AclRolePermissionRltMapper {
    int countByExample(AclRolePermissionRltExample example);

    int deleteByExample(AclRolePermissionRltExample example);

    int deleteByPrimaryKey(String id);

    int insert(AclRolePermissionRlt record);

    int insertSelective(AclRolePermissionRlt record);

    List<AclRolePermissionRlt> selectByExampleWithRowbounds(AclRolePermissionRltExample example, RowBounds rowBounds);

    List<AclRolePermissionRlt> selectByExample(AclRolePermissionRltExample example);

    AclRolePermissionRlt selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") AclRolePermissionRlt record, @Param("example") AclRolePermissionRltExample example);

    int updateByExample(@Param("record") AclRolePermissionRlt record, @Param("example") AclRolePermissionRltExample example);

    int updateByPrimaryKeySelective(AclRolePermissionRlt record);

    int updateByPrimaryKey(AclRolePermissionRlt record);
}