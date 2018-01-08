package com.sinochem.yunlian.upm.admin.mapper;

import com.sinochem.yunlian.upm.admin.domain.Company;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author huangyang
 * 企业信息 mapper
 */
public interface CompanyMapper {


    /**
     * 动态插入
     * @param record
     * @return
     */
    int insertDynamic(Company record);

    /**
     * 动态更新
     * @param record
     * @return
     */
    int updateDynamicById(Company record);

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    Company findById(Integer id);

    /**
     * 根据名字查询
     * @param companyName
     * @return
     */
    Company findByCompanyName(String companyName);

    /**
     * 查询，支持模糊查
     * @param companyName
     * @return
     */
    List<Company> findAllByCompanyName(@Param("companyName") String companyName);

}