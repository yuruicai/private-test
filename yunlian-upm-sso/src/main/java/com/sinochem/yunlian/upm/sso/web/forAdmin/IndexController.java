package com.sinochem.yunlian.upm.sso.web.forAdmin;

import com.sinochem.yunlian.upm.sso.service.SessionService;
import com.sinochem.yunlian.upm.sso.util.SSOUtil;
import com.sinochem.yunlian.upm.sso.model.Session;
import com.sinochem.yunlian.upm.sso.service.ApplicationService;
import com.sinochem.yunlian.upm.sso.util.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/")
public class IndexController {
    private static final String MIS_URL = "http://www.sinochem.com";
    @Value("#{configProperties['upm_host']}")
    private String upm_host;

    @Resource
    private SessionService sessionService;
    @Resource
    private ApplicationService applicationService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(HttpServletRequest request) {
        String token = SSOUtil.getSid(request);
        if (!SSOUtil.isValidSid(token)) {
            return "redirect:/login";
        }
        Session session = sessionService.getSession(token);
        if (session!=null&& !StringUtil.isBlank(session.getUserName())){
//            List  applications =applicationService.APPLICATION_LIST_CACHE;
//            request.setAttribute("currentUserName", session.getUserName());
//            request.setAttribute("applications",applications);
            return "redirect:"+upm_host+"/home";
        }
        return "redirect:/login";
        //return isPass ? "index/home":"redirect:/login";
    }
}
