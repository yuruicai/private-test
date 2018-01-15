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
     * 根据appId和typeId查询出该应用下所有的元素/操作记录
     * @param appId
     * @return
     */
    @RequestMapping(value = "selectElement", method = RequestMethod.GET)
    public List<Operation> selectElement(String appId,String typeId) {
        return permissionService.selectElement(appId,typeId);
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
    @RequestMapping(value = "saveOperation", method = RequestMethod.POST)
    public Response saveOperation(@RequestBody Operation operation) {
       int i =  permissionService.saveOperation(operation);
        return i==1?Response.succeed():Response.fail("保存失败");
    }

    /**
     * 根据元素/操作数据id删除元素/操作记录
     * @param id
     * @return
     */
    @RequestMapping(value = "deleteOperation", method = RequestMethod.GET)
    public Response updateStatus(String id) {
        int i =  permissionService.updateStatus(id);
        return i==1?Response.succeed():Response.fail("删除失败");
    }

    /**
     * 根据元素/操作id查询纪录
     * @param id
     * @return
     */
    @RequestMapping(value = "getOne", method = RequestMethod.GET)
    public Operation getOne(String id) {
        Operation operation =  permissionService.getOne(id);
        return operation;
    }

    /**
     * 根据appId查询出该应用下所有的数据记录
     * @param appId
     * @return
     */
    @RequestMapping(value = "selectData", method = RequestMethod.GET)
    public List<ResourceData> selectData(String appId) {
        return permissionService.selectData(appId);
    }

    /**
     * 根据数据id删除数据记录
     * @param id
     * @return
     */
    @RequestMapping(value = "deleteData", method = RequestMethod.GET)
    public Response updateStatusOfData(String id) {
        int i =  permissionService.updateStatusOfData(id);
        return i==1?Response.succeed():Response.fail("删除失败");
    }

    /**
     * 保存数据资源
     * @param resourceData
     * @return
     */
    @RequestMapping(value = "saveData", method = RequestMethod.POST)
    public Response saveData(@RequestBody ResourceData resourceData) {
        int i =  permissionService.saveData(resourceData);
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
