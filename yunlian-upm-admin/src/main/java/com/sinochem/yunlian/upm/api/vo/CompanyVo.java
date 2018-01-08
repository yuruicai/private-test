package com.sinochem.yunlian.upm.api.vo;


import com.sinochem.yunlian.upm.admin.domain.Company;

import java.util.Date;

/**
 * 公司实体VO类，用于公司列表
 */
public class CompanyVo {

    private Company company;

    public CompanyVo(Company company) {
        this.company = company;
    }

    public String getCompanyName() {
        return this.company.getCompanyName();
    }

    public String getKind() {
        return this.company.getKind();
    }

    public String getAddress() {
        return this.company.getAddress();
    }

    public String getStatus() {
        Short status = this.company.getStatus();
        String statusStr = "";
        //0-未认证，1-已认证，2：未通过
        if (status == 0) {
            statusStr = "未认证";
        } else if (status == 1) {
            statusStr = "已认证";
        } else if (status == 2) {
            statusStr = "认证未通过";
        }
        return statusStr;
    }
    public Date getCreateTime(){
        return company.getCreateTime();
    }

}
