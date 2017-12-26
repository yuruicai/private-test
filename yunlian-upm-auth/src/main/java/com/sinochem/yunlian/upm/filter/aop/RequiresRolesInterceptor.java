package com.sinochem.yunlian.upm.filter.aop;

import com.sinochem.yunlian.upm.filter.annotation.RequiresRoles;
import com.sinochem.yunlian.upm.filter.annotation.RoleAnnotationHandler;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author zhangxi
 * @created 14-1-16
 */
@Component
@Aspect
public class RequiresRolesInterceptor {
    private static final Logger LOG = LoggerFactory.getLogger(RequiresRolesInterceptor.class);
    private RoleAnnotationHandler roleAnnotationHandler = new RoleAnnotationHandler();

    @Pointcut("@annotation(com.sinochem.yunlian.upm.filter.annotation.RequiresRoles)")
    public void requireAuth() {
    }

    @Around("requireAuth()")
    public Object auth(ProceedingJoinPoint point) throws Throwable {
        RequiresRoles rpAnnotation = getAnnotation(point.getSignature());
        roleAnnotationHandler.assertAuthorized(rpAnnotation);
        return point.proceed(point.getArgs());
    }

    private RequiresRoles getAnnotation(Signature signature) {
        if (signature instanceof MethodSignature) {
            Method method = ((MethodSignature) signature).getMethod();
            if (method != null) {
                return method.getAnnotation(RequiresRoles.class);
            }
        }
        return null;
    }
}
