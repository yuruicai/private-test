package com.sinochem.yunlian.upm.api.vo;

import com.sinochem.yunlian.upm.admin.domain.AclUser;

/**
 * @author gaowei
 * @Description:企业成员管理：所有成员列表
 * @date 2018/1/9.19:46
 */
public class UserCompanyVo extends UserVo {

    //所属企业
    private String companyName;

    public UserCompanyVo(AclUser aclUser) {
        super(aclUser);
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
