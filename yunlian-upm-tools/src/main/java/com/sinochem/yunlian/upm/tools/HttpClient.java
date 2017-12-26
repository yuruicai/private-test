package com.sinochem.yunlian.upm.tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.apache.http.*;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.SyncBasicHttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

/**
 * @author zhangxi
 * @created 13-1-17
 */
class HttpClient {
    private static final Logger LOG = LoggerFactory.getLogger(HttpClient.class);

    private static final int CONNECTION_TIMEOUT_DEFAULT = 10000;
    private static final int SO_TIMEOUT_DEFAULT = 10000;
    private static final int RETRY_DEFAULT = 3;

    public HttpResponse executeRaw(final MtHttpRequest request) {
        final String url = request.getHost() + request.getPath();
        final String method = request.getMethod();
        URL u;
        try {
            u = new URL(url);
        } catch (MalformedURLException e) {
            LOG.error(e.getMessage(), e);
            throw new HttpException(e.getMessage(), e);
        }
        String uri = u.getPath();
        HttpRequestBase httpRequest = null;
        if ("GET".equals(method)) {
            httpRequest = new HttpGet(u.toString());
        } else if ("POST".equals(method)) {
            httpRequest = new HttpPost(u.toString());
        } else if ("PUT".equals(method)) {
            httpRequest = new HttpPut(u.toString());
        } else if ("DELETE".equals(method)) {
            httpRequest = new HttpDelete(u.toString());
        }
        HttpParams defaultParams = new SyncBasicHttpParams();
        DefaultHttpClient.setDefaultHttpParams(defaultParams);
        DefaultHttpClient httpClient = new DefaultHttpClient(defaultParams);

        int connectTimeout = getConnectionTimeout(request);
        int soTimeout = getSoTimeout(request);
        final int retry = getRetry(request);

        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, connectTimeout);
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, soTimeout);
        HttpRequestRetryHandler myRetryHandler = new HttpRequestRetryHandler() {
            public boolean retryRequest(IOException exception, int executionCount,
                                        HttpContext context) {
                if (executionCount > retry) {
                    return false;
                } else {
                    LOG.warn("retry " + executionCount + " " + url + " -> "
                            + exception.getMessage());
                }
                return true;
            }
        };
        httpClient.setHttpRequestRetryHandler(myRetryHandler);
        Map<String, Object> params = request.getParams();
        if (params != null && params.size() > 0) {
            if ("GET".equals(method)) {
                List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                for (Iterator<String> it = params.keySet().iterator(); it.hasNext(); ) {
                    String key = it.next();
                    if (params.get(key) != null) {
                        String value = (params.get(key)).toString();
                        nvps.add(new BasicNameValuePair(key, value));
                    }
                }
                HttpEntity httpEntity;
                try {
                    httpEntity = new UrlEncodedFormEntity(nvps, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    LOG.error(e.getMessage(), e);
                    throw new HttpException(e.getMessage(), e);
                }
                String paramString;
                try {
                    paramString = EntityUtils.toString(httpEntity);
                } catch (IOException e) {
                    LOG.error(e.getMessage(), e);
                    throw new HttpException(e.getMessage(), e);
                }
                String tmpUrl = httpRequest.getURI().toString();
                if (tmpUrl.contains("?")) {
                    tmpUrl = tmpUrl + "&" + paramString;
                } else {
                    tmpUrl = tmpUrl + "?" + paramString;
                }
                try {
                    httpRequest.setURI(new URI(tmpUrl));
                } catch (URISyntaxException e) {
                    LOG.error(e.getMessage(), e);
                    throw new HttpException(e.getMessage(), e);
                }
                LOG.debug(method + " " + httpRequest.getURI());
            } else if ("POST".equals(method) || "PUT".equals(method) || "DELETE".equals(method)) {
                String jsonString;
                HttpEntity httpEntity;
                try {
                    jsonString = JSON.toJSONString(params);
                    httpEntity = new StringEntity(jsonString, "application/json", "UTF-8");
                } catch (Exception e) {
                    LOG.error(e.getMessage(), e);
                    throw new HttpException(e.getMessage(), e);
                }
                if ("POST".equals(method)) {
                    HttpPost httpPost = (HttpPost) httpRequest;
                    httpPost.setEntity(httpEntity);
                } else if ("PUT".equals(method)) {
                    HttpPut httpPut = (HttpPut) httpRequest;
                    httpPut.setEntity(httpEntity);
                }
                if (LOG.isDebugEnabled()) {
                    LOG.debug(method + " " + httpRequest.getURI());
                    LOG.debug("params: " + jsonString);
                }
            }
        } else {
            LOG.debug(method + " " + httpRequest.getURI());
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
            if (LOG.isDebugEnabled()) {
                LOG.debug("RESP " + httpRequest.getURI() + " (" + timeUsed + "ms)");
            }
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() != HttpStatus.SC_OK) {
                httpRequest.abort();
                throw new HttpException(statusLine.toString());
            }
        } catch (IOException e) {
            LOG.error(httpRequest.getURI() + " -> " + e.getMessage(), e);
            httpRequest.abort();
            throw new HttpException(e.getMessage(), e);
        } finally {
            // TODO httpClient release & reuse
            // if (!apiResponse.isBinary()) {
            // httpClient.getConnectionManager().shutdown();
            // }
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
            // LOG.error(httpRequest.getURI() + " -> " + e.getMessage(), e);
            // httpRequest.abort();
            throw new HttpException(e.getMessage(), e);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("response: " + respString);
        }
        return respString;
    }

    public <T> MtHttpResponse<T> executeResponse(final MtHttpRequest request, Class<T> clazz) {
        String respString = execute(request);
        long start = System.currentTimeMillis();
        try {
            JSONObject nodes = JSON.parseObject(respString);
            // System.out.println(respString);
            ErrorCode error = nodes.getObject("error", ErrorCode.class);
            // System.out.println(error);
            Paging paging = nodes.getObject("paging", Paging.class);
            // System.out.println(paging.getNext() + "," + paging.getPrevious() + "," + paging.getTotalCount());
            if (error != null) {
                return MtHttpResponse.error(error);
            }
            T t = nodes.getObject("data", clazz);
            if (paging != null) {
                return MtHttpResponse.create(t, paging);
            }
            MtHttpResponse<T> response = MtHttpResponse.create(t);
            return response;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new HttpException("Failed to parse response", e);
        } finally {
            long end = System.currentTimeMillis();
            LOG.debug("json to Object cost " + (end - start));
        }
    }

    public <T> MtHttpResponse<T> executeResponse(final MtHttpRequest request, TypeReference<T> typeReference) {
        String respString = execute(request);
        long start = System.currentTimeMillis();
        try {
            JSONObject nodes = JSON.parseObject(respString);
            ErrorCode error = nodes.getObject("error", ErrorCode.class);
            // System.out.println(error);
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
            MtHttpResponse<T> response = MtHttpResponse.create(t);
            return response;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new HttpException("Failed to parse response : " + respString, e);
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
            // System.out.println(error);
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
            LOG.error(e.getMessage(), e);
            throw new HttpException("Failed to parse response : " + respString, e);
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
            LOG.error(e.getMessage(), e);
            throw new HttpException("Failed to parse response : " + respString, e);
        } finally {
            long end = System.currentTimeMillis();
            LOG.debug("json to Object cost " + (end - start));
        }
    }

    /**
     * since 1.1.9
     * @param request
     * @return
     */
    private int getConnectionTimeout(final MtHttpRequest request) {

        if (null != request.getConnectionTimeout()) {
            return request.getConnectionTimeout();
        }

        return CONNECTION_TIMEOUT_DEFAULT;
    }

    /**
     * since 1.1.9
     * @param request
     * @return
     */
    private int getSoTimeout(final MtHttpRequest request) {
        // since 1.1.4
        if (null != request.getSoTimeout()) {
            return request.getSoTimeout();
        }

        return SO_TIMEOUT_DEFAULT;
    }

    /**
     * since 1.1.9
     * @param request
     * @return
     */
    private int getRetry(final MtHttpRequest request) {
        if (null != request.getRetry()) {
            return request.getRetry();
        }
        return RETRY_DEFAULT;
    }
}
