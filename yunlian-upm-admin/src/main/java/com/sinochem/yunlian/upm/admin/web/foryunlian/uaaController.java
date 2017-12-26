package com.sinochem.yunlian.upm.admin.web.foryunlian;


import com.sinochem.yunlian.upm.admin.bean.RoleListBean;
import com.sinochem.yunlian.upm.admin.domain.AclRole;
import com.sinochem.yunlian.upm.admin.service.RoleService;
import com.sinochem.yunlian.upm.admin.service.UserRoleService;
import com.sinochem.yunlian.upm.common.reflect.TypeReference;
import com.sinochem.yunlian.upm.common.util.ApiUtil;
import com.sinochem.yunlian.upm.util.AjaxResultUtil;
import com.sinochem.yunlian.upm.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/api/")
public class uaaController {
    private static final Logger LOG = LoggerFactory.getLogger(uaaController.class);

    @Resource
    private UserRoleService userRoleService;
    @Resource
    private RoleService roleService;

    //添加用户角色
    @RequestMapping(value = "/saveUserRole", method = RequestMethod.POST)
    @ResponseBody
    public Object saveUserRole(HttpServletRequest request){
        Map<String, Object> data = new HashMap<String, Object>();
        try {
            Map<String, String> params =
                    ApiUtil.parse(request.getReader(), new TypeReference<Map<String, String>>() {});
            //获取参数
            String roleId = params.get("roleId");
            String userId = params.get("userId");
            Integer rltType = Integer.parseInt(params.get("rltType"));
            String operatorId = params.get("operatorId");
            //参数校验
            if(StringUtil.isBlank(roleId) || StringUtil.isBlank(userId)|| StringUtil.isBlank(rltType) || StringUtil.isBlank(operatorId)){
                return AjaxResultUtil.resultAjax("添加用户角色关系表的参数不全",11104,data);
            }

            String userRoleRltId = userRoleService.addRltByUserAndRole(userId, roleId, operatorId, rltType);
            if(userRoleRltId==null || StringUtil.isBlank(userRoleRltId) ){
                return AjaxResultUtil.resultAjax("添加角色失败", 10002, data);
            }
            return AjaxResultUtil.resultSuccessAjax(data);
        } catch (IOException e) {
            LOG.error("", e);
            return AjaxResultUtil.resultAjax("请求数据格式错误或服务内部异常", 10002, data);
        }
    }

    //编辑用户角色
    @RequestMapping(value = "/updateUserRole", method = RequestMethod.POST)
    @ResponseBody
    public Object updateUserRole(HttpServletRequest request){
        Map<String, Object> data = new HashMap<String, Object>();
        try {
            Map<String, String> params =
                    ApiUtil.parse(request.getReader(), new TypeReference<Map<String, String>>() {});
            //获取参数
            String roleId = params.get("roleId");
            String userId = params.get("userId");
            Integer rltType = Integer.parseInt(params.get("rltType"));
            String operatorId = params.get("operatorId");
            //参数校验
            if(StringUtil.isBlank(roleId) || StringUtil.isBlank(userId)|| StringUtil.isBlank(rltType) || StringUtil.isBlank(operatorId)){
                return AjaxResultUtil.resultAjax("修改用户角色关系表的参数不全",11104,data);
            }

            String userRoleRltId = userRoleService.addRltByUserAndRole(userId,roleId,operatorId,rltType);
            if(userRoleRltId==null || StringUtil.isBlank(userRoleRltId) ){
                return AjaxResultUtil.resultAjax("添加角色失败", 10002, data);
            }
            return AjaxResultUtil.resultSuccessAjax(data);
        } catch (IOException e) {
            LOG.error("", e);
            return AjaxResultUtil.resultAjax("请求数据格式错误或服务内部异常", 10002, data);
        }
    }

    //删除用户角色
    @RequestMapping(value = "/deleteUserRole",method = RequestMethod.POST)
    @ResponseBody
    public Object deleteUserRole(HttpServletRequest request) {
        Map<String, Object> data = new HashMap<String, Object>();
        try {
            Map<String, String> params =
                    ApiUtil.parse(request.getReader(), new TypeReference<Map<String, String>>() {});
            //获取参数
            String roleId = params.get("roleId");
            String userId = params.get("userId");

            if(StringUtil.isBlank(roleId) || StringUtil.isBlank(userId)){
                return AjaxResultUtil.resultAjax("删除用户角色关系表的参数不全",11104,data);
            }

            userRoleService.deleteRltByUserAndRole(userId, roleId, null, null);
            return AjaxResultUtil.resultSuccessAjax(data);
        } catch (IOException e) {
            LOG.error("", e);
            return AjaxResultUtil.resultAjax("请求数据格式错误或服务内部异常", 10002, data);
        }
    }

    //获取角色列表
    @RequestMapping(value = "/roleList", method = RequestMethod.POST)
    @ResponseBody
    public Object roleList(HttpServletRequest request) {
        Map<String, Object> data = new HashMap<String, Object>();
        try {
            Map<String, String> params =
                    ApiUtil.parse(request.getReader(), new TypeReference<Map<String, String>>() {});
            //获取参数
            String applicationId = params.get("applicationId");
            RoleListBean roleListBean = new RoleListBean();
            roleListBean.setApplicationId(applicationId);
            if(StringUtil.isBlank(applicationId)){
                return AjaxResultUtil.resultAjax("应用标识applicationId不能为空",11104,data);
            }
            List<AclRole> roleList = roleService.getList(roleListBean);
            if(roleList==null || roleList.size()==0){
                return AjaxResultUtil.resultAjax("无法查到角色信息，请核对应用标识是否正确",11104,data);
            }
            data.put("roleList",roleList);
            return AjaxResultUtil.resultSuccessAjax(data);
        } catch (IOException e) {
            LOG.error("", e);
            return AjaxResultUtil.resultAjax("请求数据格式错误或服务内部异常", 10002, data);
        }
    }
}
