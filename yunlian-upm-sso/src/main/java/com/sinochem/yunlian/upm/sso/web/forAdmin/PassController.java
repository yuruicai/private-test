package com.sinochem.yunlian.upm.sso.web.forAdmin;

import com.sinochem.yunlian.upm.common.security.Base64;
import com.sinochem.yunlian.upm.sso.cache.SsoCacheFacade;
import com.sinochem.yunlian.upm.sso.constant.UserStatus;
import com.sinochem.yunlian.upm.sso.domain.AclUser;
import com.sinochem.yunlian.upm.sso.password.Checker;
import com.sinochem.yunlian.upm.sso.service.SMSService;
import com.sinochem.yunlian.upm.sso.service.SessionService;
import com.sinochem.yunlian.upm.sso.service.SsoCaptchaService;
import com.sinochem.yunlian.upm.sso.service.UserService;
import com.sinochem.yunlian.upm.sso.util.CookieUtil;
import com.sinochem.yunlian.upm.sso.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhanggm on 2015/12/7.
 */
@Controller
@RequestMapping(value = "/pass/test")
@Slf4j
public class PassController {

    @Resource
    private UserService userService;
    @Resource
    private SsoCaptchaService ssoCaptchaService;
    @Resource
    private SsoCacheFacade ssoCacheFacade;
    @Resource
    private SessionService sessionService;
    @Resource
    private SMSService smsService;

    @RequestMapping(value = "forget", method = RequestMethod.GET)
    public String login(Model model)
            throws IOException {
        return "pass/forgetStep1";
    }

    @RequestMapping(value = "/forget", method = RequestMethod.POST)
    public String forgetPassword(String mobile, String captcha, HttpServletRequest request, Model model) {
        if(!StringUtil.verifyMobileFormat(mobile)){
            model.addAttribute("errMsg", "手机号格式不正确！");
            return "pass/forgetStep1";
        }
        String captchaId = CookieUtil.getCookie(request, "captchaid", String.class);
        model.addAttribute("mobile", mobile);
        Boolean isCorrect = ssoCaptchaService.validateResponseForID(captchaId, captcha);

        if (isCorrect.equals(Boolean.FALSE)) {
            model.addAttribute("errMsg", "验证码错误！");
        } else {
            AclUser aclUser = userService.getByMobile(mobile);
            if (aclUser == null) {
                model.addAttribute("errMsg", "手机号不存在！");
            } else {

                smsService.sendSMS(mobile, "2", "confirm.creditmarket.reset.password", null, "壹化云链");
                ssoCacheFacade.setObject("smsMobile" + captchaId, 300, mobile);
                return "pass/forgetStep2";
            }
        }
        model.addAttribute("isCorrect", isCorrect);
        return "pass/forgetStep1";
    }

    @RequestMapping(value = "reset", method = RequestMethod.GET)
    public String resetPassword(String mobile, String smsCode, HttpServletRequest request, Model model) {
        String captchaId = CookieUtil.getCookie(request, "captchaid", String.class);
        String sMobile = ssoCacheFacade.getObject("smsMobile" + captchaId, String.class);
        model.addAttribute("mobile", mobile);
        if (sMobile == null) {
            return "pass/forgetStep1";
        }
        if (!mobile.equals(sMobile)) {
            model.addAttribute("errMsg", "非法访问！");
            return "pass/forgetStep1";
        }
        AclUser aclUser = userService.getByMobile(mobile);
        if (aclUser == null) {
            model.addAttribute("errMsg", "该手机号不存在！");
            return "pass/forgetStep1";
        }
        if (!(aclUser.getStatus() == UserStatus.ACTIVE.getIndex())) {
            model.addAttribute("errMsg", "该用户已停用！");
            return "pass/forgetStep1";
        }
        if (!smsService.checkCaptcha(mobile, smsCode, "confirm.creditmarket.reset.password", null)) {
            model.addAttribute("errMsg", "验证码错误！");
            return "pass/forgetStep2";
        }
        model.addAttribute("username", aclUser.getLoginName());

        ssoCacheFacade.setObject("resetMobile" + captchaId, 300, mobile);
        return "pass/forgetStep3";
    }

    @RequestMapping(value = "reset", method = RequestMethod.POST)
    public String postResetPassword(String mobile, String password1, String password2,
                                    String checkCode, HttpServletRequest request, Model model) {
        password1 = Base64.decodeToString(password1);
        password2 = Base64.decodeToString(password2);
        String captchaId = CookieUtil.getCookie(request, "captchaid", String.class);
        String msg = "";
        String resetMobile = ssoCacheFacade.getObject("resetMobile" + captchaId, String.class);
        model.addAttribute("mobile", mobile);
        model.addAttribute("checkCode", checkCode);
        // 三分钟
        if(!mobile.equals(resetMobile)){
            msg = "非法请求";
        } else{
            AclUser aclUser = userService.getByMobile(mobile);
            if (aclUser == null) {
                msg = "手机号不存在！";
            } else {
                model.addAttribute("username", aclUser.getLoginName());
                msg = userService.updatePassword(aclUser, password1, password2);
                //找回密码成功后，将所有登录状态置为失效
                if(msg == null){
                    sessionService.executeLogoutByUserId(aclUser.getId());
                }
            }
        }
        if (msg == null) {
            model.addAttribute("msg", msg);
            String ssoHost = request.getHeader("Host");
            if(!ssoHost.startsWith("http")){
                ssoHost = "http://" + ssoHost;
            }
            model.addAttribute("ssoHost", ssoHost);
            ssoCacheFacade.delete("resetMobile" + captchaId);
            ssoCacheFacade.delete("smsMobile" + captchaId);
            return "pass/success";
        } else {
            model.addAttribute("errMsg", msg);
        }
        return "pass/forgetStep3";
    }

    @RequestMapping(value = "verifyPassFormat")
    @ResponseBody
    public Map<String, Object> verifyPassFormat(String username,String password){
        Map<String, Object> relMap = new HashMap<String, Object>();
        password = Base64.decodeToString(password);

        String ret = Checker.basic(username, password);
        if (ret != null) {
            relMap.put("status", 500);
            relMap.put("errorMsg", ret);
        }else{
            relMap.put("status", 200);
        }
        return relMap;
    }

}
