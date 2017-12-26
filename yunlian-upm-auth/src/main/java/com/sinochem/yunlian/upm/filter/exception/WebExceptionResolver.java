package com.sinochem.yunlian.upm.filter.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WebExceptionResolver extends SimpleMappingExceptionResolver {

    private final Logger logger = LoggerFactory.getLogger(WebExceptionResolver.class);

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request,
                                              HttpServletResponse response, Object handler, Exception ex) {
        if(ex instanceof UnAuthorizedException){
            logger.info("unAuthorized");
        }else{
            logger.error("error", ex);
        }

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
        if(ex instanceof UnAuthorizedException){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }else{
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private boolean isAjaxRequest(HttpServletRequest request) {
        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            return true;
        }
        return false;
    }

}
