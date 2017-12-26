/*
 * Copyright (c) 2010-2013 meituan.com
 * All rights reserved.
 * 
 */
package com.sinochem.yunlian.upm.admin.bean;

import com.sinochem.yunlian.upm.admin.domain.AclMenu;

/**
 * @author wangwei
 * @version 1.0
 * @created 2013-05-14
 */
public class MenuBean extends AclMenu{


    private String pId;

    private String name;

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
