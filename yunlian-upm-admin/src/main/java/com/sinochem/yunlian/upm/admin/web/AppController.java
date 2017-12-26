package com.sinochem.yunlian.upm.admin.web;

import com.sinochem.yunlian.upm.admin.bean.AppManagerBean;
import com.sinochem.yunlian.upm.admin.bean.ApplicationBean;
import com.sinochem.yunlian.upm.admin.domain.AclApplication;
import com.sinochem.yunlian.upm.admin.service.ApplicationService;
import com.sinochem.yunlian.upm.admin.service.UpmService;
import com.sinochem.yunlian.upm.filter.annotation.RequiresPermissions;
import com.sinochem.yunlian.upm.util.AjaxResultUtil;
import com.sinochem.yunlian.upm.admin.domain.AclRole;
import com.sinochem.yunlian.upm.admin.service.RoleService;
import com.sinochem.yunlian.upm.util.Page;
import com.sinochem.yunlian.upm.util.UpmCacheUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/app")
public class AppController {
    private static final Logger LOG = LoggerFactory.getLogger(AppController.class);

    @Resource
    private ApplicationService applicationService;

    @Resource
    private RoleService roleService;
    @Resource
    private UpmService upmService;

    @RequiresPermissions("app:create")
    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String create() {
        return "app/create";
    }

    @RequiresPermissions("app:edit")
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public String edit(String id, Model model) {
        AclApplication application = applicationService.getById(id);
        model.addAttribute("application", application);
        return "app/edit";
    }

    @RequiresPermissions("app:save")
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String save(AclApplication application, Model model) {
        //AclApplication application = new AclApplication();
        applicationService.saveOrUpdate(application);
        model.addAttribute("msg", "创建成功");
        return "redirect:/app/list";
    }

    @RequiresPermissions("app:list")
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list(Page page,Model model) {
        if(null == page){
            page = new Page(1, 10);
        }
        List<ApplicationBean> applicationBeans = new ArrayList<ApplicationBean>();
        if(upmService.isSupper(UpmCacheUtil.getCurrentUserId())){
            applicationBeans = applicationService.getApplicationBean(page);
        }else{
            applicationBeans = applicationService.selectApps4Manager(UpmCacheUtil.getCurrentUserId(), page);
        }
        model.addAttribute("page", page);
        model.addAttribute("apps",applicationBeans);
        return "app/list";
    }

    @RequiresPermissions("app:delete")
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> delete(String id) {
        applicationService.delete(id);
        return AjaxResultUtil.createAjaxSuccessMap();
    }

    @RequiresPermissions("app:getRoles")
    @RequestMapping("getRoles")
    @ResponseBody
    public Map<String, Object> getRoles(String appId) {
        List<AclRole> aclRoles = roleService.getListByApp(appId);
        Map<String, Object> map = AjaxResultUtil.createAjaxSuccessMap();
        map.put("data", roleService.makeRoleBean(aclRoles));
        return map;
    }

    @RequiresPermissions("app:changeRole")
    @RequestMapping("changeRole")
    @ResponseBody
    public Map<String, Object> changeRole(String appId, String roleId) {
        applicationService.updateRole(appId, roleId);
        return AjaxResultUtil.createAjaxSuccessMap();
    }

    @RequiresPermissions("app:managerList")
    @RequestMapping("managerList")
    public ModelAndView managerList(Page page,String appId) {
        if(null == page){
            page = new Page(1, 10);
        }
        ModelAndView mv = new ModelAndView("/app/managerList");
        ApplicationBean applicationBean = applicationService.getApplicationBean(appId);
        mv.addObject("applicationBean", applicationBean);
        List<AppManagerBean> managers = applicationService.getAppManager(appId);
        mv.addObject("managers",managers );
        mv.addObject("page", page);
        return mv;
    }

    @RequiresPermissions("app:addAppManager")
    @RequestMapping("addAppManager")
    @ResponseBody
    public Map<String, Object> addAppManager(String appId, String userId){
        String msg = applicationService.addAppManager(appId, userId);
        if(msg != null){
            return AjaxResultUtil.createAjaxFailureMap(msg);
        }
        return AjaxResultUtil.createAjaxSuccessMap();
    }

    @RequiresPermissions("app:deleteAppManager")
    @RequestMapping("deleteAppManager")
    @ResponseBody
    public Map<String, Object> deleteAppManager(String appId, String userId){
        applicationService.deleteAppManager(appId, userId);
        return AjaxResultUtil.createAjaxSuccessMap();
    }

    @RequestMapping("queryByAppkey.ajax")
    @ResponseBody
    public Map<String, Object> queryByAppkey(String appkey){
        String appId = applicationService.getIdByAppkey(appkey);
        return AjaxResultUtil.success(appId);
    }

    @RequestMapping("initDefaultRole")
    @ResponseBody
    public Map<String, Object> initDefaultRole(){
        applicationService.initDefaultRole();
        return AjaxResultUtil.createAjaxSuccessMap();
    }


    @RequestMapping(value="/uploadImage", method= RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> uploadImageFile(String appId,
                                               String imageId,
                                               @RequestParam(name = "image", required = false) MultipartFile image) throws Exception{
        String imageUrl = applicationService.uploadImage(appId, imageId, image);
        return AjaxResultUtil.success(imageUrl);
    }

}
