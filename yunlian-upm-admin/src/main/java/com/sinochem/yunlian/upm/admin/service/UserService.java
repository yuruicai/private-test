package com.sinochem.yunlian.upm.admin.service;

import com.sinochem.yunlian.upm.admin.bean.UserListBean;
import com.sinochem.yunlian.upm.admin.constant.SMSStatus;
import com.sinochem.yunlian.upm.admin.constant.UserStatus;
import com.sinochem.yunlian.upm.admin.domain.AclUser;
import com.sinochem.yunlian.upm.admin.domain.AclUserExample;
import com.sinochem.yunlian.upm.admin.mapper.AclUserMapper;
import com.sinochem.yunlian.upm.admin.model.react.FengError;
import com.sinochem.yunlian.upm.admin.model.react.FengResult;
import com.sinochem.yunlian.upm.admin.password.Checker;;
import com.sinochem.yunlian.upm.filter.util.UserUtils;
import com.sinochem.yunlian.upm.tools.UpmAuthService;
import com.googlecode.ehcache.annotations.Cacheable;
import com.sinochem.yunlian.upm.util.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Resource
    private AclUserMapper aclUserMapper;
    @Resource
    private SMSService smsService;
    @Resource
    private UserRoleService userRoleService;
    @Resource
    private UpmAuthService upmAuthService;

    public AclUser getById(String id) {
        return aclUserMapper.selectByPrimaryKey(id);
    }

    public List<AclUser> getByIds(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        AclUserExample example = new AclUserExample();
        example.or().andIdIn(ids).andStatusEqualTo((short) UserStatus.ACTIVE.getIndex());
        return aclUserMapper.selectByExample(example);
    }

    public FengResult checkUserExists(AclUser user){

        FengResult result = new FengResult();

        AclUserExample condition = new AclUserExample();
        condition.or().andLoginNameEqualTo(user.getLoginName());
        List<AclUser> aclUsers = aclUserMapper.selectByExample(condition);
        if (aclUsers != null && aclUsers.size() > 0) {
            if(StringUtil.isNotBlank(user.getId())){
                if(!aclUsers.get(0).getId().equals(user.getId())){
                    result.addError(new FengError("登录名已存在"));
                    return result;
                }
            }else{
                result.addError(new FengError("登录名已存在"));
                return result;
            }
        }

        condition = new AclUserExample();
        condition.or().andMobileEqualTo(user.getMobile());
        aclUsers = aclUserMapper.selectByExample(condition);
        if (aclUsers != null && aclUsers.size() > 0) {
            if(StringUtil.isNotBlank(user.getId())){
                if(!aclUsers.get(0).getId().equals(user.getId())){
                    result.addError(new FengError("手机号已存在"));
                    return result;
                }
            }else{
                result.addError(new FengError("手机号已存在"));
                return result;
            }
        }

        if(StringUtil.isNotBlank(user.getCode())) {
            condition = new AclUserExample();
            condition.or().andCodeEqualTo(user.getCode());
            aclUsers = aclUserMapper.selectByExample(condition);
            if (aclUsers != null && aclUsers.size() > 0) {
                if(StringUtil.isNotBlank(user.getId())){
                    if(!aclUsers.get(0).getId().equals(user.getId())){
                        result.addError(new FengError("员工号已存在"));
                        return result;
                    }
                }else{
                    result.addError(new FengError("员工号已存在"));
                    return result;
                }
            }
        }

        return result;

    }

    public AclUser getByLoginName(String username){
        AclUserExample condition = new AclUserExample();
        condition.or().andLoginNameEqualTo(username)
                .andStatusNotEqualTo((short)UserStatus.DELETE.getIndex());
        List<AclUser> aclUsers = aclUserMapper.selectByExample(condition);
        if (aclUsers == null) {
            return null;
        }
        return aclUsers.isEmpty() ? null : aclUsers.get(0);
    }

    public List<AclUser> getAllUserList(){
        AclUserExample example = new AclUserExample();
        example.or().andStatusEqualTo((short) UserStatus.ACTIVE.getIndex());
        return aclUserMapper.selectByExample(example);
    }

    public List<AclUser> getUserListByCriteria(UserListBean userListBean, Page page, List<String> in) {
        AclUserExample aclUserExample = new AclUserExample();
        AclUserExample.Criteria criteria = aclUserExample.or();
        if (!(StringUtils.isEmpty(userListBean.getName())
                && StringUtils.isEmpty(userListBean.getLoginName()) && StringUtils
                .isEmpty(userListBean.getMobile()))) {
            criteria.andNameLike("%" + userListBean.getName() + "%").andLoginNameLike(
                    "%" + userListBean.getLoginName() + "%");
            if (StringUtils.isNotBlank(userListBean.getMobile())) {
                criteria.andMobileEqualTo(CommonConvert.encryptMobile(userListBean.getMobile()));
            }
        }
        if (in != null) {
            criteria.andIdIn(in);
        }
        aclUserExample.setOrderByClause("update_time desc");

        List<AclUser> aclUsers = aclUserMapper.selectByExample(aclUserExample);
        page.setTotalCount(aclUserMapper.countByExample(aclUserExample));
        return aclUsers;
    }

    public void updateFreezeUser(String id) {
        //放置在调用删除session之后，避免前边出现将用户的状态改为正常的情况出现
        updateUserStatus(id, UserStatus.FREEZE.getIndex());
        upmAuthService.logoutAll(id);
    }

    public void updateRecovery(String id) {
        updateUserStatus(id, UserStatus.ACTIVE.getIndex());
    }

    public void updateDisable(String id) {
        updateUserStatus(id, UserStatus.DELETE.getIndex());
        upmAuthService.logoutAll(id);
    }

    public void updateUserStatus(String id, Integer userStatus) {
        AclUser aclUser = getById(id);
        if (aclUser == null) {
            return;
        }
        aclUser.setStatus(userStatus.shortValue());
        update(aclUser);
    }

    public void update(AclUser aclUser) {
        aclUser.setUpdateTime(new Date());
        aclUserMapper.updateByPrimaryKeySelective(aclUser);
    }
    public void updateSelective(AclUser aclUser) {
        aclUser.setUpdateTime(new Date());
        aclUserMapper.updateByPrimaryKeySelective(aclUser);
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

    public String updatePasswordByAclUser(AclUser aclUser, String password) {
        //初始化加密密码
        initPassword(aclUser, password);
        update(aclUser);
        return null;
    }

    private String initPassword(AclUser aclUser, String newPassword){
        //设置密码
        String salt = SecurityUtils.getSalt(aclUser.getIdCode());
        String passphrase = SecurityUtils.getPassphrase(salt, newPassword);
        aclUser.setPassword(passphrase);
        aclUser.setSalt(salt);
        aclUser.setPasswordUpdateTime(new Date());

        return newPassword;
    }

    public AclUser saveUser(AclUser aclUser) {
        if(StringUtil.isBlank(aclUser.getId())) {
            aclUser.setId(UUID.randomUUID().toString());
            aclUser.setStatus((short) UserStatus.ACTIVE.getIndex());
            aclUser.setCode(aclUser.getCode());
            //生成随机密码
            String password = SecurityUtils.randomPassword(SecurityLevel.GOOD);
            initPassword(aclUser, password);
            insert(aclUser);
            //发送短信
            SMSStatus smsStatus = smsService.sendSMS(aclUser.getDecryptMobile(), "2", "notification.creditmanager.employee.created", null, "壹化云链", aclUser.getLoginName(), "<pwd" + password + "pwd>");
            logger.info("{} 的密码已经发送给 {} {}", aclUser.getLoginName(), aclUser.getMobile(),smsStatus!= null ? smsStatus.getKey():"error");

            logger.info("add " + aclUser.getId() + "," + aclUser.getName() + "," + aclUser.getLoginName());
            //初始化应用默认角色
            userRoleService.addByAppAuto(aclUser.getId());
        }else{
            update(aclUser);
        }
        return aclUser;
    }

    public AclUser executeVerify(String username, String password) {
        AclUser aclUser = getByLoginName(username);
        if (aclUser == null) {
            return null;
        }
        if (!aclUser.getStatus().equals((short) UserStatus.ACTIVE.getIndex())) {
            return null;
        }

        if (SecurityUtils.matchPassphrase(aclUser.getPassword(), aclUser.getSalt(), password)) {
            return aclUser;
        } else {

            return null;
        }
    }

    public void insert(AclUser aclUser){
        Date now = new Date();
        aclUser.setCreateTime(now);
        aclUser.setCreateUid(UserUtils.getUser().getId());
        aclUser.setUpdateTime(now);
        if(aclUser.getCode() == null){
            aclUser.setCode("");
        }
        aclUser.setUpdateUid(UserUtils.getUser().getId());

        aclUserMapper.insertSelective(aclUser);
    }

    @Cacheable(cacheName = "LRUCache-5m", cacheNull = false)
    public List<AclUser> getAllUserList(Integer type, Long lastUpdateTime){
        AclUserExample example = new AclUserExample();
        AclUserExample.Criteria criteria = example.or();
        if(lastUpdateTime != null && lastUpdateTime >0){
            criteria.andUpdateTimeGreaterThanOrEqualTo(new Date(lastUpdateTime * 1000));
        }
        if(type != null && type > 0){
            criteria.andTypeEqualTo(type.shortValue());
        }
        return aclUserMapper.selectByExample(example);
    }
}
