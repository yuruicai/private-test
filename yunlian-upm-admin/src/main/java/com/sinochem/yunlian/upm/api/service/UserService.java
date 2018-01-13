package com.sinochem.yunlian.upm.api.service;

import com.sinochem.yunlian.upm.admin.domain.AclUser;
import com.sinochem.yunlian.upm.api.vo.PageInfo;
import com.sinochem.yunlian.upm.api.vo.UserByIdVo;
import com.sinochem.yunlian.upm.api.vo.UserVo;

import java.util.List;

public interface UserService {

    PageInfo getUserListByCriteria(String name, int page, int rows);

    List<UserVo> getByIds(List<String> ids);

    UserByIdVo getUserById(String id);

    int updateUser(AclUser aclUser);

    boolean mobileCheck(String id , String newMobile);

    boolean restPassword(String ifd ,String oldPassword, String newPassword1, String newPassword2);

    List<UserVo> selectUserBaseUserIds(List<String> userIds ,String name);
}
