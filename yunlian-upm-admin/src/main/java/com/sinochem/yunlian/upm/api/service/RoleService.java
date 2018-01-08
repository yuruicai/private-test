package com.sinochem.yunlian.upm.api.service;

import com.sinochem.yunlian.upm.admin.domain.AclRole;
import com.sinochem.yunlian.upm.api.vo.PageInfo;
import com.sinochem.yunlian.upm.api.vo.RoleVo;

import java.util.List;

/**
 * @author huangyang
 * @Description:
 * @date 2018/01/08 下午2:35
 */
public interface RoleService {

    /**
     * 插入
     * @param role
     * @return 返回生成的主键
     */
    String insert(AclRole role);

    /**
     * 更新
     * @param role 封装了要更新的数据
     */
    void modifyById(AclRole role);

    AclRole getByCode(String code);

    AclRole getById(String id);

    PageInfo<RoleVo> getByCodeOrName(String param, int curPage, int pageSize);

    List<AclRole> getUserRoleInApp(String appKey, String userId);
}
