package com.sinochem.yunlian.upm.sso.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author huangyang
 * @Description: 第三方登陆API
 * @date 2018/01/12 下午5:34
 */
@Slf4j
@RestController
@RequestMapping("thirdparty/login")
public class ThirdPartyLoginController {


    /**
     * 用于用户确认微信登陆后回调
     * @param code
     * @param state
     * @return
     */
    @RequestMapping("wechat/callback")
    public Map<String,String> wechatCallback(String code ,String state ){
        Map<String,String> response = new HashMap<>();
        if(StringUtils.isEmpty(code)){
            log.info("用户拒绝微信登陆");
            //返回数据
        }
        //todo state判断是否非法请求

        return response;



    }

}
