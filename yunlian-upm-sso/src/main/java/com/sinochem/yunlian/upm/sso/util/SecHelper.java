package com.sinochem.yunlian.upm.sso.util;

import com.sinochem.yunlian.upm.sso.model.MtCookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SecHelper {
    private static final Logger LOG = LoggerFactory.getLogger(SecHelper.class);

    public static void saveSec(HttpServletRequest request, HttpServletResponse response) {
        String username = (String) TraceContext.get("username");
        String ua = (String) TraceContext.get(Constants.CONTEXT_UA_KEY);
        String utc = SSOUtil.generate(username, ua, TimeUtil.unixtime());
        TraceContext.put(Constants.SEC_UTC_KEY, utc);
        MtCookie cookie = new MtCookie(Constants.SEC_UTC_KEY).value(utc);
        cookie.setMaxAge(Constants.SEC_UTC_MAX_AGE);
        cookie.setHttpOnly(false);
        // TODO: 2017/10/29 修改域名
        cookie.setDomain(".sinochem.yunlian.com");
        cookie.saveTo(request, response);
        LOG.info("saveSec action {} add new uuid cookie {} {} {}", new Object[]{request.getRequestURI(), username, ua, cookie.getValue()});
    }

    public static final String SSOSKT_AES_KEY = "UITHT5LMUQCT78JM";
    public static final String SSOSKT_SHA_KEY = "shduihf93jishf8t22oe1je0qw0hdr37";

    public static void createSsosktCookie(Model model){
        String utc = (String)TraceContext.get(Constants.SEC_UTC_KEY);
        if(StringUtil.isNotBlank(utc)){
            long now = System.currentTimeMillis();
            String aes = CryptAES.AES_Encrypt(SSOSKT_AES_KEY, utc + "|" + now);
            String hmacSha = HMACSHA1.getSignature(aes, SSOSKT_SHA_KEY);
            String ssoskt = aes + "-" + hmacSha;

            model.addAttribute("ssoskt", ssoskt) ;
        }
    }
}
