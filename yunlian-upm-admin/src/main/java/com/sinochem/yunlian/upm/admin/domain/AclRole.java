package com.sinochem.yunlian.upm.admin.domain;

import com.sinochem.yunlian.upm.api.util.StringUtils;
import lombok.Data;

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

}

