package com.sinochem.yunlian.upm.api.service;

import com.sinochem.yunlian.upm.admin.domain.AclMenu;

import java.util.List;

/**
 * @author huangyang
 * @Description:
 * @date 2018/01/08 下午5:55
 */
public interface PermissionService {
    List<AclMenu> getByUserIdAndAppKey(String appKey, String userId);
}
