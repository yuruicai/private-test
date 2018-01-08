package com.sinochem.yunlian.upm.api.vo;

import com.sinochem.yunlian.upm.admin.domain.AclRole;

/**
 * @author huangyang
 * @Description:
 * @date 2018/01/08 下午3:38
 */
public class RoleVo {

    private AclRole role;

    public RoleVo(AclRole role) {
        this.role = role;
    }

    public String getCode() {
        return role.getCode();
    }

    public String getName() {
        return role.getName();
    }

    public String getStatus() {
        Short status = role.getStatus();
        if (status == 0) {
            return "启用";
        } else {
            return "禁用";
        }
    }

    public String getId(){
        return role.getId();
    }
}
