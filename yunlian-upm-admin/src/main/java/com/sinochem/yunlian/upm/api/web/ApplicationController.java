package com.sinochem.yunlian.upm.api.web;

import com.sinochem.yunlian.upm.admin.domain.AclApplication;
import com.sinochem.yunlian.upm.admin.domain.AclApplicationExample;
import com.sinochem.yunlian.upm.api.service.ApplicationService;
import com.sinochem.yunlian.upm.api.vo.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.sinochem.yunlian.upm.api.vo.Response;

import java.util.List;

/**
 * @author gaowei
 * @Description: 应用管理页面Controller
 * @date 2018/1/6.17:40
 */
@RestController
@RequestMapping("inner/api/application")
@Slf4j
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    /**
     * 新增记录
     * @param application
     * @return
     */
    @RequestMapping(value = "insert", method = RequestMethod.POST)
    public Response addCompany(@RequestBody AclApplication application) {
        applicationService.insert(application);
        return Response.succeed();
    }

    /**
     * 删除复选框勾选的纪录
     */
    @RequestMapping(value = "delete", method = RequestMethod.GET)
    public Response delete(String[] ids) {
        int i = applicationService.delete(ids);
        return i==0?Response.succeed():Response.fail("操作失败");
    }

    /**
     * 更改角色的状态
     * @param id
     * @return
     */
    @RequestMapping(value = "updateStatus", method = RequestMethod.GET)
    public Response updateStatus(String id) {
        applicationService.updateStatus(id);
        return Response.succeed();
    }

    /**
     * 更改数据
     * @param application
     * @return
     */
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Response update(@RequestBody AclApplication application){
        applicationService.update(application);
        return Response.succeed();
    }

    /**
     * 查询纪录
     * @param id
     * @return
     */
    @RequestMapping(value = "select", method = RequestMethod.GET)
    public AclApplication select(String id) {
        return applicationService.select(id);
    }

    /**
     * 条件查询
     * @param appkey
     * @param name
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(value = "search", method = RequestMethod.GET)
    public Response search(String appkey,
                           String name,
                       @RequestParam(defaultValue = "1",required = true) Integer  page,
                       @RequestParam(defaultValue = "20",required = true) int rows) {
        PageInfo pageInfo = applicationService.search(appkey,name,page,rows);
        return Response.succeed(pageInfo);
    }


}