package com.sinochem.yunlian.upm.admin.exception.resolver;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Sets;
import com.sinochem.yunlian.upm.admin.conf.ParamName;
import com.sinochem.yunlian.upm.util.AjaxResultUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

public class WebExceptionResolver extends SimpleMappingExceptionResolver {
    private final Logger logger = LoggerFactory.getLogger(WebExceptionResolver.class);

    private Set<String> handleApiDomain = Sets.newHashSet();

    private Map<String, Integer> knownException;

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request,
                                              HttpServletResponse response, Object handler, Exception ex) {
        printExceptionLog(request, ex);

        if (isAjaxRequest(request)) {
            setAjaxResponse(response, ex);
            return null;
        } else {
            return super.doResolveException(request, response, handler, ex);
        }
    }

    private void setAjaxResponse(HttpServletResponse response, Exception ex) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        Map<?, ?> errorResult = null;
        if (knownException.containsKey(ex.getClass().getSimpleName())) {
            response.setStatus(HttpServletResponse.SC_OK);
            errorResult = AjaxResultUtil.fail(knownException.get(ex.getClass().getSimpleName()), ex.getClass().getSimpleName(), ex.getMessage());
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            errorResult = AjaxResultUtil.fail(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getClass().getSimpleName(), ex.getMessage());
        }

        try {
            String json = JSON.toJSONString(errorResult);

            PrintWriter pw = response.getWriter();
            pw.println(json);
            pw.flush();
            pw.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private boolean isAjaxRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        checkNotNull(uri);
        for (String domain : handleApiDomain) {
            if (uri.startsWith(domain)) {
                return true;
            }
        }
        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            return true;
        }
        return false;
    }


    private void printExceptionLog(HttpServletRequest request, Exception ex) {
        String parameters = getRequestParameters(request);
        StringBuilder errorMessage = new StringBuilder().append("Error occurred when ").append(request.getMethod());
        appendClientId(request, errorMessage).append(" uri: ").append(request.getRequestURL());
        if (org.springframework.util.StringUtils.hasText(parameters)) {
            errorMessage = errorMessage.append("?").append(parameters);
        }
        if (!knownException.containsKey(ex.getClass().getSimpleName())) {
            logger.error(errorMessage.toString(), ex);
        } else {
            logger.warn(errorMessage.toString(), ex);
        }
    }

    private StringBuilder appendClientId(HttpServletRequest request, StringBuilder errorMessage) {
        Object apiClientId = request.getAttribute(ParamName.CLIENT_ID);
        if (apiClientId != null) {
            errorMessage.append(" client id: ").append(apiClientId);
        }
        return errorMessage;
    }

    private String getRequestParameters(HttpServletRequest request) {
        StringBuilder parametersBuilder = new StringBuilder();

        @SuppressWarnings("unchecked")
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Iterator<String> iter = parameterMap.keySet().iterator(); iter.hasNext(); ) {
            String key = iter.next();
            if (key != null && !"".equals(key.trim())) {
                parametersBuilder.append(key).append("=").append(request.getParameter(key));
            }
            if (iter.hasNext()) {
                parametersBuilder.append("&");
            }
        }
        return parametersBuilder.toString();
    }

    public void setHandleApiDomain(Set<String> handleApiDomain) {
        checkApiDomain(handleApiDomain);
        this.handleApiDomain = handleApiDomain;
    }

    public void setKnownException(Map<String, Integer> knownException) {
        this.knownException = knownException;
    }

    private void checkApiDomain(Set<String> handleApiDomain) {
        for (String domain : handleApiDomain) {
            if (StringUtils.contains(domain, "*")) {
                throw new RuntimeException(MessageFormat.format("{0}的配置出错,配置异常移动处理转换的url只支持字符串匹配,不支持正则等动态匹配!", WebExceptionResolver.class.getSimpleName()));
            }
        }
    }
}
