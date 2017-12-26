package com.sinochem.yunlian.upm.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class AnonymousFilter extends PathMatchingFilter {

    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) {
        return true;
    }
}
