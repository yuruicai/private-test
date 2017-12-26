/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sinochem.yunlian.upm.sso.util;

/**
 * 安全等级.
 *
 * 用于密码生成等多处逻辑
 *
 * @author sobranie
 */
public enum SecurityLevel{

    KIDDING("极弱"),
    WEAK("弱"),
    MEDIUM("中等"),
    GOOD("好"),
    STRONG("强"),
    EXTREME("极强");

    private final String key;

    private SecurityLevel(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
