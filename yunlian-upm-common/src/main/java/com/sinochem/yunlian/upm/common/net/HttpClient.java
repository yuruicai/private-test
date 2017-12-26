package com.sinochem.yunlian.upm.common.net;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.sinochem.yunlian.upm.common.rpc.Paging;
import com.sinochem.yunlian.upm.common.rpc.Response;
import com.sinochem.yunlian.upm.common.security.AuthUtil;
import com.sinochem.yunlian.upm.common.rpc.ErrorCode;
import com.sinochem.yunlian.upm.common.time.TimeUtil;
import org.apache.http.*;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zhangxi
 * @created 13-1-17
 */
public class HttpClient {
    private static final Logger LOG = LoggerFactory.getLogger(HttpClient.class);

    @Deprecated
    public HttpResponse executeRaw(final MtHttpRequest request) {
        final String url = request.getHost() + request.getPath();
        final String method = request.getMethod();
        URL u;
        try {
            u = new URL(url);
        } catch (MalformedURLException e) {
            LOG.warn(e.getMessage(), e);
            throw createHttpException(e, request);
        }
        String uri = u.getPath();
        HttpRequestBase httpRequest = null;
        if ("GET".equalsIgnoreCase(method)) {
            httpRequest = new HttpGet(u.toString());
        } else if ("POST".equalsIgnoreCase(method)) {
            httpRequest = new HttpPost(u.toString());
        } else if ("PUT".equalsIgnoreCase(method)) {
            httpRequest = new HttpPut(u.toString());
        } else if ("DELETE".equalsIgnoreCase(method)) {
            httpRequest = new HttpDelete(u.toString());
        } else {
            httpRequest = new HttpGet(u.toString());
        }
        DefaultHttpClient httpClient = new DefaultHttpClient();
        // override by request
        int connectTimeout = getConnectionTimeout(request);
        int soTimeout = getSoTimeout(request);
        final int retry = getRetry(request);

        httpClient.getParams()
                .setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, connectTimeout);
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, soTimeout);
        HttpRequestRetryHandler myRetryHandler = new HttpRequestRetryHandler() {
            public boolean retryRequest(IOException exception, int executionCount,
                                        HttpContext context) {
                if (executionCount > retry) {
                    return false;
                } else {
                    LOG.info("retry " + executionCount + " " + url + " -> " +
                            exception.getMessage());
                }
                return true;
            }
        };
        httpClient.setHttpRequestRetryHandler(myRetryHandler);
        Map<String, Object> params = request.getParams();

        if ("GET".equals(method)) {
            if (params != null && params.size() > 0) {
                List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    if (entry.getValue() != null) {
                        String value = String.valueOf(entry.getValue());
                        nvps.add(new BasicNameValuePair(entry.getKey(), value));
                    }
                }
                HttpEntity httpEntity;
                try {
                    httpEntity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e) {
                    LOG.warn(e.getMessage(), e);
                    throw createHttpException(e, request);
                }
                String paramString;
                try {
                    paramString = EntityUtils.toString(httpEntity);
                } catch (IOException e) {
                    throw createHttpException(e, request);
                }
                String tmpUrl = httpRequest.getURI().toString();
                if (tmpUrl.contains("?")) {
                    tmpUrl += "&";
                } else {
                    tmpUrl += "?";
                }
                tmpUrl += paramString;
                try {
                    httpRequest.setURI(new URI(tmpUrl));
                } catch (URISyntaxException e) {
                    throw createHttpException(e, request);
                }
            }
            LOG.debug(method + " " + httpRequest.getURI());
        } else if ("POST".equals(method) || "PUT".equals(method) || "DELETE".equals(method)) {
            Map<String, Object> entitys = request.getEntitys();
            Object contentType = request.getHeaders().get("Content-Type");
            if ("POST".equalsIgnoreCase(method) && contentType != null && "application/x-www-form-urlencoded".equals(contentType)) {
                if (params != null && params.size() > 0) {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    for (Map.Entry<String, Object> entry : params.entrySet()) {
                        if (entry.getValue() != null) {
                            String value = String.valueOf(entry.getValue());
                            nvps.add(new BasicNameValuePair(entry.getKey(), value));
                        }
                    }
                    try {
                        HttpEntity httpEntity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                        if ("POST".equals(method)) {
                            HttpPost httpPost = (HttpPost) httpRequest;
                            httpPost.setEntity(httpEntity);
                        }
                    } catch (UnsupportedEncodingException e) {
                        LOG.warn(e.getMessage(), e);
                        throw createHttpException(e, request);
                    }
                }
            } else if (entitys != null && entitys.get("json") != null) {
                String jsonString = (String) entitys.get("json");
                HttpEntity httpEntity;
                try {
                    httpEntity = new StringEntity(jsonString, "application/json", "UTF-8");
                } catch (IOException e) {
                    throw createHttpException(e, request);
                }
                if ("POST".equals(method)) {
                    HttpPost httpPost = (HttpPost) httpRequest;
                    httpPost.setEntity(httpEntity);
                } else if ("PUT".equals(method)) {
                    HttpPut httpPut = (HttpPut) httpRequest;
                    httpPut.setEntity(httpEntity);
                }
                LOG.debug("params: " + jsonString);
            } else if (entitys != null && entitys.size() > 0) {
                MultipartEntity mpEntity = new MultipartEntity();
                try {
                    for (String key : entitys.keySet()) {
                        Object value = entitys.get(key);
                        if (value == null) {
                            continue;
                        }
                        if (value instanceof ContentBody) {
                            mpEntity.addPart(key, (ContentBody) value);
                        } else if (value instanceof String) {
                            String str = (String) value;
                            mpEntity.addPart(key, new StringBody(str, Charset.forName("UTF-8")));
                        } else if (value instanceof File) {
                            mpEntity.addPart(key, new FileBody((File) value));
                        } else {
                            throw new RuntimeException(
                                    "不识别该entity类型:" + value.getClass());
                        }
                    }
                    if ("POST".equals(method)) {
                        HttpPost httpPost = (HttpPost) httpRequest;
                        httpPost.setEntity(mpEntity);
                    } else if ("PUT".equals(method)) {
                        HttpPut httpPut = (HttpPut) httpRequest;
                        httpPut.setEntity(mpEntity);
                    }
                } catch (UnsupportedEncodingException e) {
                    throw createHttpException(e, request);
                }
            } else if (params != null && params.size() > 0) {
                String jsonString;
                HttpEntity httpEntity;
                //ObjectMapper objectMapper = new ObjectMapper();
                try {
                    jsonString = JSON.toJSONString(params);
                    //jsonString = objectMapper.writeValueAsString(params);
                    httpEntity = new StringEntity(jsonString, "application/json", "UTF-8");
                } catch (IOException e) {
                    throw createHttpException(e, request);
                }
                if ("POST".equals(method)) {
                    HttpPost httpPost = (HttpPost) httpRequest;
                    httpPost.setEntity(httpEntity);
                } else if ("PUT".equals(method)) {
                    HttpPut httpPut = (HttpPut) httpRequest;
                    httpPut.setEntity(httpEntity);
                }
                LOG.debug("params: " + jsonString);
            }
            LOG.debug(method + " " + httpRequest.getURI());
        }

        // set header first TODO 区分header、property
        for (String key : request.getHeaders().keySet()) {
            Object value = request.getHeaders().get(key);
            httpRequest.setHeader(key, String.valueOf(value));
        }

        // 设置Authorization
        if (request.needAuth()) {
            String clientKey = request.getClientKey();
            String secret = request.getSecret();
            String date = TimeUtil.getAuthDate(new Date());
            String authorization = AuthUtil.getAuthorization(uri, method, date, clientKey, secret);
            httpRequest.setHeader("Date", date);
            httpRequest.setHeader("Authorization", authorization);
        }

        long timeUsed = -1;
        HttpResponse response = null;
        try {
            long startTime = System.currentTimeMillis();
            response = httpClient.execute(httpRequest);
            long endTime = System.currentTimeMillis();
            timeUsed = endTime - startTime;
            LOG.debug("RESP " + httpRequest.getURI() + " (" + timeUsed + "ms)");

            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() != HttpStatus.SC_OK) {
                httpRequest.abort();
                throw createHttpException(statusLine.toString(), request);
            }
        } catch (IOException e) {
            LOG.info(httpRequest.getURI() + " -> " + e.getMessage(), e);
            httpRequest.abort();
            throw createHttpException(e, request);
        }
        return response;
    }

    public String execute(final MtHttpRequest request) {
        String respString;
        try {
            HttpResponse response = executeRaw(request);
            long start = System.currentTimeMillis();
            HttpEntity entity = response.getEntity();
            respString = EntityUtils.toString(entity);
            long end = System.currentTimeMillis();
            LOG.debug("EntityUtils.toString cost " + (end - start));
        } catch (IOException e) {
            throw createHttpException(e, request);
        }
        LOG.debug("response: " + respString);
        return respString;
    }

    public <T> MtHttpResponse<T> executeResponse(final MtHttpRequest request, Class<T> clazz) {
        String respString = execute(request);
        long start = System.currentTimeMillis();
        try {
            JSONObject nodes = JSON.parseObject(respString);
            ErrorCode error = nodes.getObject("error", ErrorCode.class);
            Paging paging = nodes.getObject("paging", Paging.class);
            if (error != null) {
                return MtHttpResponse.error(error);
            }
            T t = nodes.getObject("data", clazz);
            if (paging != null) {
                return MtHttpResponse.create(t, paging);
            }
            return MtHttpResponse.create(t);
        } catch (Exception e) {
            LOG.warn(e.getMessage(), e);
            throw createHttpException("Failed to parse response", e, request);
        } finally {
            long end = System.currentTimeMillis();
            LOG.debug("json to Object cost " + (end - start));
        }
    }

    public <T> MtHttpResponse<T> executeResponse(final MtHttpRequest request,
                                                 TypeReference<T> typeReference) {
        String respString = execute(request);
        long start = System.currentTimeMillis();
        try {
            JSONObject nodes = JSON.parseObject(respString);
            ErrorCode error = nodes.getObject("error", ErrorCode.class);
            Paging paging = nodes.getObject("paging", Paging.class);
            if (error != null) {
                return MtHttpResponse.error(error);
            }
            Object data = nodes.get("data");
            if (data == null) {
                return MtHttpResponse.error(new ErrorCode(500, "illegal response", "no data"));
            }
            T t;
            if (data instanceof JSONObject) {
                t = JSON.parseObject(((JSONObject) data).toJSONString(), typeReference);
            } else if (data instanceof JSONArray) {
                t = JSON.parseObject(((JSONArray) data).toJSONString(), typeReference);
            } else {
                t = nodes.getObject("data", (Class<T>) typeReference.getType());
            }
            if (paging != null) {
                return MtHttpResponse.create(t, paging);
            }
            return MtHttpResponse.create(t);
        } catch (Exception e) {
            LOG.info(e.getMessage(), e);
            throw createHttpException("Failed to parse response : " + respString, e, request);
        } finally {
            long end = System.currentTimeMillis();
            LOG.debug("json to Object cost " + (end - start));
        }
    }

    public <T> MtHttpResponse<T> executeResponse(final MtHttpRequest request, Type type) {
        String respString = execute(request);
        long start = System.currentTimeMillis();
        try {
            JSONObject nodes = JSON.parseObject(respString);
            ErrorCode error = nodes.getObject("error", ErrorCode.class);
            Paging paging = nodes.getObject("paging", Paging.class);
            if (error != null) {
                return MtHttpResponse.error(error);
            }
            Object data = nodes.get("data");
            if (data == null) {
                return MtHttpResponse.error(new ErrorCode(500, "illegal response", "no data"));
            }
            T t;
            if (data instanceof JSONObject) {
                t = JSON.parseObject(((JSONObject) data).toJSONString(), type);
            } else if (data instanceof JSONArray) {
                t = JSON.parseObject(((JSONArray) data).toJSONString(), type);
            } else {
                t = nodes.getObject("data", (Class<T>) type);
                // t = JSON.parseObject(data.toString(), type);
            }
            if (paging != null) {
                return MtHttpResponse.create(t, paging);
            }
            return MtHttpResponse.create(t);
        } catch (Exception e) {
            LOG.warn(e.getMessage(), e);
            throw createHttpException("Failed to parse response : " + respString, e, request);
        } finally {
            long end = System.currentTimeMillis();
            LOG.debug("json to Object cost " + (end - start));
        }
    }

    public <T> Response<T> execute(final MtHttpRequest request, Type type) {
        String respString = execute(request);
        long start = System.currentTimeMillis();
        try {
            return JSON.parseObject(respString, type);
        } catch (Exception e) {
            LOG.warn(e.getMessage(), e);
            throw createHttpException("Failed to parse response : " + respString, e, request);
        } finally {
            long end = System.currentTimeMillis();
            LOG.debug("json to Object cost " + (end - start));
        }
    }

    private int getConnectionTimeout(final MtHttpRequest request) {
        // since 1.1.4
        if (null != request.getConnectionTimeout()) {
            return request.getConnectionTimeout();
        }
        // compatible with 1.1.3 or before
        if (null != request.getTimeout()) {
            return request.getTimeout();
        }
        return HttpConfig.CONNECTION_TIMEOUT;
    }

    private int getSoTimeout(final MtHttpRequest request) {
        // since 1.1.4
        if (null != request.getSoTimeout()) {
            return request.getSoTimeout();
        }
        // compatible with 1.1.3 or before
        if (null != request.getTimeout()) {
            return request.getTimeout();
        }
        return HttpConfig.SO_TIMEOUT;
    }

    private int getRetry(final MtHttpRequest request) {
        if (null != request.getRetry()) {
            return request.getRetry();
        }
        return HttpConfig.RETRY_TIME;
    }

    private HttpException createHttpException(Exception e, MtHttpRequest request) {
        return this.createHttpException("remote service调用出错", e, request);
    }

    private HttpException createHttpException(String msg, MtHttpRequest request) {
        return new HttpException(MessageFormatter.format("{},请求为:{}", msg, request).getMessage());
    }

    private HttpException createHttpException(String message, Exception e, MtHttpRequest request) {
        return new HttpException(MessageFormatter.format("{},请求为:{}", message, request).getMessage(), e);
    }
}
