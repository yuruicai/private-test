package com.sinochem.yunlian.upm.sso.web;

import com.sinochem.yunlian.upm.common.reflect.TypeReference;
import com.sinochem.yunlian.upm.common.util.ApiUtil;
import com.sinochem.yunlian.upm.sso.cache.SsoCacheFacade;
import com.sinochem.yunlian.upm.sso.constant.UserStatus;
import com.sinochem.yunlian.upm.sso.domain.AclUser;
import com.sinochem.yunlian.upm.sso.service.SessionService;
import com.sinochem.yunlian.upm.sso.service.SsoCaptchaService;
import com.sinochem.yunlian.upm.sso.service.UserService;
import com.sinochem.yunlian.upm.sso.util.AjaxResultUtil;
import com.sinochem.yunlian.upm.sso.util.RSAUtils.AESUtils;
import com.sinochem.yunlian.upm.sso.util.RSAUtils.RSAUtils;
import com.sinochem.yunlian.upm.sso.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
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


@Controller
@RequestMapping(value = "/api/")
@Slf4j
public class PasswordController {

    private static final Logger LOG = LoggerFactory.getLogger(PasswordController.class);

    @Resource
    private UserService userService;
    @Resource
    private SsoCaptchaService ssoCaptchaService;
    @Resource
    private SsoCacheFacade ssoCacheFacade;
    @Resource
    private SessionService sessionService;

    //找回密码
    @RequestMapping(value = "/findPassword", method = RequestMethod.POST)
    @ResponseBody
    public Object findPassword(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> data = new HashMap<String, Object>();
        try {
            Map<String, String> params = ApiUtil.parse(request.getReader(), new TypeReference<Map<String,String>>() {});
            String mobile = params.get("mobile");
            String password = params.get("password");
            String ssmCaptcha = params.get("ssmCaptcha");
            String rsaEncryptKey = params.get("rsaEncryptKey");
            String ssmCaptchaId = params.get("ssmCaptchaId");
            //参数校验
            if(StringUtil.isBlank(mobile) || StringUtil.isBlank(password)){
                return AjaxResultUtil.resultAjax("手机号或密码不能为空",11104,data);
            }
            if (StringUtil.isBlank(rsaEncryptKey) ) {
                return AjaxResultUtil.resultAjax("缺少解密参数：rsaEncryptKey",11104,data);
            }
            if (StringUtil.isBlank(ssmCaptcha) ) {
                return AjaxResultUtil.resultAjax("短信验证码不能为空：rsaEncryptKey",11104,data);
            }
            //验证用户的有效性及状态
            AclUser aclUser = userService.getByMobile(mobile);
            if(aclUser==null) {
                return AjaxResultUtil.resultAjax("手机号不存在",11204,data);
            }
            if (!(aclUser.getStatus() == UserStatus.ACTIVE.getIndex())) {
                return AjaxResultUtil.resultAjax("用户已" + UserStatus.getName(aclUser.getStatus()),12008,data);
            }
            //短信验证码
            String ssoMobile = ssoCacheFacade.getObject("smsMobile" + ssmCaptchaId, String.class);
            if(ssoMobile!=null && StringUtil.isNotBlank(ssoMobile) && !mobile.equals(ssoMobile)){
                return AjaxResultUtil.resultAjax("注册的手机号和发送验证码的手机号不一致", 11208, data);
            }
            boolean isCorrectSSM = ssoCaptchaService.validateResponseForID(ssmCaptchaId, ssmCaptcha.toLowerCase());
            if(!isCorrectSSM) {
                return AjaxResultUtil.resultAjax("无效的短信验证码", 11206, data);
            }
            //解密,加偏移向量
            String rsaRandomKey = RSAUtils.decryptByPrivateKey(rsaEncryptKey);
            password = AESUtils.decryptData(rsaRandomKey, password);
            //更新密码
            String msg = userService.updatePassword(aclUser, password);
            if(StringUtil.isNotBlank(msg)){
                return AjaxResultUtil.resultAjax(msg,11304,data);
            }
            //将所有登录状态置为失效
            return AjaxResultUtil.resultSuccessAjax(data);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResultUtil.resultAjax("请求数据格式错误或服务内部异常",10002,data);
        }
    }

    //修改密码
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    @ResponseBody
    public Object updatePassword(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> data = new HashMap<String, Object>();
        try {
            Map<String, String> params =
                    ApiUtil.parse(request.getReader(), new TypeReference<Map<String,String>>() {});
            String mobile = params.get("mobile");
            String password = params.get("password");
            String rsaEncryptKey = params.get("rsaEncryptKey");
            String token = params.get("token");
            //参数校验
            if(StringUtil.isBlank(mobile) || StringUtil.isBlank(password)){
                return AjaxResultUtil.resultAjax("手机号或密码不能为空",11104,data);
            }
            if (StringUtil.isBlank(rsaEncryptKey) ) {
                return AjaxResultUtil.resultAjax("缺少解密参数：rsaEncryptKey",11104,data);
            }
            if (StringUtil.isBlank(token) ) {
                return AjaxResultUtil.resultAjax("token不能为空",11104,data);
            }
            //验证用户的有效性及状态
            AclUser aclUser = userService.getByMobile(mobile);
            if(aclUser==null) {
                return AjaxResultUtil.resultAjax("手机号不存在",11204,data);
            }
            if (!(aclUser.getStatus() == UserStatus.ACTIVE.getIndex())) {
                return AjaxResultUtil.resultAjax("用户已" + UserStatus.getName(aclUser.getStatus()),12008,data);
            }
            //解密,加偏移向量
            String rsaRandomKey = RSAUtils.decryptByPrivateKey(rsaEncryptKey);
            password = AESUtils.decryptData(password, password);
            //更新密码
            String msg = userService.updatePassword(aclUser, password);
            if(StringUtil.isNotBlank(msg)){
                return AjaxResultUtil.resultAjax(msg,11304,data);
            }
            //将所有登录状态设为失效
            String resultMsg = userService.logoutAll(token);
            if(StringUtil.isNotBlank(resultMsg)){
                return AjaxResultUtil.resultAjax(resultMsg,11304,data);
            }
            return AjaxResultUtil.resultSuccessAjax(data);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResultUtil.resultAjax("请求数据格式错误或服务内部异常",10002,data);
        }
    }

}
