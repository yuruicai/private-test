package com.sinochem.yunlian.upm.admin.domain;

import lombok.Data;

@Data
public class AclMenuForRole {
    private String roleId;
    private String menuId;
    private String parentId;
    private String title;
}
