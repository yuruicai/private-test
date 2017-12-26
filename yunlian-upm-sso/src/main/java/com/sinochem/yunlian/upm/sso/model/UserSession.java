package com.sinochem.yunlian.upm.sso.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangxi
 * @created 14-1-19
 */
public class UserSession implements Serializable {
    private Map<TokenKey, String> sessionTokens = new HashMap();
    private Map<String, String> appTokens = new HashMap<String, String>();
    private Map<String, String> pcTokens = new HashMap<String, String>();

    public enum TokenKey {
        WEB,
        PC,
        APP
    }

    public Map<TokenKey, String> getSessionTokens() {
        return sessionTokens;
    }

    public void setSessionTokens(Map<TokenKey, String> sessionTokens) {
        this.sessionTokens = sessionTokens;
    }

    public Map<String, String> getAppTokens() {
        return appTokens;
    }

    public void setAppTokens(Map<String, String> appTokens) {
        this.appTokens = appTokens;
    }

    public Map<String, String> getPcTokens() {
        return pcTokens;
    }

    public void setPcTokens(Map<String, String> pcTokens) {
        this.pcTokens = pcTokens;
    }
}
