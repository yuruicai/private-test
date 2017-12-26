package com.sinochem.yunlian.upm.admin.password;

/**
 * @author zhangxi
 * @created 13-12-22
 */
public interface Rule {
    RuleResult check(String username, String password);
}
