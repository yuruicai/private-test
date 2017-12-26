package com.sinochem.yunlian.upm.admin.component;

import com.googlecode.ehcache.annotations.Cacheable;
import com.sinochem.yunlian.upm.filter.matcher.AntPathMatcher;
import com.sinochem.yunlian.upm.filter.matcher.PatternMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author zhangxi
 * @created 13-8-9
 */
@Component
public class UpmMatcher {
    private static final Logger LOG = LoggerFactory.getLogger(UpmMatcher.class);
    protected PatternMatcher pathMatcher = new AntPathMatcher();

    @Cacheable(cacheName = "LRUCache-1d", cacheNull = false)
    public boolean match(String pattern, String resource) {
        boolean result;
        if (pattern.contains("*")) {
            result = pathMatcher.matches(pattern, resource);
        } else {
            result = pattern.equalsIgnoreCase(resource);
        }
        LOG.info("match for " + pattern + "," + resource + "," + result);
        return result;
    }
}
