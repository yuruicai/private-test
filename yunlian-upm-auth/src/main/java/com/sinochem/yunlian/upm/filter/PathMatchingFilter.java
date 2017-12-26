package com.sinochem.yunlian.upm.filter;

import com.sinochem.yunlian.upm.filter.matcher.AntPathMatcher;
import com.sinochem.yunlian.upm.filter.matcher.PatternMatcher;
import com.sinochem.yunlian.upm.filter.servlet.PathConfigProcessor;
import com.sinochem.yunlian.upm.filter.servlet.WebUtils;
import com.sinochem.yunlian.upm.filter.util.StringUtils;
import com.sinochem.yunlian.upm.tools.UpmAuthService;
import com.sinochem.yunlian.upm.tools.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class PathMatchingFilter extends AdviceFilter implements PathConfigProcessor {
    private static final Logger log = LoggerFactory.getLogger(PathMatchingFilter.class);
    private final String DEFAULT_SID_NAME = "ssoid";
    private String tokenCookie = DEFAULT_SID_NAME;
    private UpmAuthService authService;

    protected String getToken(String ticket){
        try {
            return authService.getToken(ticket);
        }catch (Exception e){
            log.error("ticket无效　{}", ticket);
        }
        return null;
    }

    protected User getUser(String sid) {
        return authService.getUser(sid);
    }

    public void setAuthService(UpmAuthService authService) {
        this.authService = authService;
    }

    public UpmAuthService getAuthService() {
        return authService;
    }

    public String getTokenCookie() {
        return tokenCookie;
    }

    public void setTokenCookie(String tokenCookie) {
        this.tokenCookie = tokenCookie;
    }

    protected PatternMatcher pathMatcher = new AntPathMatcher();

    protected Map<String, Object> appliedPaths = new LinkedHashMap<String, Object>();

    public Filter processPathConfig(String path, String config) {
        String[] values = null;
        if (config != null) {
            values = StringUtils.split(config);
        }
        this.appliedPaths.put(path, values);
        return this;
    }

    protected String getPathWithinApplication(ServletRequest request) {
        return WebUtils.getPathWithinApplication(WebUtils.toHttp(request));
    }

    protected boolean pathsMatch(String path, ServletRequest request) {
        String requestURI = getPathWithinApplication(request);
        log.trace("Attempting to match pattern '{}' with current requestURI '{}'...", path, requestURI);
        return pathsMatch(path, requestURI);
    }

    protected boolean pathsMatch(String pattern, String path) {
        return pathMatcher.matches(pattern, path);
    }

    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {

        if (this.appliedPaths == null || this.appliedPaths.isEmpty()) {
            if (log.isTraceEnabled()) {
                log.trace("appliedPaths property is null or empty.  This Filter will passthrough immediately.");
            }
            return true;
        }

        for (String path : this.appliedPaths.keySet()) {
            if (pathsMatch(path, request)) {
                log.trace("Current requestURI matches pattern '{}'.  Determining filter chain execution...", path);
                Object config = this.appliedPaths.get(path);
                return isFilterChainContinued(request, response, path, config);
            }
        }
        return true;
    }

    private boolean isFilterChainContinued(ServletRequest request, ServletResponse response,
                                           String path, Object pathConfig) throws Exception {

        if (isEnabled(request, response, path, pathConfig)) {
            if (log.isTraceEnabled()) {
                log.trace("Filter '{}' is enabled for the current request under path '{}' with config [{}].  " +
                        "Delegating to subclass implementation for 'onPreHandle' check.",
                        new Object[]{getName(), path, pathConfig});
            }
            return onPreHandle(request, response, pathConfig);
        }

        if (log.isTraceEnabled()) {
            log.trace("Filter '{}' is disabled for the current request under path '{}' with config [{}].  " +
                    "The next element in the FilterChain will be called immediately.",
                    new Object[]{getName(), path, pathConfig});
        }
        return true;
    }

    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return true;
    }

    protected boolean isEnabled(ServletRequest request, ServletResponse response, String path, Object mappedValue)
            throws Exception {
        return isEnabled(request, response);
    }
}
