package com.sinochem.yunlian.upm.admin.mapper;

import com.sinochem.yunlian.upm.admin.domain.RoleOperation;
import com.sinochem.yunlian.upm.admin.domain.RoleOperationExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface RoleOperationMapper {
    int countByExample(RoleOperationExample example);

    int deleteByExample(RoleOperationExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(RoleOperation record);

    int insertSelective(RoleOperation record);

    List<RoleOperation> selectByExampleWithRowbounds(RoleOperationExample example, RowBounds rowBounds);

    List<RoleOperation> selectByExample(RoleOperationExample example);

    RoleOperation selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") RoleOperation record, @Param("example") RoleOperationExample example);

    int updateByExample(@Param("record") RoleOperation record, @Param("example") RoleOperationExample example);

    int updateByPrimaryKeySelective(RoleOperation record);

    int updateByPrimaryKey(RoleOperation record);
}