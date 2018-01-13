package com.sinochem.yunlian.upm.sso.service;

import com.sinochem.yunlian.upm.sso.domain.UserInviteSuccess;
import com.sinochem.yunlian.upm.sso.mapper.UserInviteSuccessMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户邀请
 */
@Service
public class UserInviteSuccessService {
    @Autowired
    private UserInviteSuccessMapper userInviteMapper;

    public void addInvite(String oldUserId, String userId) {
        userInviteMapper.insertSelective(new UserInviteSuccess());
    }
}
