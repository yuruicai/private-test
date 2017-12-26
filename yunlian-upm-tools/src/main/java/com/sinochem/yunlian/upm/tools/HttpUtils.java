package com.sinochem.yunlian.upm.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author zhangxi
 * @created 13-5-27
 */
public class HttpUtils {
    private static final Logger LOG = LoggerFactory.getLogger(HttpUtils.class);
    public static final String DEFAULT_SESSION_ID_NAME = "skmtusid";


    public static Cookie getCookie(HttpServletRequest request, String cookieName) {
        Cookie cookies[] = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equalsIgnoreCase(cookieName)) {
                    return cookie;
                }
            }
        }
        return null;
    }

    public static String getSid(HttpServletRequest request) {
        Cookie cookie = getCookie(request, DEFAULT_SESSION_ID_NAME);
        return cookie == null ? null : cookie.getValue();
    }

    public static String getRedirectService(HttpServletRequest request) {
        String service = request.getParameter("return");
        if (service == null) {
            service = request.getParameter("service");
        }
        return service == null ? "" : service;
    }

    public static boolean isValidSid(String sid) {
        return sid != null && !sid.trim().isEmpty() && sid.trim().length() > 16;
    }

    public static String generateSid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static void removeSid(HttpServletRequest request, HttpServletResponse response) {
        removeCookie(DEFAULT_SESSION_ID_NAME, request, response);
    }

    public static final String DELETED_COOKIE_VALUE = "deleteMe";

    public static void removeCookie(String name, HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = getCookie(request, name);
        if (cookie != null) {
            String value = DELETED_COOKIE_VALUE;
            String comment = null;
            String domain = cookie.getDomain();
            String path = calculatePath(request, cookie);
            int maxAge = 0;
            int version = cookie.getVersion();
            boolean secure = cookie.getSecure();
            boolean httpOnly = false;
            addCookieHeader(response, name, value, comment, domain, path, maxAge, version, secure, httpOnly);
        }
    }

    public static final String ROOT_PATH = "/";

    public static String calculatePath(HttpServletRequest request, Cookie cookie) {
        String path = StringUtils.clean(cookie.getPath());
        if (!StringUtils.hasText(path)) {
            path = StringUtils.clean(request.getContextPath());
        }
        if (path == null) {
            path = ROOT_PATH;
        }
        return path;

    }

    public static final int DEFAULT_MAX_AGE = -1;

    public static final int DEFAULT_VERSION = -1;
    public static final String NAME_VALUE_DELIMITER = "=";
    public static final String ATTRIBUTE_DELIMITER = "; ";
    public static final long DAY_MILLIS = 86400000; //1 day = 86,400,000 milliseconds
    public static final String GMT_TIME_ZONE_ID = "GMT";
    public static final String COOKIE_DATE_FORMAT_STRING = "EEE, dd-MMM-yyyy HH:mm:ss z";
    public static final String COOKIE_HEADER_NAME = "Set-Cookie";
    public static final String PATH_ATTRIBUTE_NAME = "Path";
    public static final String EXPIRES_ATTRIBUTE_NAME = "Expires";
    public static final String MAXAGE_ATTRIBUTE_NAME = "Max-Age";
    public static final String DOMAIN_ATTRIBUTE_NAME = "Domain";
    public static final String VERSION_ATTRIBUTE_NAME = "Version";
    public static final String COMMENT_ATTRIBUTE_NAME = "Comment";
    public static final String SECURE_ATTRIBUTE_NAME = "Secure";
    public static final String HTTP_ONLY_ATTRIBUTE_NAME = "HttpOnly";

    public static void addCookieHeader(HttpServletResponse response, String name, String value, String comment,
                                       String domain, String path, int maxAge, int version,
                                       boolean secure, boolean httpOnly) {

        String headerValue = buildHeaderValue(name, value, comment, domain, path, maxAge, version, secure, httpOnly);
        response.addHeader(COOKIE_HEADER_NAME, headerValue);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Added HttpServletResponse Cookie [{}]", headerValue);
        }
    }


    public static String buildHeaderValue(String name, String value, String comment,
                                          String domain, String path, int maxAge, int version,
                                          boolean secure, boolean httpOnly) {

        if (!StringUtils.hasText(name)) {
            throw new IllegalStateException("Cookie name cannot be null/empty.");
        }

        StringBuilder sb = new StringBuilder(name).append(NAME_VALUE_DELIMITER);

        if (StringUtils.hasText(value)) {
            sb.append(value);
        }
        appendComment(sb, comment);
        appendDomain(sb, domain);
        appendPath(sb, path);
        appendExpires(sb, maxAge);
        appendVersion(sb, version);
        appendSecure(sb, secure);
        appendHttpOnly(sb, httpOnly);
        return sb.toString();

    }

    public static void appendComment(StringBuilder sb, String comment) {
        if (StringUtils.hasText(comment)) {
            sb.append(ATTRIBUTE_DELIMITER);
            sb.append(COMMENT_ATTRIBUTE_NAME).append(NAME_VALUE_DELIMITER).append(comment);
        }
    }

    public static void appendDomain(StringBuilder sb, String domain) {
        if (StringUtils.hasText(domain)) {
            sb.append(ATTRIBUTE_DELIMITER);
            sb.append(DOMAIN_ATTRIBUTE_NAME).append(NAME_VALUE_DELIMITER).append(domain);
        }
    }

    public static void appendPath(StringBuilder sb, String path) {
        if (StringUtils.hasText(path)) {
            sb.append(ATTRIBUTE_DELIMITER);
            sb.append(PATH_ATTRIBUTE_NAME).append(NAME_VALUE_DELIMITER).append(path);
        }
    }

    public static void appendExpires(StringBuilder sb, int maxAge) {
        // if maxAge is negative, cookie should should expire when browser closes
        // Don"t write the maxAge cookie value if it"s negative - at least on Firefox it"ll cause the
        // cookie to be deleted immediately
        // Write the expires header used by older browsers, but may be unnecessary
        // and it is not by the spec, see http://www.faqs.org/rfcs/rfc2965.html
        // TODO consider completely removing the following
        if (maxAge >= 0) {
            sb.append(ATTRIBUTE_DELIMITER);
            sb.append(MAXAGE_ATTRIBUTE_NAME).append(NAME_VALUE_DELIMITER).append(maxAge);
            sb.append(ATTRIBUTE_DELIMITER);
            Date expires;
            if (maxAge == 0) {
                //delete the cookie by specifying a time in the past (1 day ago):
                expires = new Date(System.currentTimeMillis() - DAY_MILLIS);
            } else {
                //Value is in seconds.  So take "now" and add that many seconds, and that"s our expiration date:
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.SECOND, maxAge);
                expires = cal.getTime();
            }
            String formatted = toCookieDate(expires);
            sb.append(EXPIRES_ATTRIBUTE_NAME).append(NAME_VALUE_DELIMITER).append(formatted);
        }
    }

    public static void appendVersion(StringBuilder sb, int version) {
        if (version > DEFAULT_VERSION) {
            sb.append(ATTRIBUTE_DELIMITER);
            sb.append(VERSION_ATTRIBUTE_NAME).append(NAME_VALUE_DELIMITER).append(version);
        }
    }

    public static void appendSecure(StringBuilder sb, boolean secure) {
        if (secure) {
            sb.append(ATTRIBUTE_DELIMITER);
            sb.append(SECURE_ATTRIBUTE_NAME); //No value for this attribute
        }
    }

    public static void appendHttpOnly(StringBuilder sb, boolean httpOnly) {
        if (httpOnly) {
            sb.append(ATTRIBUTE_DELIMITER);
            sb.append(HTTP_ONLY_ATTRIBUTE_NAME); //No value for this attribute
        }
    }

    public static String toCookieDate(Date date) {
        TimeZone tz = TimeZone.getTimeZone(GMT_TIME_ZONE_ID);
        DateFormat fmt = new SimpleDateFormat(COOKIE_DATE_FORMAT_STRING, Locale.US);
        fmt.setTimeZone(tz);
        return fmt.format(date);
    }


    public static String getHost(HttpServletRequest request) {
        String host = request.getHeader("Host");
        return host;
    }

    public static String getRequestUrl(HttpServletRequest request) throws UnsupportedEncodingException {
        StringBuilder requestUrl = new StringBuilder(request.getRequestURI());
        if (request.getQueryString() != null) {
            requestUrl.append("?").append(request.getQueryString());
        }
        return requestUrl.toString();
    }

    public static String getEntry(HttpServletRequest request) throws UnsupportedEncodingException {
        String entry = request.getRequestURL().toString();
        String query = request.getQueryString();
        if (query != null) {
            entry += "?" + query;
        }
        entry = entry.substring(entry.indexOf("//") + 2);
        String host = HttpUtils.getHost(request);
        if (entry.indexOf("/") != -1) {
            entry = "http://" + host + entry.substring(entry.indexOf("/"));
        } else {
            entry = "http://" + host;
        }
        return URLEncoder.encode(entry, "UTF-8");
    }
}
