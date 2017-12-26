package com.sinochem.yunlian.upm.filter.servlet;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

public class SavedRequest implements Serializable {
    private String method;
    private String queryString;
    private String requestURI;

    public SavedRequest(HttpServletRequest request) {
        this.method = request.getMethod();
        this.queryString = request.getQueryString();
        this.requestURI = request.getRequestURI();
    }

    public String getMethod() {
        return method;
    }

    public String getQueryString() {
        return queryString;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public String getRequestUrl() {
        StringBuilder requestUrl = new StringBuilder(getRequestURI());
        if (getQueryString() != null) {
            requestUrl.append("?").append(getQueryString());
        }
        return requestUrl.toString();
    }
}
