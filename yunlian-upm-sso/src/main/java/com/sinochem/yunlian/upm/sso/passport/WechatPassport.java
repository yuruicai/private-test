package com.sinochem.yunlian.upm.sso.passport;

import com.alibaba.fastjson.JSON;
import com.sinochem.yunlian.upm.common.util.HttpUtils;
import com.sinochem.yunlian.upm.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author huangyang
 * @Description: 微信方登陆接口，后续优化对第三方登陆做成统一接口
 * @date 2018/01/12 下午8:06
 */
@Slf4j
@Service
public class WechatPassport {

    private static final String USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=%sN&openid=%s";

    @Value("${wechat.appId}")
    private String appId;

    @Value("${wechat.appSecret}")
    private String appSecret;

    @Value("${wechat.access.url}")
    private String accessUrl;

    @Value("${wechat.callback.url}")
    private String callbackUrl;

    @Value("${wechat.connect.url}")
    private String connectUrl;

    public String getConnectUrl() {
        String state = StringUtil.getUuid();
        return String.format(connectUrl, state);
    }

    /**
     * 返回token
     *
     * @param params
     * @return {
     * "access_token":"ACCESS_TOKEN",
     * "expires_in":7200,
     * "refresh_token":"REFRESH_TOKEN",
     * "openid":"OPENID",
     * "scope":"SCOPE",
     * "unionid": "o6_bmasdasdsad6_2sgVt7hMZOPfL"
     * }  or {"errcode":40029,"errmsg":"invalid code"}
     */
    public Map<String, String> handleCallback(Map<String, String> params) {
        String code = params.get("code");
        String finalConnectUrl = String.format(accessUrl, code);
        String body = HttpUtils.get(finalConnectUrl, null);
        if (!StringUtils.isEmpty(body)) {
            log.info("请求微信登陆CODE参数: " + body);
            return JSON.parseObject(body, Map.class);
        }
        return Collections.EMPTY_MAP;
    }

    /**
     * 请求用户信息
     * @param params
     * @return
     */
    public Map<String, String> getUserInfo(Map<String, String> params) {
        String accessToken = params.get("accessToken");
        String openId = params.get("accessToken");
        String userInfoUrl = String.format(USER_INFO_URL, accessToken, openId);
        String response = HttpUtils.get(userInfoUrl, null);
        if (StringUtils.isEmpty(response)) {
            log.info("获取微信用户信息：" + response);
            Map map = JSON.parseObject(response, Map.class);
            String nickName = map.get("nickname").toString();
            //1为男性，2为女性
            String sex = map.get("nickname").toString();
            //用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空
            String headImgUrl = map.get("headimgurl").toString();
            Map<String, String> userInfo = new HashMap<>(4);
            userInfo.put("userName", nickName);
            userInfo.put("sex", "1".equals(sex) ? "男" : "女");
            userInfo.put("headImgUrl", headImgUrl);
            return userInfo;
        }
        return Collections.EMPTY_MAP;
    }

}
