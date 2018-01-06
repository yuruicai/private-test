package com.sinochem.yunlian.upm.api.web;

import com.sinochem.yunlian.upm.admin.domain.Company;
import com.sinochem.yunlian.upm.api.service.CompanyService;
import com.sinochem.yunlian.upm.api.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huangyang
 * @Description: ${todo}(这里用一句话描述这个类的作用)
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

    @RequestMapping(value = "company/modification" ,method = RequestMethod.POST)
    public Response modify(@RequestBody Company company){
        companyService.update(company);
        return Response.succeed();

    }

}
