package com.sinochem.yunlian.upm.admin.mapper;

import com.sinochem.yunlian.upm.admin.domain.AclUser;
import org.apache.ibatis.annotations.Param;

import java.util.List; /**
 * user新增dao接口
 */
public interface NewUserMapper {

    List<AclUser> selectUserRole(@Param("ids") String ids, @Param("name") String name);
}
