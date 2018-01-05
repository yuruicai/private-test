package com.sinochem.yunlian.upm.sso.web;

import com.sinochem.yunlian.upm.common.reflect.TypeReference;
import com.sinochem.yunlian.upm.common.util.ApiUtil;
import com.sinochem.yunlian.upm.sso.cache.SsoCacheFacade;
import com.sinochem.yunlian.upm.sso.domain.AclUser;
import com.sinochem.yunlian.upm.sso.model.UserForSeesion;
import com.sinochem.yunlian.upm.sso.service.*;
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
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 注册
 */
@Controller
@RequestMapping(value = "/api/")
class RegisterController {
    private static final Logger LOG = LoggerFactory.getLogger(RegisterController.class);

    @Resource
    private RegisterService registerService;
    @Resource
    private SsoCaptchaService ssoCaptchaService;
    @Resource
    private UserService userService;
    @Resource
    private TimesLimitService timesLimitService;
    @Resource
    private SsoCacheFacade ssoCacheFacade;
    @Resource
    private SessionService sessionService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public Object register(HttpServletRequest request) {
        Map<String, Object> data = new HashMap<String, Object>();
        try {
            //获取参数
            Map<String, String> params =
                    ApiUtil.parse(request.getReader(), new TypeReference<Map<String, String>>() {});
            String username = params.get("username");
            String password = params.get("password");
            String ssmCaptcha = params.get("ssmCaptcha");
            String ssmCaptchaId = params.get("ssmCaptchaId");
            String rsaEncryptKey = params.get("rsaEncryptKey");
            //参数校验
            if (StringUtil.isBlank(username) || StringUtil.isBlank(password)) {
                return AjaxResultUtil.resultAjax("用户名或密码不能为空",11104,data);
            }
            if (StringUtil.isBlank(ssmCaptcha) ) {
                return AjaxResultUtil.resultAjax("短信验证码不能为空",11104,data);
            }
            if (StringUtil.isBlank(rsaEncryptKey) ) {
                return AjaxResultUtil.resultAjax("缺少解密参数",11104,data);
            }
            //IP限制判断
            if (timesLimitService.isIpLoginTimesOver()) {
                TraceContext.put("status", Status.fail);
                String limitIp = (String) TraceContext.get(Constants.CONTEXT_IP_KEY);
                //return AjaxResultUtil.resultAjax("ip访问受限,Ip地址:"+limitIp+",今天累计错误操作已超过100次了,请明天再试!",12001,data);
            }
            //手机号是否已注册
            boolean isRepeat = registerService.existMobile(username);
            if (isRepeat) {
                return AjaxResultUtil.resultAjax("该手机号已被注册", 11302, data);
            }
            //短信验证码
            String ssoMobile =
                    ssoCacheFacade.getObject("smsMobile" + ssmCaptchaId, String.class);
            if (ssoMobile!=null && StringUtil.isNotBlank(ssoMobile) && !username.equals(ssoMobile)) {
                return AjaxResultUtil.resultAjax("请按照正确的流程输入验证码", 11208, data);
            }
            boolean isCorrectSSM =
                    ssoCaptchaService.validateResponseForID(ssmCaptchaId, ssmCaptcha.toLowerCase());
            if (!isCorrectSSM) {
                return AjaxResultUtil.resultAjax("对不起，请输入正确的验证码", 11206, data);
            }
            //解密,加偏移向量
            String rsaRandomKey = RSAUtils.decryptByPrivateKey(rsaEncryptKey);
            password = AESUtils.decryptData(rsaRandomKey, password);
            String msg = userService.checkerPasswordLevel(username, password);
            if(StringUtil.isNotBlank(msg)){
                return AjaxResultUtil.resultAjax(msg,11304,data);
            }
            //注册
            String registerMessage = registerService.register(username, password);
            if (StringUtil.isNotBlank(registerMessage)) {
                return AjaxResultUtil.resultAjax("注册失败", 11206, data);
            }
            //生成token,考虑加密
            String token = SSOUtil.generateSid();
            LOG.info("generate new token " + token);
            //获取用户信息
            AclUser aclUser = userService.getByLoginName(username);
            //缓存用户信息,设置免登录先关信息
            sessionService.saveSession(token, aclUser, Constants.SESSION_TIMEOUT);
            timesLimitService.increaseUsernameSuccessLoginTimes(username);
            UserForSeesion user = sessionService.getSessionUserAndRefresh(token);
            data.put("token",token);
            data.put("user",user);
            return AjaxResultUtil.resultSuccessAjax(data);
        } catch (Exception e) {
            LOG.error("", e);
            TraceContext.put("status", Status.exception);
            return AjaxResultUtil.resultAjax("请求数据格式错误或服务内部异常", 10002, data);
        }
    }

    //验证手机号是否已注册
    @RequestMapping(value = "/isRepeatMobile", method = RequestMethod.POST)
    @ResponseBody
    public Object isRepeatMobile(HttpServletRequest request) {
        Map<String, Object> data = new HashMap<String, Object>();
        try {
            Map<String, String> params =
                    ApiUtil.parse(request.getReader(), new TypeReference<Map<String, String>>() {});
            String mobile = params.get("mobile");
            if (StringUtil.isBlank(mobile)) {
                return AjaxResultUtil.resultAjax("手机号不能为空",11104,data);
            }
            boolean isRepeat = registerService.existMobile(mobile);
            if (!isRepeat) {
                return AjaxResultUtil.resultAjax("手机号不存在",11204,data);
            }
            long userId = Long.parseLong(registerService.getUserIdByMobile(mobile));
            if (StringUtil.isBlank(userId)) {
                return AjaxResultUtil.resultAjax("根据手机号获取用户id失败",11104,data);
            }
            data.put("userId",userId);
            return AjaxResultUtil.resultSuccessAjax(data);
        } catch (IOException e) {
            e.printStackTrace();
            return AjaxResultUtil.resultAjax("请求数据格式错误或服务内部异常",10002,data);
        }
    }

    //验证密码的安全级别
    @RequestMapping(value = "/checkerPasswordLevel",method = RequestMethod.POST)
    @ResponseBody
    public Object checkerPasswordLevel(HttpServletRequest request){
        Map<String, Object> data = new HashMap<String, Object>();
        try {
            Map<String, String> params =
                    ApiUtil.parse(request.getReader(), new TypeReference<Map<String,String>>() {});
            String username = params.get("mobile");
            String password = params.get("password");
            String rsaEncryptKey = params.get("rsaEncryptKey");
            if(StringUtil.isBlank(username) || StringUtil.isBlank(password)){
                TraceContext.put("status", Status.fail);
                return AjaxResultUtil.resultAjax("手机号或密码不能为空",11104,data);
            }
            if (StringUtil.isBlank(rsaEncryptKey) ) {
                return AjaxResultUtil.resultAjax("缺少解密参数：rsaEncryptKey",11104,data);
            }
            //解密
            String rsaRandomKey = RSAUtils.decryptByPrivateKey(rsaEncryptKey);
            password = AESUtils.decryptData(rsaRandomKey, password);
            //获取校验消息
           String msg = userService.checkerPasswordLevel(username, password);
           if(StringUtil.isNotBlank(msg)){
               return AjaxResultUtil.resultAjax(msg,11304,data);
            }
            return AjaxResultUtil.resultSuccessAjax(data);
        } catch (Exception e) {
            LOG.info(e.getMessage(), e);
            return AjaxResultUtil.resultAjax("请求数据格式错误或服务内部异常",10002,data);
        }
    }

}
