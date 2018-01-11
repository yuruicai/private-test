package com.sinochem.yunlian.upm.api.vo;

import com.sinochem.yunlian.upm.admin.domain.AclUser;
import lombok.Data;


@Data
public class UserByIdVo {
    private UserByIdVo userByIdVo;

    private String id;

    private Short type;

    private String loginName;

    private String remarks;

    private String email;

    private String mobile;

    private String name;

    private Short status;

    private String source;

    private String headImage;

    private short gender;



    public UserByIdVo(AclUser aclUser) {
        this.id = aclUser.getId();
        this.type = aclUser.getType();
        this.loginName = aclUser.getLoginName();
        this.email = aclUser.getEmail();
        this.mobile = aclUser.getMobile();
        this.name = aclUser.getName();
        this.status = aclUser.getStatus();
        this.source = aclUser.getSource();
        this.headImage = aclUser.getHeadImage();
        this.gender = aclUser.getGender();
        this.remarks = aclUser.getRemarks();
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public short getGender() {
        return gender;
    }

    public void setGender(short sex) {
        this.gender = sex;
    }
}
