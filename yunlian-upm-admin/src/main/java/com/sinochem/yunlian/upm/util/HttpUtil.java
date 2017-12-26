package com.sinochem.yunlian.upm.util;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 使用httpclient发送请求工具类
 * 
 */
public class HttpUtil {

    private static final Logger log = LoggerFactory.getLogger(HttpUtil.class);

    private static DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z",
            Locale.ENGLISH);

    /**
     * 通过http post发送请求
     */
    public static final HttpResponse postString(String surl, List<NameValuePair> params,
            String clientId, String secret) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost post = new HttpPost(surl);
        try {
            Date date = new Date();
            URL u;

            try {
                u = new URL(surl);
            } catch (MalformedURLException e) {
                log.error(e.getMessage(), e);
                throw new RuntimeException(e.getMessage(), e);
            }
            // 设置Authorization
            if (clientId != null) {
                String uri = u.getPath();
                String dateString = getDateString(date);
                String authorization = getAuthorization(uri, "POST", date, clientId, secret);

                post.setHeader("Date", dateString);
                post.setHeader("Authorization", authorization);
            }
            if (params != null && params.size() > 0) {
                UrlEncodedFormEntity uefe = new UrlEncodedFormEntity(params, "UTF-8");
                post.setEntity(uefe);
                log.info("POST " + post.getURI());
                // 不打印明文密码
//                log.info("params: " + params);
            }
            HttpResponse response = httpclient.execute(post);
            return response;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
    }

    public static String getDateString(Date date) {
        df.setTimeZone(TimeZone.getTimeZone("GMT"));

        String dateString = df.format(date);

        return dateString;
    }

    public static Date toDate(String date) throws ParseException {
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        return df.parse(date);
    }

    public static String getAuthorization(String uri, String method, Date date, String clientId,
            String secret) {
        String stringToSign = method + " " + uri + "\n" + getDateString(date);

        String signature = HMACSHA1.getSignature(stringToSign, secret);

        String authorization = "MWS" + " " + clientId + ":" + signature;

        return authorization;
    }

    public static boolean isAjax(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }
}
