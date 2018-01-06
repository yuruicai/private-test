package com.sinochem.yunlian.upm.admin.mapper;

import com.sinochem.yunlian.upm.admin.domain.AclUser;
import com.sinochem.yunlian.upm.admin.domain.AclUserExample;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface AclUserMapper {
    int countByExample(AclUserExample example);

    int deleteByExample(AclUserExample example);

    int deleteByPrimaryKey(String id);

    int insert(AclUser record);

    int insertSelective(AclUser record);

    List<AclUser> selectByExampleWithRowbounds(AclUserExample example, RowBounds rowBounds);

    List<AclUser> selectByExample(AclUserExample example);

    AclUser selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") AclUser record, @Param("example") AclUserExample example);

    int updateByExample(@Param("record") AclUser record, @Param("example") AclUserExample example);

    int updateByPrimaryKeySelective(AclUser record);

    int updateByPrimaryKey(AclUser record);

    int getCount(String name);
}