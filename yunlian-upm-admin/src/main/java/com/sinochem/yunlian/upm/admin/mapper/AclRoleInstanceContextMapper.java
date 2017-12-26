package com.sinochem.yunlian.upm.admin.mapper;

import com.sinochem.yunlian.upm.admin.domain.AclRoleInstanceContext;
import com.sinochem.yunlian.upm.admin.domain.AclRoleInstanceContextExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface AclRoleInstanceContextMapper {
    int countByExample(AclRoleInstanceContextExample example);

    int deleteByExample(AclRoleInstanceContextExample example);

    int deleteByPrimaryKey(String id);

    int insert(AclRoleInstanceContext record);

    int insertSelective(AclRoleInstanceContext record);

    List<AclRoleInstanceContext> selectByExampleWithRowbounds(AclRoleInstanceContextExample example, RowBounds rowBounds);

    List<AclRoleInstanceContext> selectByExample(AclRoleInstanceContextExample example);

    AclRoleInstanceContext selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") AclRoleInstanceContext record, @Param("example") AclRoleInstanceContextExample example);

    int updateByExample(@Param("record") AclRoleInstanceContext record, @Param("example") AclRoleInstanceContextExample example);

    int updateByPrimaryKeySelective(AclRoleInstanceContext record);

    int updateByPrimaryKey(AclRoleInstanceContext record);
}