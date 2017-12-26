package com.sinochem.yunlian.upm.sso.web.forAdmin;

import com.sinochem.yunlian.upm.sso.model.TimesLimitInfo;
import com.sinochem.yunlian.upm.sso.service.TimesLimitService;
import com.sinochem.yunlian.upm.sso.auth.RequireAuth;
import com.sinochem.yunlian.upm.sso.constant.TimesLimitConstant;
import com.sinochem.yunlian.upm.sso.util.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by zhanghongze on 2015/11/19.
 */
@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    @Resource
    private TimesLimitService timesLimitService;

    @RequireAuth(permissions = "sso:admin")
    @RequestMapping(value = "timeslimit/index", method = RequestMethod.GET)
    public String timeslimitIndex(String ip, String username, Model model) {
        if(StringUtil.isNotBlank(ip)){
            TimesLimitInfo ipTimesLimitInfo = timesLimitService.getTimesLimitInfo(TimesLimitConstant.IP, ip, null);
            Date ipTimesLimitExpireTime = timesLimitService.getTimesLimitExpireTime(TimesLimitConstant.IP, ip, null);

            model.addAttribute("ipTimesLimitInfo", ipTimesLimitInfo);
            model.addAttribute("ipTimesLimitExpireTime", ipTimesLimitExpireTime);
        }else if(StringUtil.isNotBlank(username)){
            TimesLimitInfo usernameTimesLimitInfo = timesLimitService.getTimesLimitInfo(TimesLimitConstant.USERNAME, username, null);
            Date usernameTimesLimitExpireTime = timesLimitService.getTimesLimitExpireTime(TimesLimitConstant.USERNAME, username, null);
            model.addAttribute("usernameTimesLimitInfo", usernameTimesLimitInfo);
            model.addAttribute("usernameTimesLimitExpireTime", usernameTimesLimitExpireTime);
        }
        model.addAttribute("ip", ip);
        model.addAttribute("username", username);

        return "admin/timesLimit";

    }

    @RequireAuth(permissions = "sso:admin")
    @RequestMapping(value = "timeslimit/save", method = RequestMethod.POST)
    public String timeslimitSave(Integer type, String ip, String username, TimesLimitInfo timesLimitInfo, Model model) {
        if(type == TimesLimitConstant.IP){
            TimesLimitInfo timesLimitInfoNow = timesLimitService.getTimesLimitInfo(TimesLimitConstant.IP, ip, null);
            timesLimitInfoNow.setFailLoginTimes(timesLimitInfo.getFailLoginTimes());
            timesLimitInfoNow.setLoginTimeStamp(timesLimitInfo.getSuccessLoginTimes());

            timesLimitService.setTimesLimitInfo(TimesLimitConstant.IP, ip, null, timesLimitInfoNow);
        }else if(type == TimesLimitConstant.USERNAME){
            TimesLimitInfo timesLimitInfoNow = timesLimitService.getTimesLimitInfo(TimesLimitConstant.USERNAME, username, null);
            timesLimitInfoNow.setFailLoginTimes(timesLimitInfo.getFailLoginTimes());
            timesLimitInfoNow.setLoginTimeStamp(timesLimitInfo.getSuccessLoginTimes());

            timesLimitService.setTimesLimitInfo(TimesLimitConstant.USERNAME, username, null, timesLimitInfoNow);
        }

        model.addAttribute("ip", ip);
        model.addAttribute("username", username);

        return timeslimitIndex(ip, username, model);

    }
}
