package com.sinochem.yunlian.upm.filter.annotation;

import com.sinochem.yunlian.upm.filter.exception.UnAuthorizedException;
import com.sinochem.yunlian.upm.filter.util.UserUtils;

import java.lang.annotation.Annotation;

/**
 * Created by zhanghongze on 2015/11/25.
 */
public class RoleAnnotationHandler {

    public void assertAuthorized(Annotation a) {
        if (!(a instanceof RequiresRoles)) return;

        RequiresRoles rrAnnotation = (RequiresRoles) a;
        String[] roles = rrAnnotation.value();

        if (roles.length == 1) {
            if(!UserUtils.hasRole(roles[0])){
                throw new UnAuthorizedException("无访问权限");
            }
        }
        if (Logical.AND.equals(rrAnnotation.logical())) {
            if(!UserUtils.hasRoles(roles)){
                throw new UnAuthorizedException("无访问权限");
            }
        }

        if (Logical.OR.equals(rrAnnotation.logical())) {
            if(UserUtils.hasAnyRoles(roles)){
                throw new UnAuthorizedException("无访问权限");
            }
        }
    }
}
