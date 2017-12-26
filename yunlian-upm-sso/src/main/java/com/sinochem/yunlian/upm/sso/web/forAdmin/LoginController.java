package com.sinochem.yunlian.upm.sso.web.forAdmin;

import com.sinochem.yunlian.upm.common.security.Base64;
import com.sinochem.yunlian.upm.sso.domain.AclUser;
import com.sinochem.yunlian.upm.sso.model.MtCookie;
import com.sinochem.yunlian.upm.sso.model.Session;
import com.sinochem.yunlian.upm.sso.service.LoginService;
import com.sinochem.yunlian.upm.sso.service.SessionService;
import com.sinochem.yunlian.upm.sso.service.TimesLimitService;
import com.sinochem.yunlian.upm.sso.service.UserService;
import com.sinochem.yunlian.upm.sso.util.*;
import com.sinochem.yunlian.upm.sso.constant.UserStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登陆登出
 */
@Controller
@RequestMapping(value = "/")
public class LoginController {
    private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);

    @Resource
    private SessionService sessionService;
    @Resource
    private LoginService loginService;
    @Resource
    private TimesLimitService timesLimitService;
    @Resource
    private UserService userService;

    /**
     * @param request
     * @return
     * @throws IOException
     * @example http://localhost:8080/auth?return=http://sg.sankuai.com
     */
    @RequestMapping(value = "auth", method = RequestMethod.GET)
    public String defaultLogin(String appkey, Integer timestamp, String sign,
                               HttpServletRequest request, Model model, HttpServletResponse response) throws IOException {
        String service = SSOUtil.getRedirectService(request);
        String sid = SSOUtil.getSid(request);
        TraceContext.put("appkey", appkey);
        TraceContext.put("timestamp", timestamp);
        TraceContext.put("sign", sign);
        if (sid != null) {
            if (sessionService.checkSessionAndRefresh(sid)) {
                return loginService.authPass(sid, service, request, response, model);
            }
        }
        model.addAttribute("appkey", appkey);
        model.addAttribute("service", service);

        return loginView(model, appkey);
    }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String login(String key, String appkey, HttpServletRequest request, HttpServletResponse response, Model model)
            throws IOException {
        model.addAttribute("appkey", appkey);
        model.addAttribute("key", key);

        String token = SSOUtil.getSid(request);
        String service = SSOUtil.getRedirectService(request);
        model.addAttribute("service", service);

        if (token != null) {
            if (sessionService.checkSessionAndRefresh(token)) {
                return loginService.authPass(token, service, request, response, model);
            }
        }

        return loginView(model, appkey);
    }

    private String loginView(Model model, String appkey) {
        try {
            String username = (String)TraceContext.get(Constants.CONTEXT_USER_NAME);
            boolean needCaptcha = loginService.executeCurrentNeedCaptcha(username);
            TraceContext.put("needCaptcha", needCaptcha);
            model.addAttribute("needCaptcha", needCaptcha ? "true" : "false");

            Boolean isMobile = (Boolean) TraceContext.get(Constants.CONTEXT_ISMOBILE_KEY);
            if (isMobile != null && isMobile) {
                return "auth/loginMobile";
            }else{
                return "auth/login";
            }
        } catch (Exception e) {
            LOG.error("", e);
            return "auth/login";
        }
    }

    /**
     * 登录验证
     *
     * @param username 可能是login或mobile
     * @param password
     * @param appkey
     * @param service
     * @param captcha
     * @param key
     * @param request
     * @param response
     * @param model
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String auth(String username, String password, String appkey, String service, String captcha, String key,
                       HttpServletRequest request, HttpServletResponse response, Model model)
            throws IOException {
        if (service == null) {
            LOG.info("no service from post, get again...");
            service = SSOUtil.getRedirectService(request);
        }
        model.addAttribute("service", service);
        model.addAttribute("appkey", appkey);
        model.addAttribute("key", key);

        //参数校验
        if(StringUtil.isBlank(username) || StringUtil.isBlank(password)){
            model.addAttribute("error", "用户名或密码不能为空");
            TraceContext.put("status", Status.fail);
            return loginView(model, appkey);
        }
        password = Base64.decodeToString(password);

        if(timesLimitService.isIpLoginTimesOver()){
            model.addAttribute("error", "ip错误次数过多");
            TraceContext.put("status", Status.fail);
            return loginView(model, appkey);
        }

        Boolean isMobile = (Boolean) TraceContext.get(Constants.CONTEXT_ISMOBILE_KEY);
        // freeze
        boolean freeze = timesLimitService.executeIsFreeze(username);
        TraceContext.put("freeze", freeze);
        // TODO mobile also freeze
        if (freeze) {
            model.addAttribute("error", "由于重试过多，账号已临时冻结，请一小时后再试或者找回密码");
            TraceContext.put("status", Status.fail);
            return loginView(model, appkey);
        }

        //判断用户是否存在
        AclUser aclUser = userService.getByLoginName(username);
        if(aclUser == null){
            model.addAttribute("error", "用户不存在");
            TraceContext.put("status", Status.fail);
            return loginView(model, appkey);
        }

        //判断用户状态
        if(aclUser.getStatus() != UserStatus.ACTIVE.getIndex()){
            model.addAttribute("error", "用户已" + UserStatus.getName(aclUser.getStatus()));
            TraceContext.put("status", Status.fail);
            return loginView(model, appkey);
        }

        Integer timeout = Constants.SESSION_TIMEOUT;

        TraceContext.put("timeout", timeout);
        TraceContext.put("username", username);

        //生成token
        String token = SSOUtil.generateSid();
        LOG.info("generate new token " + token);
        TraceContext.put("token", token);

        //设置登录cookie
        MtCookie cookie = MtCookie.newCookie().value(token);
        timeout = (timeout != null && timeout > 0 ? timeout : Constants.SESSION_COOKIE_TIMEOUT);
        LOG.info("set cookie {} max-age {}", new Object[]{token, timeout});
        cookie.setMaxAge(timeout);
        //cookie.setSecure(true);
        cookie.saveTo(request, response);

        //设置登录名cookie
        MtCookie userCookie = new MtCookie("username").value(username);
        userCookie.setHttpOnly(false);
        userCookie.saveTo(request, response);

        //验证图片验证码
        boolean needCaptcha = loginService.executeCurrentNeedCaptcha(aclUser.getLoginName());
//        TraceContext.put("needCaptcha", needCaptcha);
//        model.addAttribute("needCaptcha", needCaptcha ? "true" : "false");
//        if (needCaptcha) {
//            String ret = loginService.verifyCaptcha(request, captcha, username, service);
//            if(ret != null){
//                model.addAttribute("error", ret);
//                return loginView(model, appkey);
//            }
//        }

        try {
            String loginIp = (String) TraceContext.get(Constants.CONTEXT_IP_KEY);
//            if(!sessionService.executeLogin(username, password)) {
//                TraceContext.put("status", Status.fail);
//                int freezeLimit = timesLimitService.executeFreezeLimit(username);
//                TraceContext.put("freezeLimit", freezeLimit);
//                model.addAttribute("freezeLimit", freezeLimit);
//                if (freezeLimit != -1) {
//                    model.addAttribute("error", "密码错误，请重新输入，多次重试将临时冻结账号，剩余重试次数" + freezeLimit);
//                } else {
//                    model.addAttribute("error", "密码错误，请重新输入");
//                }
//                return loginView(model, appkey);
//            }

            sessionService.saveSession(token, aclUser, timeout);
            timesLimitService.increaseUsernameSuccessLoginTimes(username);

            LOG.info("{} auth ok from {} for {} with {} timeout {}", new Object[]{aclUser.getLoginName(), loginIp, service, token, timeout});
            TraceContext.put("username", aclUser.getLoginName());

            return loginService.authPass(token, service, request, response, model);
        } catch (Exception e) {
            LOG.error("", e);
            model.addAttribute("error", e.getMessage());
            TraceContext.put("status", Status.exception);
            return loginView(model, appkey);
        }
    }

    @RequestMapping(value = "illegalauth")
    public String illegalauth(String from, Model model) {
        System.out.println(from);
        model.addAttribute("from", from);
        return "auth/illegal";
    }

    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        String service = request.getParameter("service");
        try {
            String sid = SSOUtil.getSid(request);
            if (sid != null) {
                sessionService.executeLogout(sid);
                SSOUtil.removeSid(request, response);
            }
        } catch (Exception e) {
            TraceContext.put("status", Status.exception);
            LOG.info(e.getMessage(), e);
        }
        if(StringUtil.isNotBlank(service)) {
            return "redirect:" + service;
        }else{
            return "redirect:/login";
        }
    }

    @RequestMapping(value = "logoutAll", method = RequestMethod.GET)
    public String logoutAll(HttpServletRequest request, HttpServletResponse response, Model model) {
        String service = request.getParameter("service");
        try {
            String sid = SSOUtil.getSid(request);
            if (sid != null) {
                Session session = sessionService.getSession(sid);
                sessionService.executeLogout(sid);
                if (session != null) {
                    sessionService.executeLogout(session.getUserId());
                }
                SSOUtil.removeSid(request, response);
            }
        } catch (Exception e) {
            TraceContext.put("status", Status.exception);
            LOG.info(e.getMessage(), e);
        }

        String appkey = request.getParameter("appkey");
        String key = request.getParameter("key");
        if(appkey != null){
            model.addAttribute("appkey", appkey);
        }
        if(key !=null){
            model.addAttribute("key", key);
        }
        if(service !=null){
            model.addAttribute("return", service);
        }

        return "redirect:/login";
    }

    @RequestMapping(value = "logout/{sid}", method = RequestMethod.GET)
    public String logoutWithSid(@PathVariable String sid, HttpServletRequest request,
                                HttpServletResponse response) {
        String service = request.getParameter("service");
        try {
            if (sid != null) {
                sessionService.executeLogout(sid);
                SSOUtil.removeSid(request, response);
            }
        } catch (Exception e) {
            TraceContext.put("status", Status.exception);
            LOG.info(e.getMessage(), e);
        }
        return "redirect:/login"
                + (service != null && !service.trim().isEmpty() ? "?return=" + service : "");
    }

}
