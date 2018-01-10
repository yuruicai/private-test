package com.sinochem.yunlian.upm.api.vo;

/**
 * @author gaowei
 * @Description: 企业成员管理：成员列表
 * @date 2018/1/9.14:20
 */
public class CompanyUserVo {

    //姓名
    private String name;
    //角色
    private String role;
    //是否是管理员
    private int admin;
    //是否被删除
    private int status;

    public CompanyUserVo() {
    }

    public CompanyUserVo(String name, String role, int admin, int status) {
        this.name = name;
        this.role = role;
        this.admin = admin;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getAdmin() {
        return admin;
    }

    public void setAdmin(int admin) {
        this.admin = admin;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
