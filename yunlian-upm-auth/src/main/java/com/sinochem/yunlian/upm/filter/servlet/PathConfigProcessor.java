package com.sinochem.yunlian.upm.filter.servlet;

import javax.servlet.Filter;

public interface PathConfigProcessor {
    Filter processPathConfig(String path, String config);
}
