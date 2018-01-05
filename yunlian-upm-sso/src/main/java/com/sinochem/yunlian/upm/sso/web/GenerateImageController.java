package com.sinochem.yunlian.upm.sso.web;


import com.sinochem.yunlian.upm.common.reflect.TypeReference;
import com.sinochem.yunlian.upm.common.util.ApiUtil;
import com.sinochem.yunlian.upm.sso.cache.SsoCacheFacade;
import com.sinochem.yunlian.upm.sso.service.SsoCaptchaService;
import com.sinochem.yunlian.upm.sso.util.AjaxResultUtil;
import com.sinochem.yunlian.upm.sso.util.CookieUtil;
import com.sinochem.yunlian.upm.sso.util.SSM.SendSmsCode;
import com.sinochem.yunlian.upm.sso.util.StringUtil;
import nl.captcha.Captcha;
import nl.captcha.servlet.CaptchaServletUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping(value = "/api/")
public class GenerateImageController {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateImageController.class);

    @Resource
    private SsoCaptchaService ssoCaptchaService;
    @Resource
    private SsoCacheFacade ssoCacheFacade;

    @RequestMapping(value = "/generatImage123")
    public void imageCaptcha(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String cookieCapId = CookieUtil.getCookie(request, "captchaid", String.class);
        String captchaId = cookieCapId != null ? cookieCapId : UUID.randomUUID().toString().replaceAll("-", "");
        CookieUtil.setCookie(response, "captchaid", captchaId);
        Captcha captcha = ssoCaptchaService.generateAndStoreCaptcha(captchaId);
        CaptchaServletUtil.writeImage(response, captcha.getImage());
    }

    //图片验证码
    @RequestMapping(value = "/generatImage",method = RequestMethod.GET)
    public Object imageCaptchaTest(@RequestParam(value = "captchaId" ,required = true)
                                               String captchaId, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> data = new HashMap<String, Object>();
        try {
            //检验入参是否为空
            if(StringUtil.isBlank(captchaId)){
                return AjaxResultUtil.resultAjax("图片验证码id不能为空",11104,data);
            }
            Captcha captcha = ssoCaptchaService.generateAndStoreCaptcha(captchaId);
            CaptchaServletUtil.writeImage(response, captcha.getImage());
            String captchaString = ssoCaptchaService.getCaptchaString(captchaId);
            LOG.info("测试redis："+captchaString);
            return AjaxResultUtil.resultSuccessAjax(data);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResultUtil.resultAjax("发送图片验证码失败",11300,data);
        }
    }

    //发送短信验证码
    @RequestMapping(value = "/sendSSM",method = RequestMethod.POST)
    @ResponseBody
    public Object ssmCaptcha(HttpServletRequest request) {
        Map<String, Object> data = new HashMap<String, Object>();
        try {
            Map<String, String> params =
                    ApiUtil.parse(request.getReader(), new TypeReference<Map<String, String>>() {});

            //防止恶意刷短信,手机号是否加密处理?
            String mobile = params.get("mobile");
            if(StringUtil.isBlank(mobile)){
                return AjaxResultUtil.resultAjax("手机号不能为空",11104,data);
            }
            String ssmCaptchaId = UUID.randomUUID().toString().replaceAll("-", "");
            //发送短信验证码
            Map<?, ?> map = SendSmsCode.send(mobile);
            String code = (String) map.get("obj");
            Integer status = (Integer) map.get("code");
            ssoCaptchaService.storeCaptcha(ssmCaptchaId,code);
            ssoCacheFacade.setObject("smsMobile" + ssmCaptchaId, 300, mobile);
            if(status==416){
                return AjaxResultUtil.resultAjax("手机号："+mobile+",今日已累计发送10次,请明日再发送",11300,data);
            }
            if(StringUtil.isBlank(code)){
                AjaxResultUtil.resultAjax("发送短信验证码失败",11300,data);
            }
            data.put("ssmCaptchaId",ssmCaptchaId);
            return AjaxResultUtil.resultSuccessAjax(data);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResultUtil.resultAjax("请求数据格式错误或服务内部异常",10002,data);
        }
    }

}
