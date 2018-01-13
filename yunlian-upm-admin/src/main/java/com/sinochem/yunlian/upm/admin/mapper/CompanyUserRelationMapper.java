package com.sinochem.yunlian.upm.admin.mapper;

import com.sinochem.yunlian.upm.admin.domain.CompanyUserRelation;
import com.sinochem.yunlian.upm.admin.domain.CompanyUserRelationExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface CompanyUserRelationMapper {
    int countByExample(CompanyUserRelationExample example);

    int deleteByExample(CompanyUserRelationExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(CompanyUserRelation record);

    int insertSelective(CompanyUserRelation record);

    List<CompanyUserRelation> selectByExampleWithRowbounds(CompanyUserRelationExample example, RowBounds rowBounds);

    List<CompanyUserRelation> selectByExample(CompanyUserRelationExample example);

    CompanyUserRelation selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") CompanyUserRelation record, @Param("example") CompanyUserRelationExample example);

    int updateByExample(@Param("record") CompanyUserRelation record, @Param("example") CompanyUserRelationExample example);

    int updateByPrimaryKeySelective(CompanyUserRelation record);

    int updateByPrimaryKey(CompanyUserRelation record);

}