package com.sinochem.yunlian.upm.admin.mapper;

import com.sinochem.yunlian.upm.admin.bean.AppManagerBean;
import com.sinochem.yunlian.upm.admin.domain.AclApplication;
import com.sinochem.yunlian.upm.admin.domain.AclMenuForRole;
import com.sinochem.yunlian.upm.admin.domain.AclRole;
import com.sinochem.yunlian.upm.admin.domain.MenuItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhangxi
 * @created 13-6-17
 */
public interface UpmMapper {

    /**
     * 根据用户及应用信息查询对应资源配置项
     * 
     * @param userId
     * @param appkey
     * @return
     */
    List<String> selectResource(@Param(value = "userId") String userId,
                                @Param(value = "appkey") String appkey);

    /**
     * 根据用户及应用信息查询对应权限
     * 
     * @param userId
     * @param appkey
     * @return
     */
    List<String> selectPermission(@Param(value = "userId") String userId,
            @Param(value = "appkey") String appkey);

    /**
     * 根据用户及应用信息查询对应角色
     * 
     * @param userId
     * @param appkey
     * @return
     */
    List<AclRole> selectRole(@Param(value = "userId") String userId,
                             @Param(value = "appkey") String appkey);

    /**
     * 查找应用对应的资源
     *
     * @param appkey
     * @return
     */
    List<String> selectAppResource(@Param(value = "appkey") String appkey);

    /**
     * 根据用户及应用信息查询对应子菜单项
     * 
     * @param userId
     * @param appkey
     * @return
     */
    List<MenuItem> selectMenu(@Param(value = "userId") String userId,
                              @Param(value = "appkey") String appkey);

    List<MenuItem> selectAppMenu(@Param(value = "appkey") String appkey);

    List<AppManagerBean> selectAppManagers(String appId);

    List<AclApplication> selectApps4Manager(@Param(value = "userId") String userId);

    List<AclMenuForRole> getMenuByRoleId(@Param("appId") String appId, @Param("roleId") String roleId);
    List<AclApplication> selectAppsByUser(@Param(value = "userId") String userId);

}
