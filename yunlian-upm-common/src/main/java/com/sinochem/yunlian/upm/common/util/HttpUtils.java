package com.sinochem.yunlian.upm.common.util;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author huangyang
 * @Description:
 * @date 2018/01/13 上午11:27
 */

public class HttpUtils {

    private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);

    public static final String get(String url, Map<String, String> paramMap) {
        HttpGet get = new HttpGet(url);
        if (paramMap != null) {
            paramMap.forEach((k, v) -> {
                get.getParams().setParameter(k, v);
            });
        }
        HttpClient client = new DefaultHttpClient();
        //设置连接超时时间 和 读取超时时间。
        client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);

        HttpResponse response;
        try {
            response = client.execute(get);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                String body = EntityUtils.toString(response.getEntity(), "utf-8");
                log.debug("http response:" + body);
                return body;
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return "";
    }
}
