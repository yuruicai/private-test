package com.sinochem.yunlian.upm.sso.service;

import com.sinochem.yunlian.upm.sso.model.MtCookie;
import com.sinochem.yunlian.upm.sso.model.Session;
import com.sinochem.yunlian.upm.sso.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhangxi
 * @created 13-8-9
 */
@Service
public class LoginService {
    private static final Logger LOG = LoggerFactory.getLogger(LoginService.class);

    private static final String SSO_TIMEOUT_KEY = "sso.sso_timeout.";

    @Resource
    TimesLimitService timesLimitService;
    @Resource
    private TicketService ticketService;
    @Resource
    private SessionService sessionService;
    @Resource
    private SsoCaptchaService ssoCaptchaService;

    /**
     * 当前访问是否需要出验证码
     *
     * @return
     */
    public boolean executeCurrentNeedCaptcha(String username) {
        return true;
    }

    /**
     * 当前访问是否需要出验证码
     *
     * @return
     */
    public String verifyCaptcha(HttpServletRequest request, String captcha, String username, String service){
        try {
            String captchaId = CookieUtil.getCookie(request, "captchaid", String.class);
            if (StringUtil.isBlank(captcha)) {
                LOG.warn("validateResponseForID fail null..." + username + "," + service + "," + captchaId);
                TraceContext.put("status", Status.codeFail);
                return "请输入图片验证码";
            }

            boolean isCorrect = ssoCaptchaService.validateResponseForID(captchaId, captcha.toLowerCase());
            if (!isCorrect) {
                LOG.warn("validateResponseForID fail..." + username + "," + service + "," + captcha
                        + "," + captchaId);
                return "验证码错误，请重新输入";
            }
            LOG.info("validateResponseForID suc..." + username + "," + service + "," + captcha + "," + captchaId);
        } catch (Exception e) {
            LOG.warn("validateResponseForID exception", e);
        }
        return null;
    }


    public String executeRedirect(String sid, String service, Model model) {
        if(StringUtil.isBlank(service)){
            return "redirect:/";
        }
        String url;
        String ticket = ticketService.generateTicket();
        ticketService.saveTicket(ticket, sid);
        if (service.contains("?")) {
            url = service + "&ticket=" + ticket;
        } else {
            url = service + "?ticket=" + ticket;
        }
        LOG.info("redirect to {} {} {} {}", ticket,sid,service,url);

        return "redirect:" + url;
    }

    public String verifyCaptcha(String captchaId, String captcha, String username){
        try {
            if (StringUtil.isBlank(captchaId)) {
                TraceContext.put("status", Status.codeFail);
                return "图片验证码隐藏ID不能为空";
            }
            if (StringUtil.isBlank(captcha)) {
                TraceContext.put("status", Status.codeFail);
                return "请输入图片验证码";
            }

            boolean isCorrect = ssoCaptchaService.validateResponseForID(captchaId, captcha.toLowerCase());
            if (!isCorrect) {
                return "图片验证码错误，请重新输入";
            }
        } catch (Exception e) {
            LOG.warn("validateResponseForID exception", e);
        }
        return null;
    }

    public String authPass(String token, String service, HttpServletRequest request, HttpServletResponse response, Model model) {
        Session session = sessionService.getSession(token);
        String username = session == null ? "" : session.getUserLogin();
        TraceContext.put("token", token);
        TraceContext.put("username", username);

        MtCookie cookie = new MtCookie(Constants.CONTEXT_USER_NAME).value(username);
        cookie.setMaxAge(Constants.UUID_MAX_AGE);
        cookie.setHttpOnly(false);
        cookie.saveTo(request, response);
        //跳转到第三方系统
        LOG.info("begin to redirect..." + token + "," + service);
        return executeRedirect(token, service, model);
    }
}
