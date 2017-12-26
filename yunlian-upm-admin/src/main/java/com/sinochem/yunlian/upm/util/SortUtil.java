package com.sinochem.yunlian.upm.util;

import org.apache.commons.lang.StringUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * TODO 在这里编写类的功能描述
 * 
 * @author wangwei
 * @version 1.0
 * @created 2013-08-21
 */
public class SortUtil {

    public static void sortUrlList(List<String> urls) {
        Collections.sort(urls, new Comparator<String>() {
            public int compare(String url1, String url2) {
                if (!url1.contains("*") && !url2.contains("*")) {
                    return (StringUtils.countMatches(url2, "/") * 1000 + url2.length())
                            - (StringUtils.countMatches(url1, "/") * 1000 + url1.length());
                } else if (url2.contains("*") || url1.contains("*")) {
                    return (StringUtils.countMatches(url2, "*") * -10000
                            + StringUtils.countMatches(url2, "/") * 1000 + url2.length())
                            - (StringUtils.countMatches(url1, "*") * -10000
                            + StringUtils.countMatches(url1, "/") * 1000 + url1.length());
                }
                return 0;
            }
        });
    }

}
