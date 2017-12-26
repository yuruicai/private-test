package com.sinochem.yunlian.upm.filter.annotation;

import com.sinochem.yunlian.upm.filter.exception.UnAuthorizedException;
import com.sinochem.yunlian.upm.filter.util.UserUtils;

import java.lang.annotation.Annotation;

/**
 * Created by zhanghongze on 2015/11/25.
 */
public class PermissionAnnotationHandler {

    public void assertAuthorized(Annotation a) {
        if (!(a instanceof RequiresPermissions)) return;

        RequiresPermissions rpAnnotation = (RequiresPermissions) a;
        String[] perms = rpAnnotation.value();

        if (perms.length == 1) {
            if(!UserUtils.hasPermission(perms[0])){
                throw new UnAuthorizedException("无访问权限");
            }
        }
        if (Logical.AND.equals(rpAnnotation.logical())) {
            if(!UserUtils.hasPermissions(perms)){
                throw new UnAuthorizedException("无访问权限");
            }
        }

        if (Logical.OR.equals(rpAnnotation.logical())) {
            if(!UserUtils.hasAnyPermissions(perms)){
                throw new UnAuthorizedException("无访问权限");
            }
        }

    }
}
