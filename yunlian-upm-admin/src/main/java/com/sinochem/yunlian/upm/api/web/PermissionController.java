package com.sinochem.yunlian.upm.api.web;

import com.sinochem.yunlian.upm.admin.domain.AclMenu;
import com.sinochem.yunlian.upm.admin.domain.Operation;
import com.sinochem.yunlian.upm.admin.domain.ResourceData;
import com.sinochem.yunlian.upm.api.service.PermissionService;
import com.sinochem.yunlian.upm.api.service.ResourceService;
import com.sinochem.yunlian.upm.api.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author huangyang
 * @Description: 权限相关api
 * @date 2018/01/10 下午2:02
 */
@Slf4j
@RestController
@RequestMapping("inner/api")
public class PermissionController {


    @Autowired
    private PermissionService permissionService;

    @Autowired
    private ResourceService resourceService;

    @RequestMapping(value = "resource", method = RequestMethod.POST)
    public Response addResource(AclMenu menu) {
        resourceService.add(menu);
        return Response.succeed();
    }

    @RequestMapping(value = "resource/{id}/deletion", method = RequestMethod.GET)
    public Response deleteResource(@PathVariable("id") String id) {
        resourceService.deleteById(id);
        return Response.succeed();
    }

    @RequestMapping(value = "resource/modification", method = RequestMethod.POST)
    public Response modifyResource(AclMenu menu) {
        resourceService.modifyById(menu);
        return Response.succeed();
    }

    @RequestMapping(value = "app/resource",method = RequestMethod.GET)
    public Response getAppResource(String appId){
        List<AclMenu> resource = resourceService.getByAppId(appId);
        return Response.succeed().put("resources",resource);
    }



    /**
     * 根据appKey和typeId查询出该应用下所有的元素/操作记录
     * @param appKey
     * @return
     */
    @RequestMapping(value = "selectElement", method = RequestMethod.GET)
    public List<Operation> selectElement(String appKey,String typeId) {
        return permissionService.selectElement(appKey,typeId);
    }

    /**
     * 新增元素/操作/数据资源:随机生成12位字符串_元素/操作/数据编码
     * @return
     */
    @RequestMapping(value = "randomStr", method = RequestMethod.GET)
    public String randomStr() {
        return permissionService.randomStr();
    }

    /**
     * 保存元素/操作资源
     * @param operation
     * @return
     */
    @RequestMapping(value = "insert", method = RequestMethod.POST)
    public Response insert(@RequestBody Operation operation) {
       int i =  permissionService.insert(operation);
        return i==1?Response.succeed():Response.fail("保存失败");
    }

    /**
     * 根据元素/操作编码删除元素/操作记录
     * @param code
     * @return
     */
    @RequestMapping(value = "deleteOperation", method = RequestMethod.GET)
    public Response updateStatus(String code) {
        int i =  permissionService.updateStatus(code);
        return i==1?Response.succeed():Response.fail("删除失败");
    }

    /**
     * 根据元素/操作编码查询纪录
     * @param code
     * @return
     */
    @RequestMapping(value = "getOne", method = RequestMethod.GET)
    public Operation getOne(String code) {
        Operation operation =  permissionService.getOne(code);
        return operation;
    }

    /**
     * 根据appKey查询出该应用下所有的数据记录
     * @param appKey
     * @return
     */
    @RequestMapping(value = "selectData", method = RequestMethod.GET)
    public List<ResourceData> selectData(String appKey) {
        return permissionService.selectData(appKey);
    }

    /**
     * 根据数据编码删除数据记录
     * @param code
     * @return
     */
    @RequestMapping(value = "deleteData", method = RequestMethod.GET)
    public Response updateStatusOfData(String code) {
        int i =  permissionService.updateStatusOfData(code);
        return i==1?Response.succeed():Response.fail("删除失败");
    }

    /**
     * 保存数据资源
     * @param resourceData
     * @return
     */
    @RequestMapping(value = "insertData", method = RequestMethod.POST)
    public Response insertData(@RequestBody ResourceData resourceData) {
        int i =  permissionService.insertData(resourceData);
        return i==1?Response.succeed():Response.fail("保存失败");
    }

    /**
     * 根据数据编码查询纪录
     * @param code
     * @return
     */
    @RequestMapping(value = "getDataOne", method = RequestMethod.GET)
    public ResourceData getDataOne(String code) {
        ResourceData resourceData =  permissionService.getDataOne(code);
        return resourceData;
    }
}
