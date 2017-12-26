package com.sinochem.yunlian.upm.filter.servlet;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public interface FilterChainResolver {
    FilterChain getChain(ServletRequest request, ServletResponse response, FilterChain originalChain);
}
