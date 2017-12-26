package com.sinochem.yunlian.upm.filter;

import com.sinochem.yunlian.upm.filter.servlet.FilterChainResolver;
import com.sinochem.yunlian.upm.filter.servlet.MtHttpServletRequest;
import com.sinochem.yunlian.upm.filter.servlet.MtHttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AbstractMtFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(AbstractMtFilter.class);
    private FilterChainResolver filterChainResolver;

    protected AbstractMtFilter() {
    }

    public FilterChainResolver getFilterChainResolver() {
        return filterChainResolver;
    }

    public void setFilterChainResolver(FilterChainResolver filterChainResolver) {
        this.filterChainResolver = filterChainResolver;
    }


    protected final void onFilterConfigSet() throws Exception {
        init();
    }

    public void init() throws Exception {
    }

    protected boolean isHttpSessions() {
        return true;
    }

    protected ServletRequest wrapServletRequest(HttpServletRequest orig) {
        return new MtHttpServletRequest(orig, getServletContext(), isHttpSessions());
    }

    protected ServletRequest prepareServletRequest(ServletRequest request, ServletResponse response, FilterChain chain) {
        ServletRequest toUse = request;
        if (request instanceof HttpServletRequest) {
            HttpServletRequest http = (HttpServletRequest) request;
            toUse = wrapServletRequest(http);
        }
        return toUse;
    }

    protected ServletResponse wrapServletResponse(HttpServletResponse orig, MtHttpServletRequest request) {
        return new MtHttpServletResponse(orig, getServletContext(), request);
    }

    protected ServletResponse prepareServletResponse(ServletRequest request, ServletResponse response, FilterChain chain) {
        ServletResponse toUse = response;
        if (!isHttpSessions() && (request instanceof MtHttpServletRequest) &&
                (response instanceof HttpServletResponse)) {
            toUse = wrapServletResponse((HttpServletResponse) response, (MtHttpServletRequest) request);
        }
        return toUse;
    }

    protected void doFilterInternal(ServletRequest servletRequest, ServletResponse servletResponse, final FilterChain chain)
            throws ServletException, IOException {
        Throwable t = null;
        try {
            final ServletRequest request = prepareServletRequest(servletRequest, servletResponse, chain);
            final ServletResponse response = prepareServletResponse(request, servletResponse, chain);
            executeChain(request, response, chain);
        } catch (Throwable throwable) {
            t = throwable;
        }

        if (t != null) {
            if (t instanceof ServletException) {
                throw (ServletException) t;
            }
            if (t instanceof IOException) {
                throw (IOException) t;
            }
            String msg = "Filtered request failed.";
            throw new ServletException(msg, t);
        }
    }

    protected FilterChain getExecutionChain(ServletRequest request, ServletResponse response, FilterChain origChain) {
        FilterChain chain = origChain;

        FilterChainResolver resolver = getFilterChainResolver();
        if (resolver == null) {
            log.debug("No FilterChainResolver configured.  Returning original FilterChain.");
            return origChain;
        }

        FilterChain resolved = resolver.getChain(request, response, origChain);
        if (resolved != null) {
            log.trace("Resolved a configured FilterChain for the current request.");
            chain = resolved;
        } else {
            log.trace("No FilterChain configured for the current request.  Using the default.");
        }

        return chain;
    }

    protected void executeChain(ServletRequest request, ServletResponse response, FilterChain origChain)
            throws IOException, ServletException {
        FilterChain chain = getExecutionChain(request, response, origChain);
        chain.doFilter(request, response);
    }
}
