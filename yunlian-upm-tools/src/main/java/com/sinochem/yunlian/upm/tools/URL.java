package com.sinochem.yunlian.upm.tools;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

final class URL implements Serializable {
    private String host;
    private String path;
    private String method;
    private Map<String, Object> headers;
    private Map<String, Object> params;

    private URL() {
    }

    public URL(Builder builder) {
        this.host = builder.host;
        this.path = builder.path;
        this.method = builder.method;
        if (builder.headers == null) {
            builder.headers = new HashMap<String, Object>();
        }
        this.headers = builder.headers;
        if (builder.params == null) {
            builder.params = new HashMap<String, Object>();
        }
//        this.params = Collections.unmodifiableMap(builder.params);
        // still can modify, maybe bugs
        this.params = builder.params;
    }

    public static Builder builder() {
        return new Builder();
    }


    public static class Builder {
        private String host;
        private String path;
        private String method;
        private Map<String, Object> headers;
        private Map<String, Object> params;

        public URL build() {
            return new URL(this);
        }

        public Builder host(String host) {
            assert (host != null);
            this.host = host;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder method(String method) {
            assert (path == null || (path != null && path.startsWith("/")));
            this.method = method;
            return this;
        }

        public Builder headers(Map<String, Object> values) {
            if (headers == null) {
                if (values == null) {
                    headers = new HashMap<String, Object>();
                } else {
                    headers = new HashMap<String, Object>(values);
                }
            } else if (values != null) {
                headers.putAll(values);
            }
            return this;
        }

        public Builder header(String key, Object value) {
            if (headers == null) {
                headers = new HashMap<String, Object>();
            }
            this.headers.put(key, value);
            return this;
        }

        public Builder params(Map<String, Object> parameters) {
            if (params == null) {
                if (parameters == null) {
                    params = new HashMap<String, Object>();
                } else {
                    params = new HashMap<String, Object>(parameters);
                }
            } else if (parameters != null) {
                params.putAll(parameters);
            }
            return this;
        }

        public Builder param(String key, Object value) {
            if (params == null) {
                params = new HashMap<String, Object>();
            }
            this.params.put(key, value);
            return this;
        }
    }

    public String getHost() {
        return host;
    }

    public String getPath() {
        return path;
    }

    public String getMethod() {
        return method;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(host);
        sb.append(path);
        boolean first = true;
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (entry.getKey() != null && entry.getKey().length() > 0) {
                if (first) {
                    sb.append("?");
                    first = false;
                } else {
                    sb.append("&");
                }
                sb.append(entry.getKey());
                sb.append("=");
                sb.append(entry.getValue() == null ? "" : entry.getValue().toString().trim());
            }
        }
        first = true;
        for (Map.Entry<String, Object> entry : headers.entrySet()) {
            if (entry.getKey() != null && entry.getKey().length() > 0) {
                if (first) {
                    sb.append("(");
                    first = false;
                } else {
                    sb.append("&");
                }
                sb.append(entry.getKey());
                sb.append("=");
                sb.append(entry.getValue() == null ? "" : entry.getValue().toString().trim());
            }
        }
        if (!first) {
            sb.append(")");
        }
        return sb.toString();
    }
}