package com.sinochem.yunlian.upm.sso.service;

import com.sinochem.yunlian.upm.sso.constant.RedisKey;
import com.sinochem.yunlian.upm.sso.domain.AclUser;
import com.sinochem.yunlian.upm.sso.mapper.AclUserMapper;
import com.sinochem.yunlian.upm.sso.util.CommonConvert;
import com.sinochem.yunlian.upm.sso.util.IdGenerator;
import com.sinochem.yunlian.upm.sso.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class RegisterService {
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    @Resource
    private AclUserMapper aclUserMapper;
    @Resource
    private UserService userService;
    @Resource
    private TimesLimitService timesLimitService;
    @Resource
    private IdGenerator idGenerator;

    //验证手机号重复
    public boolean existMobile(String mobile) {
        AclUser aclUser = null;
        if (mobile!=null && !StringUtil.isBlank(mobile)) {
            aclUser = userService.getByMobile(mobile);
        }
        if (aclUser != null) {
            return true;
        }
        return false;
    }

    public String getUserIdByMobile(String mobile) {
        AclUser aclUser = null;
        if (mobile!=null && !StringUtil.isBlank(mobile)) {
            aclUser = userService.getByMobile(mobile);
        }
        if (aclUser == null) {
            return null;
        }
        LOG.info("can't find user by " + mobile);
        return aclUser.getId();
    }

    public String register(String mobile, String password) {
        AclUser aclUser = new AclUser();
        try {
            if (StringUtil.isNotBlank(mobile) && StringUtil.isNotBlank(password)) {
                aclUser.setLoginName(mobile);
                mobile = CommonConvert.encryptMobile(mobile);
                aclUser.setMobile(mobile);
                userService.initPassword(aclUser,password);
                if (!existMobile(mobile)) {
                    Long userId = idGenerator.generatShardId(RedisKey.USER_ID.getKey(),1L);
                    aclUser.setId(String.valueOf(userId));
                    aclUser.setCreateTime(new Date());
                    int result = aclUserMapper.insertSelective(aclUser);
                    if (result < 1) {
                        timesLimitService.increaseIpFailLoginTimes();
                        return "注册失败";
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return null;
    }
}
