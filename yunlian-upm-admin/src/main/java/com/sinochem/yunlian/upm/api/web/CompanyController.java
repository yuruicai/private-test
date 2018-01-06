package com.sinochem.yunlian.upm.api.web;

import com.sinochem.yunlian.upm.admin.domain.Company;
import com.sinochem.yunlian.upm.api.service.CompanyService;
import com.sinochem.yunlian.upm.api.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author huangyang
 * @Description: 公司信息 controller
 * @date 2018/01/05 下午8:01
 */
@RestController
@RequestMapping("api")
@Slf4j
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @RequestMapping(value = "company", method = RequestMethod.POST)
    public Response addCompany(@RequestBody Company company) {
        int id = companyService.insert(company);
        return Response.succeed().put("id", id);
    }

    @RequestMapping(value = "company/modification", method = RequestMethod.POST)
    public Response modify(@RequestBody Company company) {
        companyService.update(company);
        return Response.succeed();

    }

    @RequestMapping(value = "company", method = RequestMethod.GET)
    public Response getOne(String companyName) {
        Company company = companyService.getByCompanyName(companyName);
        if (company == null) {
            return Response.fail(String.format("公司信息不存在.公司名=%s", companyName));
        }
        return Response.succeed(company);
    }

    @RequestMapping(value = "company/{id}", method = RequestMethod.GET)
    public Response getOne(@PathVariable("id") Integer companyId) {
        if (companyId == null) {
            log.info("查询企业信息ID参数为空");
            return Response.fail("companyId参数为空");
        }
        Company company = companyService.getById(companyId);
        if (company == null) {
            log.info("企业信息不存在：ID={}", companyId);
            return Response.fail(String.format("公司信息不存在.公司ID=%s", companyId));
        }
        return Response.succeed(company);
    }

    /**
     * 获取公司认证状态
     *
     * @param companyId
     * @return 0-未认证，1-已认证，2：未通过
     */
    @RequestMapping(value = "company/{id}/certification", method = RequestMethod.GET)
    public Response getCertificateFlag(Integer companyId) {
        return Response.succeed().put("certificationStatus", companyService.getCertificateStatus(companyId));
    }

}
