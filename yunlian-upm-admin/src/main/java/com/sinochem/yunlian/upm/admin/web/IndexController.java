package com.sinochem.yunlian.upm.admin.web;

import com.sinochem.yunlian.upm.admin.service.UpmService;
import com.sinochem.yunlian.upm.filter.util.UserUtils;
import com.sinochem.yunlian.upm.util.UpmCacheUtil;
import com.sinochem.yunlian.upm.admin.domain.AclApplication;
import com.sinochem.yunlian.upm.util.AjaxResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "")
public class IndexController {
    private static final Logger LOG = LoggerFactory.getLogger(IndexController.class);

    @Resource
    private UpmService upmService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index() throws IOException {
        return "index/index";
    }

    @RequestMapping(value = "unauthorized", method = RequestMethod.GET)
    public String unauthorized() throws IOException {
        return "index/unauthorized";
    }

    @RequestMapping(value = "/apps.ajax", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getAllowApps() throws IOException {
        try {
            String userId = UpmCacheUtil.getCurrentUserId();
            List<AclApplication> apps = upmService.getAllowedApps(userId);
            Collections.sort(apps, new Comparator<AclApplication>() {
                @Override
                public int compare(AclApplication o1, AclApplication o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
            return AjaxResultUtil.success(apps);
        } catch (Exception e) {
            LOG.warn(e.getMessage(), e);
        }
        return AjaxResultUtil.createAjaxFailureMap("服务内部异常");
    }

    @RequestMapping(value = "home", method = RequestMethod.GET)
    public String ssoHome(HttpServletRequest request) throws IOException {
        List<AclApplication> applications =  upmService.selectAppsByUser(UserUtils.getUser().getId());
        request.setAttribute("applications",applications);
        return "home";
    }
}
