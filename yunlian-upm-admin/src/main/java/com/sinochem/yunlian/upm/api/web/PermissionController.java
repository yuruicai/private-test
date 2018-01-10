package com.sinochem.yunlian.upm.api.web;

import com.sinochem.yunlian.upm.admin.domain.AclMenu;
import com.sinochem.yunlian.upm.api.service.ResourceService;
import com.sinochem.yunlian.upm.api.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
}
