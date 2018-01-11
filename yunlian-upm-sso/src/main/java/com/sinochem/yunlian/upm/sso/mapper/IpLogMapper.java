package com.sinochem.yunlian.upm.sso.mapper;

import com.sinochem.yunlian.upm.sso.domain.IpLog;
import com.sinochem.yunlian.upm.sso.domain.IpLogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface IpLogMapper {
    int countByExample(IpLogExample example);

    int deleteByExample(IpLogExample example);

    int deleteByPrimaryKey(Long id);

    int insert(IpLog record);

    int insertSelective(IpLog record);

    List<IpLog> selectByExampleWithRowbounds(IpLogExample example, RowBounds rowBounds);

    List<IpLog> selectByExample(IpLogExample example);

    IpLog selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") IpLog record, @Param("example") IpLogExample example);

    int updateByExample(@Param("record") IpLog record, @Param("example") IpLogExample example);

    int updateByPrimaryKeySelective(IpLog record);

    int updateByPrimaryKey(IpLog record);
}