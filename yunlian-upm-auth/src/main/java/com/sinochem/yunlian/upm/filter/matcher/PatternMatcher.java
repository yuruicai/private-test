package com.sinochem.yunlian.upm.filter.matcher;

public interface PatternMatcher {
    boolean matches(String pattern, String source);
}
