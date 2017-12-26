package com.sinochem.yunlian.upm.filter.spring;

import com.sinochem.yunlian.upm.filter.servlet.*;
import com.sinochem.yunlian.upm.filter.AbstractMtFilter;
import com.sinochem.yunlian.upm.filter.AccessControlFilter;
import com.sinochem.yunlian.upm.filter.PathMatchingFilter;
import com.sinochem.yunlian.upm.filter.auth.AuthFilter;
import com.sinochem.yunlian.upm.filter.cache.CacheManager;
import com.sinochem.yunlian.upm.filter.sso.LoginFilter;
import com.sinochem.yunlian.upm.filter.sso.LogoutFilter;
import com.sinochem.yunlian.upm.filter.util.ObjectHolder;
import com.sinochem.yunlian.upm.filter.util.SSOUtils;
import com.sinochem.yunlian.upm.filter.util.StringUtils;
import com.sinochem.yunlian.upm.tools.UpmAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.CollectionUtils;

import javax.servlet.Filter;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilterFactoryBean implements FactoryBean, BeanPostProcessor {
    private static transient final Logger log = LoggerFactory.getLogger(FilterFactoryBean.class);
    public static final String FILTERS = "filters";
    public static final String URLS = "urls";
    private Map<String, Filter> filters;
    private Map<String, String> filterChainDefinitionMap;
    private Map<String, String> defaultDefinitions;

    private final String DEFAULT_SID_NAME = "ssoid";
    private String tokenCookie = DEFAULT_SID_NAME;
    private String appkey;
    private String secret;
    private String authUrl;
    private String loginUrl;
    private String callbackUri;
    private String successUrl;
    private String unauthorizedUrl;
    private boolean throwUnAuthorizedException;
    private boolean ignoreAll;
    private boolean autoLoad = true;
    private boolean isCacheBodyData = false;
    private boolean ignoreAcceptHeader = true;
    private int timeout = 0;

    @Autowired
    private UpmAuthService authService;

    private AbstractMtFilter instance;

    private CacheManager cacheManager;

    public FilterFactoryBean() {
        this.filters = new LinkedHashMap<String, Filter>();
        this.filterChainDefinitionMap = new LinkedHashMap<String, String>(); //order matters!
        this.defaultDefinitions = new LinkedHashMap<String, String>(); //order matters!
        // add for login
        defaultDefinitions.put("/mt-sso", "login");
        defaultDefinitions.put("/logout", "logout");
        defaultDefinitions.put("/favicon.ico", "anon");
        defaultDefinitions.put("/static/**", "anon");
    }

    public UpmAuthService getAuthService() {
        return authService;
    }

    public void setAuthService(UpmAuthService authService) {
        this.authService = authService;
    }

    public CacheManager getCacheManager() {
        return cacheManager;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public String getSuccessUrl() {
        return successUrl;
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    public String getUnauthorizedUrl() {
        return unauthorizedUrl;
    }

    public void setUnauthorizedUrl(String unauthorizedUrl) {
        this.unauthorizedUrl = unauthorizedUrl;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public Map<String, Filter> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, Filter> filters) {
        this.filters = filters;
    }

    public Map<String, String> getFilterChainDefinitionMap() {
        return filterChainDefinitionMap;
    }

    public void setFilterChainDefinitionMap(Map<String, String> filterChainDefinitionMap) {
        this.filterChainDefinitionMap = filterChainDefinitionMap;
    }

    public void setFilterChainDefinitions(String definitions) {
        Ini ini = new Ini();
        List<String> authUrls = null;
        if (!StringUtils.hasText(getAppkey())) {
            if (authService != null && StringUtils.hasText(authService.getClientId())) {
                setAppkey(authService.getClientId());
                setSecret(authService.getSecret());
            } else {
                setAppkey(appkey);
                if (StringUtils.hasText(appkey) && authService != null) {
                    authService.setClientId(appkey);
                    authService.setSecret(secret);
                }
            }
        }
        if (StringUtils.hasText(getAppkey()) && authService != null && !StringUtils.hasText(authService.getClientId())) {
            authService.setClientId(appkey);
        }
        if (StringUtils.hasText(getAuthUrl()) && authService != null && !StringUtils.hasText(authService.getAuthUrl())) {
            authService.setAuthUrl(getAuthUrl());
        }
        if (isAutoLoad()) {
            authUrls = authService.getUrls(getAppkey());
        }
        String urls = merge(defaultDefinitions, authUrls, definitions);
        log.info("merged urls: {}", urls);
        ini.load(urls);
        //did they explicitly state a 'urls' section?  Not necessary, but just in case:
        Ini.Section section = ini.getSection(URLS);
        if (CollectionUtils.isEmpty(section)) {
            //no urls section.  Since this _is_ a urls chain definition property, just assume the
            //default section contains only the definitions:
            section = ini.getSection(Ini.DEFAULT_SECTION_NAME);
        }
        setFilterChainDefinitionMap(section);
    }

    private String merge(Map<String, String> defaultDefinitions, List<String> authUrls, String definitions) {
        log.info("defaultDefinitions: {}", defaultDefinitions);
        log.info("authUrls: {}", authUrls);
        log.info("filterChainDefinitions: {}", definitions);
        Map<String, String> defines = new LinkedHashMap<String, String>();
        Pattern pattern = Pattern.compile("(.*) = (.*)\n");
        Matcher matcher = pattern.matcher(definitions);
        while (matcher.find()) {
            defines.put(matcher.group(1), matcher.group(2));
        }
        StringBuilder sb = new StringBuilder();
        for (Iterator<Map.Entry<String, String>> itr = defaultDefinitions.entrySet().iterator(); itr.hasNext(); ) {
            Map.Entry<String, String> entry = itr.next();
            String key = entry.getKey();
            String value = entry.getValue();
            if (defines.keySet().contains(key)) {
                continue;
            }
            sb.append(key);
            sb.append(" = ");
            sb.append(value);
            sb.append("\n");
        }
        //从权限系统获取到的url优先配置成authMenu
        if (authUrls != null) {
            for (String url : authUrls) {
                if (defines.keySet().contains(url) || url.equalsIgnoreCase("/**")) {
                    continue;
                }
                sb.append(url);
                sb.append(" = ");
                sb.append("authMenu\n");
            }
        }

        for (Iterator<Map.Entry<String, String>> itr = defines.entrySet().iterator(); itr.hasNext(); ) {
            Map.Entry<String, String> entry = itr.next();
            String key = entry.getKey();
            String value = entry.getValue();

            sb.append(key);
            sb.append(" = ");
            sb.append(value);
            sb.append("\n");
        }

        return sb.toString();
    }

    public Object getObject() throws Exception {
        if (instance == null) {
            instance = createInstance();
        }
        return instance;
    }

    public Class getObjectType() {
        return SpringFilter.class;
    }

    public boolean isSingleton() {
        return true;
    }

    protected FilterChainManager createFilterChainManager() {
        DefaultFilterChainManager manager = new DefaultFilterChainManager();
        if (isIgnoreAll()) {
            return manager;
        }
        Map<String, Filter> defaultFilters = manager.getFilters();
        //apply global settings if necessary:
        for (Filter filter : defaultFilters.values()) {
            applyGlobalPropertiesIfNecessary(filter);
        }

        //Apply the acquired and/or configured filters:
        Map<String, Filter> filters = getFilters();
        if (!CollectionUtils.isEmpty(filters)) {
            for (Map.Entry<String, Filter> entry : filters.entrySet()) {
                String name = entry.getKey();
                Filter filter = entry.getValue();
                applyGlobalPropertiesIfNecessary(filter);
                if (filter instanceof Nameable) {
                    ((Nameable) filter).setName(name);
                }
                //'init' argument is false, since Spring-configured filters should be initialized
                //in Spring (i.e. 'init-method=blah') or implement InitializingBean:
                manager.addFilter(name, filter, false);
            }
        }

        //build up the chains:
        Map<String, String> chains = getFilterChainDefinitionMap();
        if (!CollectionUtils.isEmpty(chains)) {
            for (Map.Entry<String, String> entry : chains.entrySet()) {
                String url = entry.getKey();
                String chainDefinition = entry.getValue();
                manager.createChain(url, chainDefinition);
            }
        }

        return manager;
    }

    protected AbstractMtFilter createInstance() throws Exception {
        log.debug("Creating Filter instance.");
        applyTimeout();
        FilterChainManager manager = createFilterChainManager();
        PathMatchingFilterChainResolver chainResolver = new PathMatchingFilterChainResolver();
        chainResolver.setFilterChainManager(manager);
        //设置全局需要使用的对象
        ObjectHolder.setUpmAuthService(authService);
        ObjectHolder.setUnauthorizedUrl(getUnauthorizedUrl());
        if(getCacheManager() != null){
            ObjectHolder.setCacheManager(getCacheManager());
        }
        if (isCacheBodyData) {
            return new CacheBodySpringFilter(chainResolver);
        } else {
            return new SpringFilter(chainResolver);
        }
    }

    private void applyLoginUrl(Filter filter) {
        // login
        if (filter instanceof LoginFilter && StringUtils.hasText(getSuccessUrl())) {
            LoginFilter loginFilter = (LoginFilter) filter;
            loginFilter.setSuccessUrl(getSuccessUrl());
        }
        // logout
        if (filter instanceof LogoutFilter && StringUtils.hasText(getLoginUrl())) {
            LogoutFilter logoutFilter = (LogoutFilter) filter;
            logoutFilter.setLogoutUrl(getLoginUrl());
            if (StringUtils.hasText(getCallbackUri())) {
                logoutFilter.setCallbackUri(getCallbackUri());
            }
        }
        if (filter instanceof PathMatchingFilter) {
            ((PathMatchingFilter) filter).setTokenCookie(getTokenCookie());
            ((PathMatchingFilter) filter).setAuthService(authService);
        }
        if (filter instanceof AccessControlFilter) {
            AccessControlFilter acFilter = (AccessControlFilter) filter;
            acFilter.setAppkey(getAppkey());
            acFilter.setSecret(getSecret());
            acFilter.setLoginUrl(getLoginUrl());
            acFilter.setIgnoreAcceptHeader(isIgnoreAcceptHeader());
            if (StringUtils.hasText(getCallbackUri())) {
                acFilter.setCallbackUri(getCallbackUri());
            }
        }
    }

    private void applyAuthUrl(Filter filter) {
        if (filter instanceof AuthFilter) {
            AuthFilter authFilter = (AuthFilter) filter;
            authFilter.setThrowUnAuthorizedException(getThrowUnAuthorizedException());
            String unauthorizedUrl = getUnauthorizedUrl();
            if (StringUtils.hasText(unauthorizedUrl)) {
                //only apply the unauthorizedUrl if they haven't explicitly configured one already:
                String existingUnauthorizedUrl = authFilter.getUnauthorizedUrl();
                if (existingUnauthorizedUrl == null) {
                    authFilter.setUnauthorizedUrl(unauthorizedUrl);
                }
            }
        }
    }

    private void applyTimeout(){
        if(this.timeout != 0){
            SSOUtils.TIMEOUT = timeout;
        }
    }

    private void applyGlobalPropertiesIfNecessary(Filter filter) {
        applyLoginUrl(filter);
        applyAuthUrl(filter);
    }

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof Filter) {
            log.debug("Found filter chain candidate filter '{}'", beanName);
            Filter filter = (Filter) bean;
            applyGlobalPropertiesIfNecessary(filter);
            getFilters().put(beanName, filter);
        } else {
            log.trace("Ignoring non-Filter bean '{}'", beanName);
        }
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public Boolean getThrowUnAuthorizedException() {
        return throwUnAuthorizedException;
    }

    public void setThrowUnAuthorizedException(Boolean throwUnAuthorizedException) {
        this.throwUnAuthorizedException = throwUnAuthorizedException;
    }

    public String getCallbackUri() {
        return callbackUri;
    }

    public void setCallbackUri(String callbackUri) {
        this.callbackUri = callbackUri;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
        authService.setLoginUrl(loginUrl);

    }

    public String getAuthUrl() {
        return authUrl;
    }

    public void setAuthUrl(String authUrl) {
        this.authUrl = authUrl;
        if (StringUtils.hasText(authUrl) && authService != null && !StringUtils.hasText(authService.getAuthUrl())) {
            authService.setAuthUrl(authUrl);
        }
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
        if (StringUtils.hasText(appkey) && authService != null && !StringUtils.hasText(authService.getClientId())) {
            authService.setClientId(appkey);
        }
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getTokenCookie() {
        return tokenCookie;
    }

    public void setTokenCookie(String tokenCookie) {
        this.tokenCookie = tokenCookie;
    }

    public boolean isIgnoreAll() {
        return ignoreAll;
    }

    public void setIgnoreAll(boolean ignoreAll) {
        this.ignoreAll = ignoreAll;
    }

    public boolean isAutoLoad() {
        return autoLoad;
    }

    public void setAutoLoad(boolean autoLoad) {
        this.autoLoad = autoLoad;
    }

    public void setCacheBodyData(boolean isCacheBodyData) {
        this.isCacheBodyData = isCacheBodyData;
    }

    public boolean isIgnoreAcceptHeader() {
        return ignoreAcceptHeader;
    }

    public void setIgnoreAcceptHeader(boolean ignoreAcceptHeader) {
        this.ignoreAcceptHeader = ignoreAcceptHeader;
    }

    private static final class SpringFilter extends AbstractMtFilter {
        protected SpringFilter(FilterChainResolver resolver) {
            super();
            if (resolver != null) {
                setFilterChainResolver(resolver);
            }
        }
    }

    private static final class CacheBodySpringFilter extends AbstractMtFilter {
        protected CacheBodySpringFilter(FilterChainResolver resolver) {
            super();
            if (resolver != null) {
                setFilterChainResolver(resolver);
            }
        }

        @Override
        protected ServletRequest wrapServletRequest(HttpServletRequest orig) {
            return new MtCacheBodyHttpServletRequest(orig, getServletContext(), isHttpSessions());
        }
    }
}
