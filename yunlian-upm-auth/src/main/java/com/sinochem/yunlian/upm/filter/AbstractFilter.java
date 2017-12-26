package com.sinochem.yunlian.upm.filter;

import com.sinochem.yunlian.upm.filter.util.StringUtils;
import com.sinochem.yunlian.upm.filter.servlet.ServletContextSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

public abstract class AbstractFilter extends ServletContextSupport implements Filter {

    private static transient final Logger log = LoggerFactory.getLogger(AbstractFilter.class);

    protected FilterConfig filterConfig;

    public FilterConfig getFilterConfig() {
        return filterConfig;
    }

    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        setServletContext(filterConfig.getServletContext());
    }

    protected String getInitParam(String paramName) {
        FilterConfig config = getFilterConfig();
        if (config != null) {
            return StringUtils.clean(config.getInitParameter(paramName));
        }
        return null;
    }

    public final void init(FilterConfig filterConfig) throws ServletException {
        setFilterConfig(filterConfig);
        try {
            onFilterConfigSet();
        } catch (Exception e) {
            if (e instanceof ServletException) {
                throw (ServletException) e;
            } else {
                if (log.isErrorEnabled()) {
                    log.error("Unable to start Filter: [" + e.getMessage() + "].", e);
                }
                throw new ServletException(e);
            }
        }
    }

    protected void onFilterConfigSet() throws Exception {
    }

    public void destroy() {
    }
}
