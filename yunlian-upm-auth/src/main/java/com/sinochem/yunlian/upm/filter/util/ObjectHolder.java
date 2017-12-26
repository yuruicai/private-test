package com.sinochem.yunlian.upm.filter.util;

import com.sinochem.yunlian.upm.filter.cache.CacheManager;
import com.sinochem.yunlian.upm.tools.UpmAuthService;

/**
 * Created by zhanghongze on 2015/11/24.
 */
public class ObjectHolder {

    private static UpmAuthService upmAuthService;

    private static String unauthorizedUrl;

    private static CacheManager cacheManager;

    public static UpmAuthService getUpmAuthService() {
        return upmAuthService;
    }

    public static void setUpmAuthService(UpmAuthService upmAuthService) {
        ObjectHolder.upmAuthService = upmAuthService;
    }

    public static String getUnauthorizedUrl() {
        return unauthorizedUrl;
    }

    public static void setUnauthorizedUrl(String unauthorizedUrl) {
        ObjectHolder.unauthorizedUrl = unauthorizedUrl;
    }

    public static CacheManager getCacheManager() {
        return cacheManager;
    }

    public static void setCacheManager(CacheManager cacheManager) {
        ObjectHolder.cacheManager = cacheManager;
    }


}
