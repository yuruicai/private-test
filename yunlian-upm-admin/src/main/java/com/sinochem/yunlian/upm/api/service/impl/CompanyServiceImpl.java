package com.sinochem.yunlian.upm.api.service.impl;

import com.github.pagehelper.PageHelper;
import com.sinochem.yunlian.upm.admin.domain.Company;
import com.sinochem.yunlian.upm.admin.mapper.CompanyMapper;
import com.sinochem.yunlian.upm.api.exception.ApiException;
import com.sinochem.yunlian.upm.api.service.CompanyService;
import com.sinochem.yunlian.upm.api.vo.CompanyVo;
import com.sinochem.yunlian.upm.api.vo.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    public Company getByCompanyName(String companyName) {
        return companyDao.findByCompanyName(companyName);
    }

    @Override
    public PageInfo getPagedByCompanyName(String companyName, int curPage, int pageSize) {
        PageHelper.startPage(curPage, pageSize);
        String name = "".equals(companyName) ? null : companyName;
        List<Company> companies = companyDao.findAllByCompanyName(name);
        List<CompanyVo> companyVos = companies.stream().map(c -> new CompanyVo(c)).collect(Collectors.toList());
        com.github.pagehelper.PageInfo pageInfo = new com.github.pagehelper.PageInfo(companyVos);
        return new PageInfo(pageInfo.getPageNum(), pageSize, pageInfo.getPages(), (int) pageInfo.getTotal(), pageInfo.getList());

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
     *
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
