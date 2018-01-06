package com.sinochem.yunlian.upm.api.service;

import com.sinochem.yunlian.upm.admin.domain.Company;

/**
 * @author huangyang
 * @Description: ${todo}(这里用一句话描述这个类的作用)
 * @date 2018/01/05 下午5:13
 */
public interface CompanyService {

    int insert(Company company);

    Company getById(int id);

    int update(Company company);

}
