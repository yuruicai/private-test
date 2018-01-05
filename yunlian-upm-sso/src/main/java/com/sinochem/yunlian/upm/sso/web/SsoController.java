package com.sinochem.yunlian.upm.sso.web;

import com.sinochem.yunlian.upm.common.reflect.TypeReference;
import com.sinochem.yunlian.upm.common.util.ApiUtil;
import com.sinochem.yunlian.upm.sso.constant.UserStatus;
import com.sinochem.yunlian.upm.sso.domain.AclUser;
import com.sinochem.yunlian.upm.sso.model.Session;
import com.sinochem.yunlian.upm.sso.model.UserForSeesion;
import com.sinochem.yunlian.upm.sso.service.LoginService;
import com.sinochem.yunlian.upm.sso.service.SessionService;
import com.sinochem.yunlian.upm.sso.service.TimesLimitService;
import com.sinochem.yunlian.upm.sso.service.UserService;
import com.sinochem.yunlian.upm.sso.util.*;
import com.sinochem.yunlian.upm.sso.util.RSAUtils.AESUtils;
import com.sinochem.yunlian.upm.sso.util.RSAUtils.RSAUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 登陆登出
 */
@Controller
@RequestMapping(value = "/api/")
public class SsoController {
    private static final Logger LOG = LoggerFactory.getLogger(SsoController.class);

