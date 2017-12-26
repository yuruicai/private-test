package com.sinochem.yunlian.upm.sso.auth;

import com.sinochem.yunlian.upm.sso.model.Session;
import com.sinochem.yunlian.upm.sso.service.SessionService;
import com.sinochem.yunlian.upm.sso.util.Constants;
import com.sinochem.yunlian.upm.sso.util.StringUtil;
import com.sinochem.yunlian.upm.sso.util.TraceContext;
import com.sinochem.yunlian.upm.tools.UpmAuthService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author zhangxi
 * @created 14-1-16
 */
@Component
@Aspect
public class AuthAspect {
    private static final Logger LOG = LoggerFactory.getLogger(AuthAspect.class);

    @Resource
    private UpmAuthService upmAuthService;
    @Resource
    private SessionService sessionService;

    @Pointcut("@annotation(com.sinochem.yunlian.upm.sso.auth.RequireAuth)")
    public void requireAuth() {
    }

    @Around("requireAuth()")
    public Object auth(ProceedingJoinPoint point) throws Throwable {
        String token = (String) TraceContext.get(Constants.CONTEXT_TOKEN);
        if (StringUtil.isNotBlank(token)) {
            Session session = sessionService.getSession(token);
            if (session != null) {
                String userId = session.getUserId();
                String ip = (String) TraceContext.get(Constants.CONTEXT_IP_KEY);
                RequireAuth requireAuth = getRequireAuth(point.getSignature());
                LOG.info("{} {} {} {}", new Object[]{userId, ip, requireAuth, point.getTarget().getClass()});
                // TODO 如果作为认证组件，还需要考虑怎样获取相关信息如：用户信息、资源信息等
                if (auth(userId, requireAuth)) {
                    Object response = point.proceed(point.getArgs());
                    return response;
                } else {
                    throw new UnAuthorizedException();
                }
            }
        }
        return "redirect:/login";
    }

    private boolean auth(String userId, RequireAuth requireAuth) {
        if (requireAuth.permissions() != null && requireAuth.permissions().length > 0) {
            List<String> permissions = upmAuthService.getPermissions("sso", userId);
            LOG.info("{} {}", new Object[]{permissions, requireAuth.permissions()});
            for (String permission : requireAuth.permissions()) {
                for (String p : permissions) {
                    if (p.equalsIgnoreCase(permission)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private RequireAuth getRequireAuth(Signature signature) {
        if (signature instanceof MethodSignature) {
            Method method = ((MethodSignature) signature).getMethod();
            if (method != null) {
                return method.getAnnotation(RequireAuth.class);
            }
        }
        return null;
    }
}
