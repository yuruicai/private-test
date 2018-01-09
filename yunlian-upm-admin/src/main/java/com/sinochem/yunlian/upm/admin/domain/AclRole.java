package com.sinochem.yunlian.upm.admin.domain;

import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * 角色
 */
@Data
public class AclRole {
    private String id;

    private String code;

    private String name;

    private Short status;

    private String comment;

    private Date createTime;

    private Date updateTime;

    private Integer roleType;

    //应用ID
    private String applicationId;

    public void setCode(String code) {
        if (!StringUtils.isEmpty(code)) {
            this.code = code.trim();
        }
    }

    public void setName(String name) {
        if (!StringUtils.isEmpty(name)) {
            this.name = name.trim();
        }
    }

}

