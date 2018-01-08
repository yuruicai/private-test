package com.sinochem.yunlian.upm.api.service;

import com.sinochem.yunlian.upm.admin.domain.Company;
import com.sinochem.yunlian.upm.api.vo.PageInfo;

/**
 * @author huangyang
 * @Description: ${todo}(这里用一句话描述这个类的作用)
 * @date 2018/01/05 下午5:13
 */
public interface CompanyService {

    int insert(Company company);

    Company getById(int id);

    Company getByCompanyName(String companyName);

    /**
     * 根据企业名称查询，支持模糊查询，分页
     * @param companyName
     * @return
     */
    PageInfo getPagedByCompanyName(String companyName, int curPage, int pageSize);

    int update(Company company);

    int getCertificateStatus(Integer id);
}
