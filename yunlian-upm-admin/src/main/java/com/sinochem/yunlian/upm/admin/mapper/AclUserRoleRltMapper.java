package com.sinochem.yunlian.upm.admin.mapper;

import com.sinochem.yunlian.upm.admin.domain.AclUserRoleRlt;
import com.sinochem.yunlian.upm.admin.domain.AclUserRoleRltExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface AclUserRoleRltMapper {
    int countByExample(AclUserRoleRltExample example);

    int deleteByExample(AclUserRoleRltExample example);

    int deleteByPrimaryKey(String id);

    int insert(AclUserRoleRlt record);

    int insertSelective(AclUserRoleRlt record);

    List<AclUserRoleRlt> selectByExampleWithRowbounds(AclUserRoleRltExample example, RowBounds rowBounds);

    List<AclUserRoleRlt> selectByExample(AclUserRoleRltExample example);

    AclUserRoleRlt selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") AclUserRoleRlt record, @Param("example") AclUserRoleRltExample example);

    int updateByExample(@Param("record") AclUserRoleRlt record, @Param("example") AclUserRoleRltExample example);

    int updateByPrimaryKeySelective(AclUserRoleRlt record);

    int updateByPrimaryKey(AclUserRoleRlt record);
}