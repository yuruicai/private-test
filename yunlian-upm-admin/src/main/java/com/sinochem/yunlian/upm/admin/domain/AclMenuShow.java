package com.sinochem.yunlian.upm.admin.domain;

import lombok.Data;

@Data
public class AclMenuShow {
    private String id;
    private String pId;
    private String name;
    private boolean open;
    private boolean checked;
}
