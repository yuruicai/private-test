package com.sinochem.yunlian.upm.api.web;

import com.sinochem.yunlian.upm.admin.domain.Company;
import com.sinochem.yunlian.upm.api.service.CompanyUserManageService;
import com.sinochem.yunlian.upm.api.vo.PageInfo;
import com.sinochem.yunlian.upm.api.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author gaowei
 * @Description: 企业成员管理 controller
 * @date 2018/1/9.10:23
 */
@RestController
@RequestMapping("inner/api/companyManage")
@Slf4j
public class CompanyUserManageController {

    @Autowired
    private CompanyUserManageService companyUserManageService;

    /**
     * 根据企业名称获得企业列表,可模糊查询
     * @param companyName  用户输入信息
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(value = "getListOfCompany", method = RequestMethod.GET)
    public Response getListOfCompany(String companyName,
                                     @RequestParam(defaultValue = "1",required = true) Integer  page,
                                     @RequestParam(defaultValue = "20",required = true) int rows) {
        PageInfo pageInfo = companyUserManageService.getListOfCompany(companyName, page, rows);
        return Response.succeed(pageInfo);
    }

    /**
     * 根据企业id更新状态
     * @param id
     * @return
     */
    @RequestMapping(value = "updateCompanyStatus", method = RequestMethod.GET)
    public Response updateCompanyStatus(String id){
        companyUserManageService.updateCompanyStatus(id);
        return Response.succeed();
    }

    /**
     * 根据企业id查询当前企业的所有成员
     * @param id
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(value = "getListOfUser", method = RequestMethod.GET)
    public Response getListOfUser(String id,
                                     @RequestParam(defaultValue = "1",required = true) Integer  page,
                                     @RequestParam(defaultValue = "20",required = true) int rows) {
        PageInfo pageInfo = companyUserManageService.getListOfUser(id, page, rows);
        return Response.succeed(pageInfo);
    }



    /**
     * 根据userId删除企业成员
     * @param id
     * @return
     */
    @RequestMapping(value = "delete", method = RequestMethod.GET)
    public Response delete(String id) {
        int i = companyUserManageService.updateStatus(id);
        return i==1?Response.succeed():Response.fail("删除失败");
    }

    /**
     * 根据userId设置当前用户为管理员
     * @param id
     * @return
     */
    @RequestMapping(value = "updateAdmin", method = RequestMethod.GET)
    public Response updateAdmin(String id) {
        int i = companyUserManageService.updateAdmin(id);
        return i==1?Response.succeed():Response.fail("设置失败");
    }

    /**
     * 条件查询所有企业用户成员
     * @param loginName 根据用户名查询
     * @param mobile    根据手机号查询
     * @param name      根据姓名查询
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(value = "getAllListOfUser", method = RequestMethod.GET)
    public Response getAllListOfUser(String loginName,
                                     String mobile,
                                     String name,
                                  @RequestParam(defaultValue = "1",required = true) Integer  page,
                                  @RequestParam(defaultValue = "20",required = true) int rows) {
        PageInfo pageInfo = companyUserManageService.getAllListOfUser(loginName,mobile,name, page, rows);
        return Response.succeed(pageInfo);
    }
}
