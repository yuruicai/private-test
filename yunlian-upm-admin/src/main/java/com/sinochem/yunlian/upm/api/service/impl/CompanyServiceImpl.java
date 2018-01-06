package com.sinochem.yunlian.upm.api.service.impl;

import com.sinochem.yunlian.upm.admin.domain.Company;
import com.sinochem.yunlian.upm.admin.mapper.CompanyMapper;
import com.sinochem.yunlian.upm.api.exception.ApiException;
import com.sinochem.yunlian.upm.api.service.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author huangyang
 * @Description:
 * @date 2018/01/05 下午5:13
 */
@Slf4j
@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyMapper companyDao;

    @Override
    public int insert(Company company) {
        //todo 数据验证
        companyDao.insertDynamic(company);
        return company.getId();
    }

    @Override
    public Company getById(int id) {
        return companyDao.findById(id);
    }

    @Override
    public Company getByCompanyName(String compaynName) {
        return companyDao.findByCompanyName(compaynName);
    }

    @Override
    public int update(Company company) {
        Integer id = company.getId();
        if (id == null) {
            throw ApiException.of("企业ID不能为空");
        }
        return companyDao.updateDynamicById(company);
    }

    /**
     * 企业认证的标识码
     * @param id
     * @return
     */
    @Override
    public int getCertificateStatus(Integer id) {
        if (id == null) {
            throw ApiException.of("企业ID不能为空");
        }
        Company company = companyDao.findById(id);
        if (company == null) {
            throw ApiException.of("企业不存在：id=" + id);
        }
        return company.getStatus();
    }
}
