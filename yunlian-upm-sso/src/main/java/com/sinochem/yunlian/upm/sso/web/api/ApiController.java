package com.sinochem.yunlian.upm.sso.web.api;

import com.sinochem.yunlian.upm.common.reflect.TypeReference;
import com.sinochem.yunlian.upm.common.rpc.MtError;
import com.sinochem.yunlian.upm.common.rpc.Response;
import com.sinochem.yunlian.upm.common.util.ApiUtil;
import com.sinochem.yunlian.upm.sso.model.Session;
import com.sinochem.yunlian.upm.sso.service.SessionService;
import com.sinochem.yunlian.upm.sso.service.TicketService;
import com.sinochem.yunlian.upm.sso.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author zhangxi
 * @created 13-2-28
 * @mtapi.appkey mtsso
 * @mtapi.principal 张熙
 */
@Controller
@RequestMapping(value = "/api")
public class ApiController {
    private static final Logger LOG = LoggerFactory.getLogger(ApiController.class);

    @Resource
    private SessionService sessionService;
    @Resource
    private TicketService ticketService;

    @RequestMapping(value = "/monitor/alive")
    @ResponseBody
    public Map<String, Object> monitorAlive() {
        Map<String, Object> result = new HashMap<String, Object>();
        LOG.info("测试monitorAlive：");
        result.put("status", "ok");
        return result;
    }

    @RequestMapping(value = "serviceValidate", method = RequestMethod.POST)
    @ResponseBody
    public Response serviceValidate(HttpServletRequest request) {
        try {
            Map<String, String> params = ApiUtil.parse(request.getReader(), new TypeReference<Map<String, String>>() {});
            String ticket = params.get("ticket");
            LOG.info("测试serviceValidate："+ticket);
            if(ticket != null) {
                String token = ticketService.getTicket(ticket);
                if (StringUtil.isNotBlank(token)) {
                    Session session = sessionService.getSession(token);
                    if (session != null) {
                        return ApiUtil.response(token);
                    }
                }
            }
        } catch (Exception e) {
            LOG.info(e.getMessage(), e);
        }
        return ApiUtil.errorResponse(MtError.accessAppForbidden);
    }

    /**
     * @mtapi.name 获取用户信息
     * @mtapi.description sid以显式参数方式传递，不安全
     * @mtapi.category 认证
     * @mtapi.param sid 用户sid
     * @mtapi.result 简单用户信息 User
     * @mtapi.requestExample http://sso.sankuai.com/api/session/c92a196705074c368b0cf8cac38972de
     * @mtapi.responseExample {"data":{"name":"张熙","id":7655,"login":"zhangxi"}}
     * @mtapi.errorExample {"data":{"name":null,"id":0,"login":null}}
     */
    @RequestMapping(value = "session", method = RequestMethod.POST)
    @ResponseBody
    public Response getSession(HttpServletRequest request) {
        try {
            Map<String, String> params = ApiUtil.parse(request.getReader(), new TypeReference<Map<String,String>>() {});
            String token = params.get("token");
            LOG.info("测试serviceValidate："+token);
            if (token != null) {
               return ApiUtil.response(sessionService.getSessionUserAndRefreshForApi(token));
            }
        } catch (Exception e) {
            LOG.info(e.getMessage(), e);
        }
        return ApiUtil.errorResponse(MtError.accessAppForbidden);
    }

    /**
     * @mtapi.name api方式登出
     * @mtapi.description sid以显式参数方式传递，不安全
     * @mtapi.category 认证
     * @mtapi.param sid 用户sid
     * @mtapi.requestExample http://sso.sankuai.com/api/logout/c92a196705074c368b0cf8cac38972de
     * @mtapi.responseExample {"data":"ok"}
     */
    @RequestMapping(value = "logout/{sid}")
    public
    @ResponseBody
    Response logout(@PathVariable String sid) {
        try {
            if (sid != null) {
                sessionService.executeLogout(sid);
            }
        } catch (Exception e) {
            LOG.info(e.getMessage(), e);
            return ApiUtil.errorResponse(MtError.serviceInternelException);
        }
        return ApiUtil.response("ok");
    }

    /**
     * @mtapi.name api方式安全登出
     * @mtapi.description Post方式
     * @mtapi.category 认证
     * @mtapi.param sid 用户sid，通过Body以json格式传输如{"sid":"c92a196705074c368b0cf8cac38972de"}
     * @mtapi.requestExample http://sso.sankuai.com/api/logout
     * @mtapi.responseExample {"data":"ok"}
     */
    @RequestMapping(value = "logout", method = RequestMethod.POST)
    public
    @ResponseBody
    Response logout(HttpServletRequest request) {
        try {
            Map<String, String> params = ApiUtil.parse(request.getReader(), new TypeReference<Map<String,String>>() {
            });
            String token = params.get("token");
            if (token != null) {
                sessionService.executeLogout(token);
            }
        } catch (Exception e) {
            LOG.info(e.getMessage(), e);
            return ApiUtil.errorResponse(MtError.serviceInternelException);
        }
        return ApiUtil.response("ok");
    }

    /**
     * @mtapi.name 登出
     * @mtapi.description Post方式
     * @mtapi.category 认证
     * @mtapi.param uid 用户id，通过Body以json格式传输如{"uid":7655}
     * @mtapi.requestExample http://sso.sankuai.com/api/logoutAll
     * @mtapi.responseExample {"data":"ok"}
     */
    @RequestMapping(value = "logoutAll", method = RequestMethod.POST)
    public
    @ResponseBody
    Response logoutAll(HttpServletRequest request) {
        try {
            Map<String, String> params = ApiUtil.parse(request.getReader(), new TypeReference<Map<String, String>>() {
            });
            String uid = params.get("uid");
            if (uid != null) {
                sessionService.executeLogoutByUserId(uid);
            }
        } catch (Exception e) {
            LOG.info(e.getMessage(), e);
            return ApiUtil.errorResponse(MtError.serviceInternelException);
        }
        return ApiUtil.response("ok");
    }
}
