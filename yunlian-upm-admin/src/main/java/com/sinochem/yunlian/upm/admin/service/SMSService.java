package com.sinochem.yunlian.upm.admin.service;

import com.alibaba.fastjson.JSON;
import com.sinochem.yunlian.upm.admin.constant.SMSStatus;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhanghongze on 2015/11/20.
 */
public class SMSService {

    private static final Logger LOG = LoggerFactory.getLogger(SMSService.class);

    public static final String CONTENT_SPLIT_MARK = "&";
    public static final String STATUS_TEXT = "statusText";

    private String smsUrl;

    public String getSmsUrl() {
        return smsUrl;
    }

    public void setSmsUrl(String smsUrl) {
        this.smsUrl = smsUrl;
    }

    public SMSStatus sendSMS(String mobile, String routeType, String smsType, String ext, String... contents) {
        LOG.debug("Sending message. [mobile={}][content={}]", mobile, contents);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("mobileNum", mobile));
        params.add(new BasicNameValuePair("smsType", smsType));
        params.add(new BasicNameValuePair("content", encapsulateParam(contents)));
        params.add(new BasicNameValuePair("ruteEnum", routeType));
        if (StringUtils.isNotBlank(ext)) {
            params.add(new BasicNameValuePair("ext", ext));
        }

        SMSStatus smsStatus = postRequest(smsUrl + "/sms/send", params);

        return smsStatus;
    }

    private SMSStatus postRequest(String url, List<NameValuePair> params) {

        SMSStatus smsStatus = null;
        HttpClient httpClient = null;
        try {
            httpClient = new DefaultHttpClient();
            HttpPost post = new HttpPost(url);
            post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            HttpResponse resp = httpClient.execute(post);

            //记录短信平台返回
            HttpEntity httpEntity = resp.getEntity();
            String result = EntityUtils.toString(httpEntity);
            LOG.info("Result from invoking SMSService. [responseCode={}][response={}]",
                    resp.getStatusLine().getStatusCode(),
                    result);

            if (StringUtils.isNotBlank(result)) {
                Map<String, Object> map = JSON.parseObject(result, HashMap.class);
                smsStatus = convertSMSStatus((String)map.get(STATUS_TEXT));
            }

        } catch (Exception ex) {
            LOG.error("Exception when invoked SMSService", ex);
        } finally {
            if (httpClient != null) {
                try {
                    httpClient.getConnectionManager().shutdown();
                } catch (Exception e) {
                    LOG.error("Exception when shurdown ConnectionManager", e);
                }
            }
            return smsStatus;
        }
    }

    private String encapsulateParam(String... contents) {
        if (contents == null) {
            return StringUtils.EMPTY;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < contents.length; i++) {
            if (contents[i] == null) {
                continue;
            }
            if (i == (contents.length - 1)) {
                builder.append(contents[i]);
            }else {
                builder.append(contents[i]).append(CONTENT_SPLIT_MARK);//&
            }

        }
        return builder.toString();
    }

    private SMSStatus convertSMSStatus(String statusText) {
        try {
            SMSStatus smsStatus = SMSStatus.getSMSStatus(statusText);
            return smsStatus;
        } catch (Exception e) {
            LOG.info("Exception when convertSMSStatus", e);
        }
        return null;
    }

    public static void main(String [] args){
        SMSService smsService = new SMSService();
        smsService.setSmsUrl("http://10.255.0.167:8097");
        SMSStatus smsStatus = smsService.sendSMS("18910270829", "2", "creditmanager.reset.password", null, "壹化云链", "<pwd" + 1234 + "pwd>");
       // SMSStatus smsStatus = smsService.sendSMS("18910270829", "2", "notification.creditmanager.employee.created", null, "壹化云链", "zhanghongze", "<pwd" + 1234 + "pwd>");

        System.out.println(smsStatus.getKey());
    }

}
