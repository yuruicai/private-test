package com.sinochem.yunlian.upm.filter.servlet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.net.URLEncoder;

public class MtHttpServletResponse extends HttpServletResponseWrapper {
    private ServletContext context = null;
    private MtHttpServletRequest request = null;

    public MtHttpServletResponse(HttpServletResponse wrapped, ServletContext context, MtHttpServletRequest request) {
        super(wrapped);
        this.context = context;
        this.request = request;
    }

    public ServletContext getContext() {
        return context;
    }

    public void setContext(ServletContext context) {
        this.context = context;
    }

    public MtHttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(MtHttpServletRequest request) {
        this.request = request;
    }

    private String toAbsolute(String location) {

        if (location == null)
            return (location);

        boolean leadingSlash = location.startsWith("/");

        if (leadingSlash || !hasScheme(location)) {

            StringBuilder buf = new StringBuilder();

            String scheme = request.getScheme();
            String name = request.getServerName();
            int port = request.getServerPort();

            try {
                buf.append(scheme).append("://").append(name);
                if ((scheme.equals("http") && port != 80)
                        || (scheme.equals("https") && port != 443)) {
                    buf.append(':').append(port);
                }
                if (!leadingSlash) {
                    String relativePath = request.getRequestURI();
                    int pos = relativePath.lastIndexOf('/');
                    relativePath = relativePath.substring(0, pos);

                    String encodedURI = URLEncoder.encode(relativePath, getCharacterEncoding());
                    buf.append(encodedURI).append('/');
                }
                buf.append(location);
            } catch (IOException e) {
                IllegalArgumentException iae = new IllegalArgumentException(location);
                iae.initCause(e);
                throw iae;
            }

            return buf.toString();

        } else {
            return location;
        }
    }

    public static boolean isSchemeChar(char c) {
        return Character.isLetterOrDigit(c) ||
                c == '+' || c == '-' || c == '.';
    }

    private boolean hasScheme(String uri) {
        int len = uri.length();
        for (int i = 0; i < len; i++) {
            char c = uri.charAt(i);
            if (c == ':') {
                return i > 0;
            } else if (!isSchemeChar(c)) {
                return false;
            }
        }
        return false;
    }
}
