package com.sinochem.yunlian.upm.tools;

import java.util.Map;

/**
 * @author zhangxi
 * @created 13-1-17
 */
interface Request {

    void setProvider(String provider);

    String getProvider();

    void setConsumer(String consumer);

    String getConsumer();

    void setApikey(String apikey);

    String getApikey();

    String getHost();

    String getPath();

    String getMethod();

    Map<String, Object> getParams();

    boolean needAuth();

    String getSecret();

    String getClientKey();

    Integer getConnectionTimeout();

    void setConnectionTimeout(Integer connectionTimeout);

    Integer getSoTimeout();

    void setSoTimeout(Integer soTimeout);

    void setRetry(Integer retry);

    Integer getRetry();
}
