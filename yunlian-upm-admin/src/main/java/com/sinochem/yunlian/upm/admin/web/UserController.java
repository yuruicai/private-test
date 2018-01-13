package com.sinochem.yunlian.upm.admin.web;

import com.sinochem.yunlian.upm.admin.bean.UserListBean;
import com.sinochem.yunlian.upm.admin.constant.SMSStatus;
import com.sinochem.yunlian.upm.admin.constant.UserStatus;
import com.sinochem.yunlian.upm.admin.constant.UserType;
import com.sinochem.yunlian.upm.admin.model.IdNameBean;
import com.sinochem.yunlian.upm.admin.model.react.FengError;
import com.sinochem.yunlian.upm.admin.model.react.FengResult;
import com.sinochem.yunlian.upm.admin.service.SMSService;
import com.sinochem.yunlian.upm.admin.service.UserService;
import com.sinochem.yunlian.upm.common.security.Base64;
import com.sinochem.yunlian.upm.filter.annotation.RequiresPermissions;
import com.sinochem.yunlian.upm.filter.util.CollectionUtils;
import com.sinochem.yunlian.upm.filter.util.UserUtils;
import com.sinochem.yunlian.upm.tools.UpmAuthService;
import com.sinochem.yunlian.upm.util.AjaxResultUtil;
import com.sinochem.yunlian.upm.util.Page;
import com.google.common.collect.Lists;
import com.sinochem.yunlian.upm.util.SecurityLevel;
import com.sinochem.yunlian.upm.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Resource
    private UserService userService;
    @Resource
    private UpmAuthService upmAuthService;
    @Resource
    private SMSService smsService;

    @RequiresPermissions("user:admin:list")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView userlist(UserListBean userListBean, Page page, Model model) {
        if(page == null){
            page = new Page(1, 20);
        }
        List<AclUser> aclUserList = userService.getUserListByCriteria(userListBean, page, null);
        ModelAndView mv = new ModelAndView("/user/list");
        mv.addObject("userListBean", userListBean);
        mv.addObject("page", page);
        mv.addObject("userList", AclUser.richUser(aclUserList));
        mv.addObject("userTypes", UserType.values());
        return mv;
    }

    @RequiresPermissions("user:admin:save")
    @RequestMapping(value = "/save", method = RequestMethod.GET)
    public ModelAndView saveUser(AclUser aclUser) {
        ModelAndView mv = new ModelAndView("/user/edit");
        AclUser user = userService.saveUser(aclUser);
        mv.addObject("user", user);
        return mv;
    }

    @RequiresPermissions("user:admin:addUserCheck")
    @RequestMapping(value = "/addUserCheck", method = RequestMethod.POST)
    @ResponseBody
    public FengResult addUserCheck(@ModelAttribute AclUser user, HttpServletRequest request) {

        FengResult result = userService.checkUserExists(user);
        return result;
    }

    @RequiresPermissions("user:admin:addUser")
    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> addUser(@ModelAttribute AclUser user) {
        FengResult result = userService.checkUserExists(user);
        if(!result.isSuccess()){
            return AjaxResultUtil.createAjaxFailureMap(((FengError)result.getError().get(0)).getMessage());
        }
        userService.saveUser(user);
        return AjaxResultUtil.createAjaxSuccessMap("新增用户成功");

    }

    @RequiresPermissions("user:admin:updateUser")
    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> updateUser(@ModelAttribute AclUser user) {
        AclUser userResult = userService.getById(user.getId());
        if(userResult==null){
            return AjaxResultUtil.createAjaxFailureMap("用户不存在");
        }
        userService.updateSelective(user);
        return AjaxResultUtil.createAjaxSuccessMap("修改成功");
    }

    @RequiresPermissions("user:admin:mobile")
    @RequestMapping("/mobile")
    @ResponseBody
    public Map<String, Object> mobile(String id) {
        AclUser aclUser = userService.getById(id);
        return AjaxResultUtil.createAjaxSuccessMap(aclUser.getDecryptMobile());
    }

    @RequiresPermissions("user:admin:resetPass")
    @RequestMapping("/resetPass")
    @ResponseBody
    public Map<String, Object> resetPassBySys(String id) {
        AclUser aclUser = userService.getById(id);
        String newPassword = SecurityUtils.randomPassword(SecurityLevel.GOOD);
        String msg = null;
        if (!(aclUser.getStatus() == UserStatus.ACTIVE.getIndex())) {
            msg = "该用户已停用，不允许重置密码";
        } else {
            msg = userService.updatePasswordByAclUser(aclUser, newPassword);
            LOG.info("******user {} resetPassword. newPassword:{}",aclUser.getLoginName(),newPassword);
        }
        if (msg == null) {
            SMSStatus smsStatus = smsService.sendSMS(aclUser.getDecryptMobile(), "2", "creditmanager.reset.password", null, "壹化云链", "<pwd" + newPassword + "pwd>");
            LOG.info("resetPassBySys {} {}", aclUser.getLoginName(), smsStatus!=null ? smsStatus.getKey() : "error");
            return AjaxResultUtil.createAjaxSuccessMap("已重置，密码通过短信发给用户");
        } else {
            return AjaxResultUtil.createAjaxSuccessMap(msg);
        }
    }

    @RequestMapping("modPassword")
    public String modPassword(String oldPassword, String newPassword1, String newPassword2, HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
        AclUser aclUser = userService.getById(UserUtils.getUser().getId());

        model.addAttribute("username", aclUser.getLoginName());
        if (oldPassword == null) {
            return "user/modPassword";
        }

        oldPassword = Base64.decodeToString(oldPassword);
        newPassword1 = Base64.decodeToString(newPassword1);
        newPassword2 = Base64.decodeToString(newPassword2);

        if (userService.executeVerify(aclUser.getLoginName(), oldPassword) == null) {
            model.addAttribute("errMsg", "旧密码输入错误");
            return "user/modPassword";
        }

        String msg = userService.updatePassword(aclUser, newPassword1, newPassword2);

        if (msg == null) {
            //退出所有登录
            upmAuthService.logoutAll(aclUser.getId());
            model.addAttribute("msg", "修改成功");
            return "user/modPassword";
        } else {
            model.addAttribute("errMsg", msg);
            return "user/modPassword";
        }
    }

    @RequiresPermissions("user:admin:freeze")
    @RequestMapping("/freeze")
    @ResponseBody
    public Map<String, Object> freeze(String id) {
        userService.updateFreezeUser(id);
        return AjaxResultUtil.createAjaxSuccessMap("用户已冻结");
    }

    @RequiresPermissions("user:admin:recovery")
    @RequestMapping("/recovery")
    @ResponseBody
    public Map<String, Object> recovery(String id) {
        userService.updateRecovery(id);
        return AjaxResultUtil.createAjaxSuccessMap("用户已解冻");
    }

    @RequiresPermissions("user:admin:disable")
    @RequestMapping("/disable")
    @ResponseBody
    public Map<String, Object> disable(String id) {
        userService.updateDisable(id);
        return AjaxResultUtil.createAjaxSuccessMap("用户已停用");

    }

    @RequiresPermissions("user:admin:enable")
    @RequestMapping("/enable")
    @ResponseBody
    public Map<String, Object> enable(String id) {
        userService.updateRecovery(id);
        return AjaxResultUtil.createAjaxSuccessMap("重新启用虚拟账户成功");
    }

    @RequestMapping(value = "profile", method = RequestMethod.GET)
    public String profile(HttpServletRequest request, Model model)  {
        model.addAttribute("user", userService.getById(UserUtils.getUser().getId()));
        return "user/profile";
    }

    @RequiresPermissions("user:admin:view")
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public String profile(String id,Model model)  {
        model.addAttribute("user", userService.getById(id));
        return "user/view";
    }

    @RequiresPermissions("user:admin:delUser")
    @RequestMapping(value = "/delUser", method = RequestMethod.POST)
    public String delUser(String id,Model model)  {
        model.addAttribute("user", userService.getById(id));
        return "redirect:/user/list";
    }

    @RequestMapping("/users.ajax")
    @ResponseBody
    public Map<String, Object> allUser() {
        List<AclUser> aclUsers = userService.getAllUserList();
        List<IdNameBean> users = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(aclUsers)) {
            for (AclUser aclUser : aclUsers) {
                IdNameBean bean = new IdNameBean();
                bean.setId(aclUser.getId());
                bean.setName(aclUser.getName()+"("+aclUser.getLoginName()+")");
                users.add(bean);
            }
        }
        return AjaxResultUtil.success(users);
    }
}
