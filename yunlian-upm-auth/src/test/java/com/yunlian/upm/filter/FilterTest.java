package com.yunlian.upm.filter;

import com.sinochem.yunlian.upm.filter.matcher.AntPathMatcher;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhangxi
 * @created 13-7-2
 */
public class FilterTest {

    @Test
    public void test() throws UnsupportedEncodingException {
        System.out.println(URLEncoder.encode("http://localhost:8080/mt-sso%3Fa=2&b=3", "UTF-8"));
        String url = "http%3A%2F%2F192.168.80.106%3A9999%2Findex.jsp%3Fa%3D1%26b%3D2%26c%3D3%26time%3D1374562448%26sign%3D547fdcf3064b0ef05138d9b7b2748b94";
        System.out.println(URLDecoder.decode(url, "UTF-8"));
        String authIps = "127.0.0.1,192.168.*,10.64.*,10.*";
        String splitor = "[\\s,;，；]+";
        for (String ip : authIps.split(splitor)) {
            System.out.println(ip);
        }
    }

    @Test
    public void testAnt() {
        AntPathMatcher matcher = new AntPathMatcher();
        System.out.println(matcher.matches("/aaa/**", "/234234/aaa/xxx"));
        System.out.println(matcher.matches("/aaa/bb", "/234234/aaa/bb"));
        System.out.println(matcher.matches("/aaa/bb", "/aaa/bb/wwww"));
        System.out.println(matcher.matches("/aaa/bb", "/aaa/bbwwww"));
        System.out.println(matcher.matches("/aaa/bb?", "/aaa/bbwwww"));
        System.out.println(matcher.matches("/aaa/bb?", "/aaa/bbw"));
        System.out.println(matcher.matches("/aaa/bb?", "/aaa/bb"));
        System.out.println(matcher.matches("/aaa/bb*", "/aaa/bb"));
        System.out.println(matcher.matches("/aaa/bb/*", "/aaa/bb/wwww"));
        System.out.println(matcher.matches("/aaa?/bb", "/aaa/bb"));
        System.out.println(matcher.matches("/aaa?/bb", "/aaax/bb"));
        System.out.println(matcher.matches("/aaa*/bb", "/aaaxxxx/bb"));
    }

    @Test
    public void testRegex() {
        String s = "/static/** = anon\n/** = user\n";
        System.out.println(s);
        String regx = "(.*) = (.*)\n";
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(s);
        while (matcher.find()) {
            System.out.println("find: " + matcher.group(1));
            System.out.println("find: " + matcher.group(2));
        }
    }
}
