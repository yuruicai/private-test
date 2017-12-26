package com.sinochem.yunlian.upm.sso.service;

import com.alibaba.fastjson.JSON;
import com.sinochem.yunlian.upm.sso.cache.SsoCacheFacade;
import com.sinochem.yunlian.upm.sso.domain.AclUser;
import com.sinochem.yunlian.upm.sso.model.Session;
import com.sinochem.yunlian.upm.sso.model.User;
import com.sinochem.yunlian.upm.sso.model.UserForSeesion;
import com.sinochem.yunlian.upm.sso.model.UserSession;
import com.sinochem.yunlian.upm.sso.util.Constants;
import com.sinochem.yunlian.upm.sso.util.Status;
import com.sinochem.yunlian.upm.sso.util.TraceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class SessionService {
    private static final Logger LOG = LoggerFactory.getLogger(SessionService.class);

    private static final String USER_KEY_PREFIX = "sso.user.";
    private static final String SESSION_KEY_PREFIX = "sso.session.";
    private static final String SESSION_PC_KEY_PREFIX = "sso.session.pc.";
    private static final String SESSION_APP_KEY_PREFIX = "sso.session.app.";
    private static final int MEDIS_USER_SESSION_TIMEOUT = 3600 * 24 * 7;

    @Resource
    private UserService userService;
    @Resource
    private SsoCacheFacade ssoCacheFacade;

    public Session getSession(String token) {
        return ssoCacheFacade.getObject(sessionKey(token), Session.class);
    }

    private void delSessionFromCache(String token) {
        ssoCacheFacade.delete(sessionKey(token));
    }

    private void delAllSessionFromCache(List<String> tokenList){
        if(tokenList == null || tokenList.size() == 0){
            return;
        }
        List<String> keyList = new ArrayList<String>();
        for(String token : tokenList){
            keyList.add(sessionKey(token));
        }

        ssoCacheFacade.multiDelete(keyList);
    }

    public void delAndUpdateUserSession(String token) {
        TraceContext.put("token", token);
        Session session = getSession(token);
        LOG.info("delAndUpdateUserSession {} = {}", new Object[]{token, session});
        delSessionFromCache(token);
        if (session != null) {
            delFromUserSession(token, session);
        }
    }

    private void delFromUserSession(String token, Session session) {
        UserSession userSession = getUserSession(session.getUserLogin());
        if (userSession != null && userSession.getAppTokens() != null) {
            userSession.getAppTokens().remove(token);
            // TODO remove sessionTokens?
            saveUserSession(session.getUserLogin(), userSession);
        }
    }

    private void saveSessionToCache(String token, int sessionTimeout, Session session) {
        ssoCacheFacade.setObject(sessionKey(token), sessionTimeout, session);
    }




    public void saveSession(String token, AclUser aclUser, Integer timeout){
         saveSession(token, newSession(aclUser), timeout);
    }


    public void saveSession(String token, Session session, Integer timeout) {
        LOG.info("login ok, saveSession " + token + "=" + JSON.toJSONString(session));
        if (session == null) {
            return;
        }
        TraceContext.put("token", token);
        if (session != null) {
            TraceContext.put("uid", session.getUserId());
            TraceContext.put("username", session.getUserLogin());
        }
        saveSessionToCache(token, timeout == null ? Constants.SESSION_TIMEOUT : timeout, session);
        saveWebUserSession(session.getUserLogin(), token);
    }

    public String sessionKey(String token) {
        return SESSION_KEY_PREFIX + token;
    }

    private void saveWebUserSession(String username, String token) {
        UserSession userSession = getUserSession(username);
        if (userSession == null) {
            userSession = new UserSession();
        }
        userSession.getSessionTokens().put(UserSession.TokenKey.WEB, token);
        if (userSession.getAppTokens() == null) {
            userSession.setAppTokens(new HashMap<String, String>());
        }
        if (userSession.getAppTokens().size() < 5) {
            userSession.getAppTokens().put(token, token);
        }else {
            LOG.info("saveWebUserSession too many sessions {}", username);
            userSession.getAppTokens().clear();
            userSession.getAppTokens().put(token, token);
        }

        saveUserSession(username, userSession);
    }

    private void saveUserSession(String loginName, UserSession userSession) {
        ssoCacheFacade.setObject(userSessionKey(loginName), MEDIS_USER_SESSION_TIMEOUT, userSession);
    }

    public void delUserSession(String loginName) {
        ssoCacheFacade.delete(userSessionKey(loginName));
    }

    private UserSession getUserSession(String loginName) {
        return ssoCacheFacade.getObject(userSessionKey(loginName), UserSession.class);
    }

    public Map<String, Session> getAll(String loginName) {
        UserSession userSession = getUserSession(loginName);
        if (userSession == null) {
            return Collections.emptyMap();
        }
        Set<String> toDeleted = new HashSet<String>();
        Map<String, Session> sessions = new LinkedHashMap<String, Session>();
        for (String token : userSession.getAppTokens().keySet()) {
            Session session = getSession(token);
            if (session != null) {
                sessions.put(token, session);
            } else {
                toDeleted.add(token);
            }
        }
        for (String appToken : toDeleted) {
            userSession.getAppTokens().remove(appToken);
        }
        return sessions;
    }

    private String userSessionKey(String loginName) {
        return USER_KEY_PREFIX + loginName;
    }

    /**
     * 全部登出，离职等情况下
     *
     */
    public void executeLogoutByUserId(String uid) {
        try {
            AclUser aclUser = userService.getById(uid);
            if (aclUser != null) {
                TraceContext.put("uid", aclUser.getId());
                TraceContext.put("username", aclUser.getLoginName());
                UserSession userSession = getUserSession(aclUser.getLoginName());
                if (userSession != null) {
                    ArrayList tokenList = new ArrayList();
                    tokenList.addAll(userSession.getSessionTokens().values());
                    tokenList.addAll(userSession.getAppTokens().values());
                    delAllSessionFromCache(tokenList);
                }
                delUserSession(aclUser.getLoginName());
            }
        } catch (Exception e) {
            LOG.error("executeLogout exception", e);
        }
    }

    /**
     * 检查token对应用户session是否合法，同时更新session
     *
     * @param token
     * @return
     */
    public boolean checkSessionAndRefresh(String token) {
        Session session = getSession(token);
        if (session == null) {
            return false;
        }
        TraceContext.put("token", token);
        if (session != null) {
            TraceContext.put("uid", session.getUserId());
            TraceContext.put("username", session.getUserLogin());
        }
        LOG.info("updateSession " + token + "=" + JSON.toJSONString(session));
        session.setUpdateTime(new Date());
        session.setLastIp((String) TraceContext.get(Constants.CONTEXT_IP_KEY));
        session.setLastUA((String) TraceContext.get(Constants.CONTEXT_UA_KEY));
        session.setLastOs((String) TraceContext.get(Constants.CONTEXT_OS_KEY));
        session.setLastScreen((String) TraceContext.get(Constants.CONTEXT_SCREEN_KEY));
        session.setLastService((String) TraceContext.get(Constants.CONTEXT_SERVICE_KEY));
        session.setLastUuid((String) TraceContext.get(Constants.CONTEXT_UUID_KEY));
        int timeout = ssoCacheFacade.ttl(sessionKey(token));
        if (timeout < 3600) {
            saveSessionToCache(token, timeout + 3600, session);
        } else {
            saveSessionToCache(token, timeout, session);
        }
        saveWebUserSession(session.getUserLogin(), token);
        return true;
    }

    public boolean checkSession(String token) {
        return ssoCacheFacade.exists(sessionKey(token));
    }

    /**
     * 验证用户凭证，如未创建session创建
     *
     * @param username
     * @param password
     * @return
     */
    public boolean executeLogin(String username, String password) {
        AclUser aclUser = userService.executeVerify(username, password);
        if (aclUser != null) {
            return true;
        } else {
            TraceContext.put("status", Status.fail);
            return false;
        }
    }

    public void executeLogout(String sid) {
        LOG.info("logout, delAndUpdateUserSession " + sid);
        delAndUpdateUserSession(sid);
    }

    private Session newSession(AclUser user) {
        Session session = new Session();
        session.setUserId(user.getId());
        session.setUserLogin(user.getLoginName());
        session.setUserName(user.getName());
        session.setUserType(user.getType());
        session.setCreateTime(new Date());
        session.setUpdateTime(new Date());
        String loginIp = (String) TraceContext.get(Constants.CONTEXT_IP_KEY);
        session.setCreateIp(loginIp);
        session.setLastIp(loginIp);
        session.setCreateUA((String) TraceContext.get(Constants.CONTEXT_UA_KEY));
        session.setLastUA((String) TraceContext.get(Constants.CONTEXT_UA_KEY));
        session.setCreateOs((String) TraceContext.get(Constants.CONTEXT_OS_KEY));
        session.setLastOs((String) TraceContext.get(Constants.CONTEXT_OS_KEY));
        session.setCreateScreen((String) TraceContext.get(Constants.CONTEXT_SCREEN_KEY));
        session.setLastScreen((String) TraceContext.get(Constants.CONTEXT_SCREEN_KEY));
        session.setCreateService((String) TraceContext.get(Constants.CONTEXT_SERVICE_KEY));
        session.setLastService((String) TraceContext.get(Constants.CONTEXT_SERVICE_KEY));
        session.setUuid((String) TraceContext.get(Constants.CONTEXT_UUID_KEY));
        session.setLastUuid((String) TraceContext.get(Constants.CONTEXT_UUID_KEY));
        return session;
    }

    /**
     * 不存在情况下，返回new User()
     * 如果为工作时间，则顺延超时一小时
     *
     * @param token
     * @return
     */
    public UserForSeesion getSessionUserAndRefresh(String token) {
        if (token == null || token.trim().isEmpty()) {
            return new UserForSeesion();
        }
        TraceContext.put(Constants.CONTEXT_TOKEN, token);

        Session session = getSession(token);
        if (session != null) {
            TraceContext.put("token", token);
            if (session != null) {
                TraceContext.put("uid", session.getUserId());
                TraceContext.put("username", session.getUserLogin());
            }
            LOG.info("refreshSession " + token + "=" + JSON.toJSONString(session));
            saveSessionToCache(token, Constants.SESSION_TIMEOUT, session);
        }
        return session == null ? new UserForSeesion() : session.toUserForSession();
    }

    public User getSessionUserAndRefreshForApi(String token) {
        if (token == null || token.trim().isEmpty()) {
            return new User();
        }
        TraceContext.put(Constants.CONTEXT_TOKEN, token);

        Session session = getSession(token);
        if (session != null) {
            TraceContext.put("token", token);
            if (session != null) {
                TraceContext.put("uid", session.getUserId());
                TraceContext.put("username", session.getUserLogin());
            }
            LOG.info("refreshSession " + token + "=" + JSON.toJSONString(session));
            saveSessionToCache(token, Constants.SESSION_TIMEOUT, session);
        }
        return session == null ? new User() : session.toUser();
    }

    // TODO: 2017/12/25 pc session
    public void savePcSession(String token, AclUser aclUser, Integer timeout){
        savePcSession(token, newSession(aclUser), timeout);
    }
    public void savePcSession(String token, Session session, Integer timeout) {
        LOG.info("PC login ok, saveSession " + token + "=" + JSON.toJSONString(session));
        if (session == null) {
            return;
        }
        TraceContext.put("token", token);
        if (session != null) {
            TraceContext.put("uid", session.getUserId());
            TraceContext.put("username", session.getUserLogin());
        }
        savePcSessionToCache(token, timeout == null ? Constants.PC_SESSION_TIMEOUT : timeout, session);
        savePcUserSession(session.getUserLogin(), token);
    }
    public String pcSessionKey(String token) {
        return SESSION_PC_KEY_PREFIX + token;
    }
    private void savePcSessionToCache(String token, int sessionTimeout, Session session) {
        ssoCacheFacade.setObject(pcSessionKey(token), sessionTimeout, session);
    }
    //PC保存同账号多个token
    private void savePcUserSession(String username, String token) {
        UserSession userSession = getUserSession(username);
        if (userSession == null) {
            userSession = new UserSession();
        }
        //最后一次登录等token
        userSession.getSessionTokens().put(UserSession.TokenKey.PC, token);
        if (userSession.getPcTokens() == null) {
            userSession.setPcTokens(new HashMap<String, String>());
        }
        if (userSession.getPcTokens().size() < 5) {
            userSession.getPcTokens().put(token, token);
        }else {
            LOG.info("savePcUserSession too many sessions {}", username);
            userSession.getPcTokens().clear();
            userSession.getPcTokens().put(token, token);
        }
        saveUserSession(username, userSession);
    }
    //获取pcToken缓存信息
    public Session getPcSession(String token) {
        return ssoCacheFacade.getObject(pcSessionKey(token), Session.class);
    }
    //获取并刷新pc缓存信息
    public UserForSeesion getPcSessionUserAndRefresh(String token) {
        if (token == null || token.trim().isEmpty()) {
            return new UserForSeesion();
        }
        TraceContext.put(Constants.CONTEXT_TOKEN, token);
        //获取pcToken缓存信息
        Session session = getPcSession(token);
        if (session != null) {
            TraceContext.put("token", token);
            if (session != null) {
                TraceContext.put("uid", session.getUserId());
                TraceContext.put("username", session.getUserLogin());
            }
            LOG.info("refreshSession " + token + "=" + JSON.toJSONString(session));
            //刷新pc
            savePcSessionToCache(token, Constants.SESSION_TIMEOUT, session);
        }
        return session == null ? new UserForSeesion() : session.toUserForSession();
    }

    //pc删除key
    private void delPcSessionFromCache(String token) {
        ssoCacheFacade.delete(pcSessionKey(token));
    }

    //注销时删除pcTokens中对应的token
    private void delFromPcUserSession(String token, Session session) {
        UserSession userSession = getUserSession(session.getUserLogin());
        if (userSession != null && userSession.getAppTokens() != null) {
            userSession.getPcTokens().remove(token);
            saveUserSession(session.getUserLogin(), userSession);
        }
    }

    //pc清除缓存信息
    public void pcDelAndUpdateUserSession(String token) {
        TraceContext.put("token", token);
        Session session = getPcSession(token);
        LOG.info("pcDelAndUpdateUserSession {} = {}", new Object[]{token, session});
        delPcSessionFromCache(token);
        if (session != null) {
            delFromPcUserSession(token, session);
        }
    }
    //pc注销
    public void pcExecuteLogout(String sid) {
        LOG.info("logout, delAndUpdateUserSession " + sid);
        pcDelAndUpdateUserSession(sid);
    }


    // TODO: 2017/12/26 app
    public void saveAppSession(String token, AclUser aclUser, Integer timeout){
        saveAppSession(token, newSession(aclUser), timeout);
    }
    public void saveAppSession(String token, Session session, Integer timeout) {
        LOG.info("APP login ok, saveSession " + token + "=" + JSON.toJSONString(session));
        if (session == null) {
            return;
        }
        TraceContext.put("token", token);
        if (session != null) {
            TraceContext.put("uid", session.getUserId());
            TraceContext.put("username", session.getUserLogin());
        }
        saveAppSessionToCache(token, timeout == null ? Constants.APP_SESSION_TIMEOUT : timeout, session);
        saveAppUserSession(session.getUserLogin(), token);
    }
    public String appSessionKey(String token) {
        return SESSION_APP_KEY_PREFIX + token;
    }
    private void saveAppSessionToCache(String token, int sessionTimeout, Session session) {
        ssoCacheFacade.setObject(appSessionKey(token), sessionTimeout, session);
    }
    //App保存同账号多个token
    private void saveAppUserSession(String username, String token) {
        UserSession userSession = getUserSession(username);
        if (userSession == null) {
            userSession = new UserSession();
        }
        //最后一次登录等token
        userSession.getSessionTokens().put(UserSession.TokenKey.APP, token);
        if (userSession.getAppTokens() == null) {
            userSession.setAppTokens(new HashMap<String, String>());
        }
        if (userSession.getAppTokens().size() < 5) {
            userSession.getAppTokens().put(token, token);
        }else {
            LOG.info("saveAppUserSession too many sessions {}", username);
            userSession.getAppTokens().clear();
            userSession.getAppTokens().put(token, token);
        }
        saveUserSession(username, userSession);
    }
    //获取appToken缓存信息
    public Session getAppSession(String token) {
        return ssoCacheFacade.getObject(appSessionKey(token), Session.class);
    }
    //获取并刷新app缓存信息
    public UserForSeesion getAppSessionUserAndRefresh(String token) {
        if (token == null || token.trim().isEmpty()) {
            return new UserForSeesion();
        }
        TraceContext.put(Constants.CONTEXT_TOKEN, token);
        //获取pcToken缓存信息
        Session session = getAppSession(token);
        if (session != null) {
            TraceContext.put("token", token);
            if (session != null) {
                TraceContext.put("uid", session.getUserId());
                TraceContext.put("username", session.getUserLogin());
            }
            LOG.info("refreshSession " + token + "=" + JSON.toJSONString(session));
            //刷新app
            saveAppSessionToCache(token, Constants.APP_SESSION_TIMEOUT, session);
        }
        return session == null ? new UserForSeesion() : session.toUserForSession();
    }

    //app删除key
    private void delAppSessionFromCache(String token) {
        ssoCacheFacade.delete(appSessionKey(token));
    }

    //注销时删除appTokens中对应的token
    private void delFromAppUserSession(String token, Session session) {
        UserSession userSession = getUserSession(session.getUserLogin());
        if (userSession != null && userSession.getAppTokens() != null) {
            userSession.getPcTokens().remove(token);
            saveUserSession(session.getUserLogin(), userSession);
        }
    }

    //app清除缓存信息
    public void appDelAndUpdateUserSession(String token) {
        TraceContext.put("token", token);
        Session session = getAppSession(token);
        LOG.info("appDelAndUpdateUserSession {} = {}", new Object[]{token, session});
        delAppSessionFromCache(token);
        if (session != null) {
            delFromAppUserSession(token, session);
        }
    }
    //pc注销
    public void appExecuteLogout(String sid) {
        LOG.info("logout, appDelAndUpdateUserSession " + sid);
        appDelAndUpdateUserSession(sid);
    }
}
