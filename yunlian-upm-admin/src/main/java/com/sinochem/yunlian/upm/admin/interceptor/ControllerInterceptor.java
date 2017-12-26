package com.sinochem.yunlian.upm.admin.interceptor;

import freemarker.ext.beans.BeansWrapper;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

/**
 * controller的拦截器
 */
@Component
@Aspect
public class ControllerInterceptor {
    private final Logger logger = LoggerFactory.getLogger(ControllerInterceptor.class);

    /**
     * 声明切入点,所有controller的public方法
     */
    @Pointcut("execution(public * com.sinochem.yunlian.upm.admin.web.*.*(..))")
    public void anyMethod() {
    }

    /**
     * 给所有的返回为ModelAndView增加静态方法支持
     * 
     * @param returnObj
     */
    @AfterReturning(pointcut = "anyMethod()", returning = "returnObj")
    public void after(Object returnObj) {
        if (returnObj != null && returnObj instanceof ModelAndView) {
            ((ModelAndView) returnObj).addObject("statics", BeansWrapper.getDefaultInstance()
                    .getStaticModels());
        }
    }
}
