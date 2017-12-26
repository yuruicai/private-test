package com.sinochem.yunlian.upm.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @author zhangxi
 * @created 13-7-11
 */
public class UpmAuthService {
    private static final Logger LOG = LoggerFactory.getLogger(UpmAuthService.class);
    private String loginUrl;
    private String authUrl;
    private String clientId;
    private String secret;
    private Integer connectionTimeout;
    private Integer soTimeout;
    private Integer retry;
    private HttpClient httpClient = new HttpClient();

    public String getAuthUrl() {
        return authUrl;
    }

    public void setAuthUrl(String authUrl) {
        this.authUrl = authUrl;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Integer getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(Integer connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public Integer getSoTimeout() {
        return soTimeout;
    }

    public void setSoTimeout(Integer soTimeout) {
        this.soTimeout = soTimeout;
    }

    public Integer getRetry() {
        return retry;
    }

    public void setRetry(Integer retry) {
        this.retry = retry;
    }

    private final <T> MtHttpResponse<T> invoke(MtHttpRequest request, Type type) {
        if(getConnectionTimeout() != null && getConnectionTimeout() > 0){
            request.setConnectionTimeout(getConnectionTimeout());
        }
        if(getSoTimeout() != null && getSoTimeout() > 0){
            request.setSoTimeout(getSoTimeout());
        }
        if(getRetry() != null && getRetry() > 0){
            request.setRetry(getRetry());
        }
        return httpClient.executeResponse(request, type);
    }

    /**
     * 根据资源进行鉴权
     *
     * @param appkey
     * @param userId
     * @param resource
     * @return
     */
    public AuthResult authResource(String appkey, String userId, String resource) {
        URL url = newUrl("authResource").host(getAuthUrl()).path("/api/authResource").param("appkey", appkey)
                .param("userId", userId).param("resource", resource).method("GET").build();
        MtHttpResponse<AuthResult> response = invoke(MtHttpRequest.from(url), new TypeReference<AuthResult>(){}.getType());
        if (LOG.isDebugEnabled()) {
            LOG.debug("" + response);
        }
        return response.getData();
    }

    /**
     * 根据权限代码进行鉴权
     *
     * @param appkey
     * @param userId
     * @param perm
     * @return
     */
    public AuthResult authPerm(String appkey, String userId, String perm) {
        URL url = newUrl("authCode").host(getAuthUrl()).path("/api/authPerm").param("appkey", appkey)
                .param("userId", userId).param("perm", perm).method("GET").build();
        MtHttpResponse<AuthResult> response = invoke(MtHttpRequest.from(url), new TypeReference<AuthResult>() {}.getType());
        if (LOG.isDebugEnabled()) {
            LOG.debug("" + response);
        }
        return response.getData();
    }

    /**
     * 获取应用权限资源列表
     *
     * @param appkey
     * @return
     */
    public List<String> getUrls(String appkey) {
        URL url = newUrl("getUrls").host(getAuthUrl()).path("/api/urls").param("appkey", appkey)
                .method("GET").build();
        MtHttpResponse<List<String>> response = invoke(MtHttpRequest.from(url), new TypeReference<List<String>>() {
        }.getType());
        if (LOG.isDebugEnabled()) {
            LOG.debug("fetchUrls: " + response.getData());
        }
        return response.getData();
    }

    /**
     * 获取用户有权限的菜单
     *
     * @param appkey
     * @param userId
     * @return
     */
    public List<Menu> getMenus(String appkey, String userId) {
        URL url = newUrl("getMenus").host(getAuthUrl()).path("/api/menus").param("appkey", appkey)
                .param("userId", userId).method("GET").build();
        MtHttpResponse<List<Menu>> response = invoke(MtHttpRequest.from(url), new TypeReference<List<Menu>>() {
        }.getType());
        if (LOG.isDebugEnabled()) {
            LOG.debug("fetchMenu: " + response.getData());
        }
        return response.getData();
    }

    /**
     * 根据SID获取用户信息
     *
     * @param sid
     * @return
     */
    public User getUser(String sid) {
        URL url = newUrl("getUser").host(getLoginUrl()).path("/api/session").method("POST")
                .param("token", sid).build();
        MtHttpResponse<User> response = invoke(MtHttpRequest.from(url), new TypeReference<User>() {
        }.getType());
        if (LOG.isDebugEnabled()) {
            LOG.debug("" + response);
        }
        return response.getData();
    }

    public String getToken(String ticket){
        URL url = newUrl("getUser").host(getLoginUrl()).path("/api/serviceValidate").method("POST")
                .param("ticket", ticket).build();
        MtHttpResponse<String> response = invoke(MtHttpRequest.from(url), new TypeReference<String>() {
        }.getType());
        if (LOG.isDebugEnabled()) {
            LOG.debug("" + response);
        }
        return response.getData();
    }

    /**
     * 用户登出接口
     *
     * @param token
     * @return
     */
    public boolean logout(String token) {
        URL url = newUrl("logout").host(getLoginUrl()).path("/api/logout").param("token", token).method("POST").build();
        MtHttpResponse<String> response = invoke(MtHttpRequest.from(url), String.class);
        if (LOG.isDebugEnabled()) {
            LOG.debug("" + response);
        }
        return response.getData().equalsIgnoreCase("ok");
    }

    /**
     * 用户登出接口
     *
     * @param uid
     * @return
     */
    public boolean logoutAll(String uid) {
        URL url = newUrl("logout").host(getLoginUrl()).path("/api/logoutAll").param("uid", uid).method("POST").build();
        MtHttpResponse<String> response = invoke(MtHttpRequest.from(url), String.class);
        if (LOG.isDebugEnabled()) {
            LOG.debug("" + response);
        }
        return response.getData().equalsIgnoreCase("ok");
    }

    /**
     * 获取用户应用内角色信息
     *
     * @param appkey
     * @param userId
     * @return
     */
    public List<String> getRoles(String appkey, String userId) {
        StringBuilder sb = new StringBuilder("/api/roles/");
        sb.append(userId);
        sb.append("/");
        sb.append(appkey);
        URL url = newUrl("getRoles").host(getAuthUrl()).path(sb.toString()).method("GET").build();
        MtHttpResponse<List<String>> response = invoke(MtHttpRequest.from(url), new TypeReference<List<String>>() {
        }.getType());
        if (LOG.isDebugEnabled()) {
            LOG.debug("fetchRoles: " + response.getData());
        }
        return response.getData();
    }

    /**
     * 获取用户应用权限标识列表
     *
     * @param userId
     * @return
     */
    public List<String> getPermissions(String appkey, String userId) {
        StringBuilder sb = new StringBuilder("/api/permissions/");
        sb.append(userId);
        sb.append("/");
        sb.append(appkey);
        URL url = newUrl("getPermissions").host(getAuthUrl()).path(sb.toString()).method("GET").build();
        MtHttpResponse<List<String>> response = invoke(MtHttpRequest.from(url), new TypeReference<List<String>>(){}.getType());
        if (LOG.isDebugEnabled()) {
            LOG.debug("getPermissions: " + response.getData());
        }
        return response.getData();
    }

    /**
     * 获取用户可以访问的应用
     *
     * @param userId
     * @return
     */
    public List<Application> getUserApps(String userId) {
        URL url = newUrl("getUserApps").host(getAuthUrl()).path("/api/userApps").method("GET")
                .param("userId", userId).build();
        MtHttpResponse<List<Application>> response = invoke(MtHttpRequest.from(url), new TypeReference<List<Application>>() {
        }.getType());
        if (LOG.isDebugEnabled()) {
            LOG.debug("" + response);
        }
        return response.getData();
    }

    private URL.Builder newUrl(String name) {
        URL.Builder builder = URL.builder().header("key", getClientId()).header("secret", getSecret());
        if (name != null) {
            builder.header("spanName", "UpmAuthService." + name);
        }
        return builder;
    }

    /**
     * 根据用户ID获取用户信息
     *
     * @param userId
     * @return
     */
    public User getUserById(String userId) {
        URL url = newUrl("getUserById").host(getAuthUrl()).path("/api/user/getUserById").method("GET")
                .param("id", userId).build();
        MtHttpResponse<User> response = invoke(MtHttpRequest.from(url), new TypeReference<User>() {
        }.getType());
        if (LOG.isDebugEnabled()) {
            LOG.debug("" + response);
        }
        return response.getData();
    }

    /**
     * 根据用户登录名获取用户信息
     *
     * @param loginName
     * @return
     */
    public User getUserByLoginName(String loginName) {
        URL url = newUrl("getUserByLoginName").host(getAuthUrl()).path("/api/user/getUserByLoginName").method("GET")
                .param("loginName", loginName).build();
        MtHttpResponse<User> response = invoke(MtHttpRequest.from(url), new TypeReference<User>() {
        }.getType());
        if (LOG.isDebugEnabled()) {
            LOG.debug("" + response);
        }
        return response.getData();
    }

    /**
     * 获取所有用户信息
     *
     * @param type 1 员工，2 企业用户
     * @param lastUpdateTime
     * @return
     */
    public List<User> getAllUser(Integer type, Long lastUpdateTime) {
        URL.Builder builder = newUrl("getAllUser").host(getAuthUrl()).path("/api/user/getAllUser").method("GET");
        if(type != null){
            builder.param("type", type);
        }
        if(lastUpdateTime != null){
            builder.param("lastUpdateTime", lastUpdateTime);
        }
        URL url = builder.build();
        MtHttpResponse<List<User>> response = invoke(MtHttpRequest.from(url), new TypeReference<List<User>>() {}.getType());
        if (LOG.isDebugEnabled()) {
            LOG.debug("" + response);
        }
        return response.getData();
    }

}
