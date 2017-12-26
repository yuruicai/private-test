package com.sinochem.yunlian.upm.sso.service;

import com.sinochem.yunlian.upm.common.util.ThreadCacheUtil;
import com.sinochem.yunlian.upm.sso.constant.UserStatus;
import com.sinochem.yunlian.upm.sso.domain.AclUser;
import com.sinochem.yunlian.upm.sso.domain.AclUserExample;
import com.sinochem.yunlian.upm.sso.mapper.AclUserMapper;
import com.sinochem.yunlian.upm.sso.model.Session;
import com.sinochem.yunlian.upm.sso.password.Checker;
import com.sinochem.yunlian.upm.sso.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhangxi
 * @created 13-12-20
 */
@Service
public class UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    public static final String THREAD_CACHE_ACL_USER = "THREAD_CACHE_ACL_USER";

    @Resource
    private AclUserMapper aclUserMapper;
    @Resource
    private TimesLimitService timesLimitService;
    @Resource
    private SessionService sessionService;

    public AclUser getById(String id) {
        return aclUserMapper.selectByPrimaryKey(id);
    }

    public AclUser getByMobile(String mobile) {
        //2.从数据库里取
        AclUser aclUser = getByMobileFromDB(mobile);
        return aclUser;
    }

    @Transactional(readOnly = true)
    public AclUser getByMobileFromDB(String mobile){
        mobile = CommonConvert.encryptMobile(mobile);
        AclUserExample condition = new AclUserExample();
        condition.or().andMobileEqualTo(mobile);
        List<AclUser> aclUsers = aclUserMapper.selectByExample(condition);
        if (aclUsers.isEmpty()) {
            LOG.info("can't find user by " + mobile);
            return null;
        }
        if (aclUsers.size() > 1) {
            LOG.error("find more then one user by " + mobile);
        }

        AclUser aclUser =  aclUsers.isEmpty() ? null : aclUsers.get(0);
        return aclUser;
    }

    public AclUser getByLoginNameOrPhone(String username) {
        if (StringUtil.isBlank(username)) {
            LOG.info("username is null ");
            return null;
        }

        if (StringUtil.isNumeric(username)) {
            return getByMobile(username);
        } else {
            return getByLoginName(username);
        }
    }

    public AclUser getByLoginName(String username){
        if (StringUtil.isBlank(username)) {
            LOG.info("username is null ");
            return null;
        }
        //首先ThreadLocal中读取
        AclUser aclUser = getAclUserFromThreadLocal(username);
        if(aclUser != null){
            return aclUser;
        }

        //从数据库里取
        aclUser = getByLoginNameFromDB(username);

        //写回cache
        if(aclUser != null){
            LOG.debug("getByLoginName FromDB ok " + username);
            setAclUserToThreadLocal(aclUser);
        }
        return aclUser;
    }

    private AclUser getAclUserFromThreadLocal(String username){
        AclUser aclUser = (AclUser) ThreadCacheUtil.getData(THREAD_CACHE_ACL_USER);
        if(aclUser != null && aclUser.getLoginName().equals(username)){
            return aclUser;
        }
        return null;
    }

    private void setAclUserToThreadLocal(AclUser aclUser){
        ThreadCacheUtil.setData(THREAD_CACHE_ACL_USER, aclUser);
    }

    @Transactional
    public void update(AclUser aclUser) {
        aclUserMapper.updateByPrimaryKey(aclUser);
    }

    @Transactional(readOnly = true)
    public AclUser getByLoginNameFromDB(String username){
        AclUserExample condition = new AclUserExample();
        condition.or().andLoginNameEqualTo(username);
        List<AclUser> aclUsers = aclUserMapper.selectByExample(condition);
        if (aclUsers == null) {
            LOG.info("can't find user by " + username);
            return null;
        }
        return aclUsers.isEmpty() ? null : aclUsers.get(0);
    }

    /**
     * 验证用户名密码是否正确
     *
     * @param username
     * @param password
     * @return
     */
    public AclUser executeVerify(String username, String password) {
        AclUser aclUser = getByLoginNameOrPhone(username);
        if (aclUser == null) {
            return null;
        }
        if (!aclUser.getStatus().equals((short) UserStatus.ACTIVE.getIndex())) {
            LOG.warn("illegal user trylogin..." + username);
            return null;
        }

        if (SecurityUtils.matchPassphrase(aclUser.getPassword(), aclUser.getSalt(), password)) {
            TraceContext.put("uid", aclUser.getId());
            TraceContext.put("username", aclUser.getLoginName());
            return aclUser;
        } else {
            LOG.warn("user password don't match..." + username);
            //增加IP错误登录次数
            timesLimitService.increaseIpFailLoginTimes();
            timesLimitService.increaseUsernameFailLoginTimes(username);

            return null;
        }
    }

    public String updatePassword(AclUser aclUser, String password1, String password2) {
        if (!password1.equals(password2)) {
            return "两次输入密码不一致";
        }
        String ret = Checker.basic(aclUser.getLoginName(), password1);
        if (ret != null) {
            return ret;
        }
        return updatePasswordByAclUser(aclUser, password1);
    }

    public String updatePassword(AclUser aclUser, String password) {

        String ret = Checker.basic(aclUser.getLoginName(), password);
        if (ret != null) {
            return ret;
        }
        return updatePasswordByAclUser(aclUser, password);
    }

    //校验密码的安全级别
    public String checkerPasswordLevel(String username, String password) {

        String ret = Checker.basic(username, password);
        return ret;
    }

    public String updatePasswordByAclUser(AclUser aclUser, String password) {
        //初始化加密密码
        initPassword(aclUser, password);
        update(aclUser);
        return null;
    }

    public String initPassword(AclUser aclUser, String newPassword){
        //设置密码
        String salt = SecurityUtils.getSalt(aclUser.getMobile());
        String passphrase = SecurityUtils.getPassphrase(salt, newPassword);
        aclUser.setPassword(passphrase);
        aclUser.setSalt(salt);
        return newPassword;
    }

    //登出设置
    public String logoutAll(String token) {
        if (StringUtil.isBlank(token)) {
            return "token不能为空";
        }
        try {
            if (!StringUtil.isBlank(token)) {
                Session session = sessionService.getSession(token);
                sessionService.executeLogout(token);
                if (session != null) {
                    sessionService.executeLogout(session.getUserId());
                    sessionService.delUserSession(session.getUserLogin());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "登出失败";
        }
        return null;
    }



}