    @Resource
    private SessionService sessionService;
    @Resource
    private LoginService loginService;
    @Resource
    private TimesLimitService timesLimitService;
    @Resource
    private UserService userService;

    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public Object auth(HttpServletRequest request,HttpServletResponse response) {

        Map<String, Object> data = new HashMap<String, Object>();
        try {
            //获取参数
           Map<String, String> params =
                    ApiUtil.parse(request.getReader(), new TypeReference<Map<String,String>>() {});
            String username = params.get("username");
            String password = params.get("password");
            String captcha = params.get("captcha");
            String rsaEncryptKey = params.get("rsaEncryptKey");
            String captchaId = params.get("captchaId");

            //参数校验
            if(StringUtil.isBlank(username) || StringUtil.isBlank(password)){
                TraceContext.put("status", Status.fail);
                return AjaxResultUtil.resultAjax("用户名或密码不能为空",11104,data);
            }
            if (StringUtil.isBlank(rsaEncryptKey) ) {
                return AjaxResultUtil.resultAjax("缺少解密参数",11104,data);
            }
            //校验用户是否已登录,考虑是否踢出已登录用户,
            // app端和pc端可以同时登录,需要注意不能以用户名来获取用户信息,要以token来获取用户信息
            /*if(sessionService.getUserSession(username)!=null){
                return AjaxResultUtil.resultAjax("用户已登录",11104,data);
            }*/
            //解密,加偏移向量
            String rsaRandomKey = RSAUtils.decryptByPrivateKey(rsaEncryptKey);
            password = AESUtils.decryptData(rsaRandomKey, password);
            //IP访问受限
            if(timesLimitService.isIpLoginTimesOver()){
               TraceContext.put("status", Status.fail);
               String limitIp = (String) TraceContext.get(Constants.CONTEXT_IP_KEY);
               //return AjaxResultUtil.resultAjax("ip访问受限,Ip地址:"+limitIp+",今天累计错误操作已超过100次了,请明天再试!",12001,data);
            }
            Boolean isMobile = (Boolean) TraceContext.get(Constants.CONTEXT_ISMOBILE_KEY);
            // freeze账号状态判断
            boolean freeze = timesLimitService.executeIsFreeze(username);
            TraceContext.put("freeze", freeze);
            if (freeze) {
                TraceContext.put("status", Status.fail);
                //return AjaxResultUtil.resultAjax("访问受限,由于重试过多，账号已临时冻结，请一小时后再试或者找回密码",12007,data);
            }
            //判断用户是否存在
            AclUser aclUser = userService.getByLoginName(username);
            if(aclUser == null){
                TraceContext.put("status", Status.fail);
                return AjaxResultUtil.resultAjax("用户不存在",11207,data);
            }
            //判断用户状态
            if(aclUser.getStatus() != UserStatus.ACTIVE.getIndex()){
                TraceContext.put("status", Status.fail);
                return AjaxResultUtil.resultAjax("用户已" + UserStatus.getName(aclUser.getStatus()),12008,data);
            }
            Integer timeout = Constants.SESSION_TIMEOUT;
            TraceContext.put("timeout", timeout);
            TraceContext.put("username", username);
            //生成token,考虑加密
            String token = SSOUtil.generateSid();
            LOG.info("generate new token " + token);
            TraceContext.put("token", token);

            //验证图片验证码
            if (StringUtil.isNotBlank(captcha)) {
                if(StringUtil.isBlank(captchaId)){
                    return AjaxResultUtil.resultAjax("有图片验证码时,图片验证码隐藏ID不能为空",11104,data);
                }
                String ret = loginService.verifyCaptcha(captchaId, captcha, username);
                if(ret != null){
                    return AjaxResultUtil.resultAjax("图片验证码错误",11205,data);
                }
            }
            //验证密码
            String loginIp = (String) TraceContext.get(Constants.CONTEXT_IP_KEY);
            if(!sessionService.executeLogin(username, password)) {
                TraceContext.put("status", Status.fail);
                int freezeLimit = timesLimitService.executeFreezeLimit(username);
                TraceContext.put("freezeLimit", freezeLimit);
                /*if (freezeLimit != -1) {
                   return AjaxResultUtil.resultAjax("密码错误，请重新输入，多次重试将临时冻结账号，剩余重试次数" + freezeLimit,11301,data);
                } else {
                   return AjaxResultUtil.resultAjax("密码错误，请重新输入",11301,data);
                }*/
                return AjaxResultUtil.resultAjax("密码错误，请重新输入",11301,data);
            }
            //  区分移动端和pc端的token
            String ua = request.getHeader("User-Agent");
            boolean isApp = CheckUaUtil.isMobileDevice(ua);
            UserForSeesion user = null;
            if(isApp){
                sessionService.saveAppSession(token, aclUser, Constants.APP_SESSION_TIMEOUT);
                LOG.info("{} auth ok from {} for app {} with {} timeout {}", new Object[]{aclUser.getLoginName(), loginIp, token, Constants.APP_SESSION_TIMEOUT});
                TraceContext.put("username", aclUser.getLoginName());
                user = sessionService.getAppSessionUserAndRefresh(token);
            }else{
                sessionService.savePcSession(token, aclUser, Constants.PC_SESSION_TIMEOUT);
                LOG.info("{} auth ok from {} for pc {} with {} timeout {}", new Object[]{aclUser.getLoginName(), loginIp, token, Constants.PC_SESSION_TIMEOUT});
                TraceContext.put("username", aclUser.getLoginName());
                user = sessionService.getPcSessionUserAndRefresh(token);
            }
            data.put("token",token);
            data.put("user",user);
            return AjaxResultUtil.resultSuccessAjax(data);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage(), e);
            TraceContext.put("status", Status.exception);
            return AjaxResultUtil.resultAjax("请求数据格式错误或服务内部异常",10002,data);
        }
    }

    @RequestMapping(value = "logoutAllSub", method = RequestMethod.POST)
    @ResponseBody
    public Object logoutAllSub(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> data = new HashMap<String, Object>();
        try {
            Map<String, String> params =
                    ApiUtil.parse(request.getReader(), new TypeReference<Map<String,String>>() {});
            String token = params.get("token");
            if(StringUtil.isBlank(token) ){
                return AjaxResultUtil.resultAjax("认证token不能为空",11104,data);
            }
            UserForSeesion user = null;
            Session session = sessionService.getSession(token);
            String ua = request.getHeader("User-Agent");
            boolean isApp = CheckUaUtil.isMobileDevice(ua);
            if(isApp){
                sessionService.appExecuteLogout(token);
            }else{
                sessionService.pcExecuteLogout(token);
            }

            return AjaxResultUtil.resultSuccessAjax(data);
        } catch (Exception e) {
            TraceContext.put("status", Status.exception);
            LOG.error(e.getMessage(), e);
            return AjaxResultUtil.resultAjax("请求数据格式错误或服务内部异常",10002,data);
        }
    }

    //获取用户信息
    @RequestMapping(value = "userSession", method = RequestMethod.POST)
    @ResponseBody
    public Object getSession(HttpServletRequest request) {
        Map<String, Object> data = new HashMap<String, Object>();
        try {
            Map<String, String> params =
                    ApiUtil.parse(request.getReader(), new TypeReference<Map<String,String>>() {});
            String token = params.get("token");
            if(StringUtil.isBlank(token)){
                return AjaxResultUtil.resultAjax("认证token不能为空",11105,data);
            }
            UserForSeesion user = null;
            String ua = request.getHeader("User-Agent");
            boolean isApp = CheckUaUtil.isMobileDevice(ua);
            //先从pcSession key中获取数据
            user = sessionService.getPcSessionUserAndRefresh(token);
            //pc中获取不到,再从app中获取
            if(user==null || user.toLongId()==null){
                user = sessionService.getAppSessionUserAndRefresh(token);
            }
            if(user==null || user.toLongId()==null){
                return AjaxResultUtil.resultAjax("token:"+token+",已失效,请登录",10002,data);
            }
            data.put("user",user);
            return AjaxResultUtil.resultSuccessAjax(data);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return AjaxResultUtil.resultAjax("请求数据格式错误或服务内部异常",10002,data);
        }
    }

}
