package com.sinochem.yunlian.upm.filter.auth;

import com.sinochem.yunlian.upm.filter.util.UserUtils;
import com.sinochem.yunlian.upm.tools.Menu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.List;

/**
 * @author zhangxi
 * @created 13-7-2
 */
public class AuthWithMenuFilter extends AuthFilter {
    private static final Logger LOG = LoggerFactory.getLogger(AuthWithMenuFilter.class);

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        boolean result = super.isAccessAllowed(request, response, mappedValue);
        if (LOG.isDebugEnabled()) {
            LOG.debug("auth " + result);
        }
        if (result) {
            fetchMenu(request);
        }
        return result;
    }

    private void fetchMenu(ServletRequest request) {
        List<Menu> menus = UserUtils.fetchMenus();
        request.setAttribute("__menus__", menus);
    }
}
