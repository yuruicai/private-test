package com.sinochem.yunlian.upm.admin.mapper;

import com.sinochem.yunlian.upm.admin.domain.Company;

/**
 * @author huangyang
 * 企业信息 mapper
 */
public interface CompanyMapper {


    int insertDynamic(Company record);

    int updateDynamicById(Company record);

    Company findById(Integer id);

    Company findByCompanyName(String companyName);


}