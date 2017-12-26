package com.sinochem.yunlian.upm.common.net;

import com.sinochem.yunlian.upm.common.rpc.Request;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangxi
 * @created 13-1-17
 */
public class MtHttpRequest implements Request {
    private String provider;
    private String consumer;
    private String apikey;
    private String host;
    private String path;
    private String method;
    private Map<String, Object> headers = new HashMap<String, Object>();
    private Map<String, Object> params = new HashMap<String, Object>();
    private Map<String, Object> entitys = new HashMap<String, Object>();

    @Override
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public void setHeaders(Map<String, Object> headers) {
        this.headers = headers;
    }

    public Map<String, Object> getHeaders() {
        return this.headers;
    }

    public static MtHttpRequest from(URL url) {
        MtHttpRequest request = new MtHttpRequest();
        request.setHost(url.getHost());
        request.setPath(url.getPath());
        request.setMethod(url.getMethod());
        request.setParams(url.getParams());
        request.setHeaders(url.getHeaders());
        request.setEntitys(url.getEntitys());
        return request;
    }

    @Override
    public void setProvider(String provider) {
        this.provider = provider;
    }

    @Override
    public String getProvider() {
        return provider;
    }

    @Override
    public void setConsumer(String consumer) {
        this.consumer = consumer;
    }

    @Override
    public String getConsumer() {
        return consumer;
    }

    @Override
    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    @Override
    public String getApikey() {
        return apikey;
    }

    @Override
    public boolean needAuth() {
        return StringUtils.isNotBlank(getClientKey()) && StringUtils.isNotBlank(getSecret());
    }

    @Override
    public String getSecret() {
        return (String) headers.get("secret");
    }

    @Override
    public void setSecret(String secret) {
        headers.put("secret", secret);
    }

    @Override
    public String getClientKey() {
        return (String) headers.get("key");
    }

    @Override
    public void setClientKey(String key) {
        headers.put("key", key);
    }

    @Override
    public Integer getTimeout() {
        return (Integer) headers.get("timeout");
    }

    @Override
    public Integer getConnectionTimeout() {
        return (Integer) headers.get("connectionTimeout");
    }

    @Override
    public void setConnectionTimeout(Integer connectionTimeout) {
        headers.put("connectionTimeout", connectionTimeout);
    }

    @Override
    public Integer getSoTimeout() {
        return (Integer) headers.get("soTimeout");
    }

    @Override
    public void setSoTimeout(Integer soTimeout) {
        headers.put("soTimeout", soTimeout);
    }

    @Override
    public Integer getRetry() {
        return (Integer) headers.get("retry");
    }

    @Override
    public void setRetry(Integer retry) {
        headers.put("retry", retry);
    }

    public Map<String, Object> getEntitys() {
        return entitys;
    }

    public void setEntitys(Map<String, Object> entitys) {
        this.entitys = entitys;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MtHttpRequest{");
        sb.append("provider='").append(provider).append('\'');
        sb.append(", consumer='").append(consumer).append('\'');
        sb.append(", apikey='").append(apikey).append('\'');
        sb.append(", host='").append(host).append('\'');
        sb.append(", path='").append(path).append('\'');
        sb.append(", method='").append(method).append('\'');
        sb.append(", params=").append(params);
        sb.append(", entitys=").append(entitys);
        sb.append('}');
        return sb.toString();
    }
}
