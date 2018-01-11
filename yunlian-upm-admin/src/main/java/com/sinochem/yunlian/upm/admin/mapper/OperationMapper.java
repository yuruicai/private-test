package com.sinochem.yunlian.upm.admin.mapper;

import com.sinochem.yunlian.upm.admin.domain.Operation;
import com.sinochem.yunlian.upm.admin.domain.OperationExample;
import java.util.List;

import com.sinochem.yunlian.upm.api.vo.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface OperationMapper {
    int countByExample(OperationExample example);

    int deleteByExample(OperationExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Operation record);

    int insertSelective(Operation record);

    List<Operation> selectByExampleWithRowbounds(OperationExample example, RowBounds rowBounds);

    List<Operation> selectByExample(OperationExample example);

    Operation selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Operation record, @Param("example") OperationExample example);

    int updateByExample(@Param("record") Operation record, @Param("example") OperationExample example);

    int updateByPrimaryKeySelective(Operation record);

    int updateByPrimaryKey(Operation record);

}