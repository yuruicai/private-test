package com.sinochem.yunlian.upm.api.service;

import com.sinochem.yunlian.upm.admin.domain.AclMenu;

import java.util.List;

/**
 * @author huangyang
 * @Description: 资源即菜单服务类
 * @date 2018/01/10 下午2:09
 */
public interface ResourceService {
    /**
     * 新增
     * @param menu
     */

    void add(AclMenu menu);

    /**
     * 按ID更新
     * @param menu
     */
    void modifyById(AclMenu menu);

    /**
     * 根据ID删除
     * @param id
     */
    void deleteById(String id);

    /**
     *
     * @param id
     * @return
     */
    AclMenu getById(String id);


    List<AclMenu> getByAppId(String appId);
}
