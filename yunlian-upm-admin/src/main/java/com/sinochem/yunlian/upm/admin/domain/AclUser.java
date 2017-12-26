package com.sinochem.yunlian.upm.admin.domain;

import com.sinochem.yunlian.upm.admin.constant.UserStatus;
import com.sinochem.yunlian.upm.admin.constant.UserType;
import com.sinochem.yunlian.upm.util.CommonConvert;
import com.sinochem.yunlian.upm.util.StringUtil;

import java.util.Date;
import java.util.List;

public class AclUser {
    private String id;

    private Short type;

    private String code;

    private String loginName;

    private String email;

    private String mobile;

    private String name;

    private String password;

    private String qqCode;

    private String orgId;

    private String comment;

    private Short status;

    private Date createTime;

    private Date updateTime;

    private String idCode;

    private String salt;

    private Date passwordUpdateTime;

    private String createUid;

    private String updateUid;

    private String typeName;

    private String statusName;

    public static void richUser(AclUser user){
        if(user != null){
            if(user.getType() != null){
                user.setTypeName(UserType.getName(user.getType()));
            }

            if(user.getStatus() != null){
                user.setStatusName(UserStatus.getName(user.getStatus()));
            }

        }
    }

    public static List<AclUser> richUser(List<AclUser> users){

        if(users != null && users.size() > 0){
            for(AclUser user : users){
                richUser(user);
            }
        }

        return users;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName == null ? null : loginName.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public String getDecryptMobile(){
        if(StringUtil.isNotBlank(mobile)) {
            return CommonConvert.decryptMobile(mobile);
        }
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : CommonConvert.encryptMobile(mobile.trim());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getQqCode() {
        return qqCode;
    }

    public void setQqCode(String qqCode) {
        this.qqCode = qqCode == null ? null : qqCode.trim();
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId == null ? null : orgId.trim();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getIdCode() {
        if(StringUtil.isNotBlank(idCode)) {
            return CommonConvert.decryptIdNumber(idCode);
        }
        return idCode;
    }

    public void setIdCode(String idCode) {
        this.idCode = idCode == null ? null : CommonConvert.encryptIdNumber(idCode.trim());
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt == null ? null : salt.trim();
    }

    public Date getPasswordUpdateTime() {
        return passwordUpdateTime;
    }

    public void setPasswordUpdateTime(Date passwordUpdateTime) {
        this.passwordUpdateTime = passwordUpdateTime;
    }

    public String getCreateUid() {
        return createUid;
    }

    public void setCreateUid(String createUid) {
        this.createUid = createUid == null ? null : createUid.trim();
    }

    public String getUpdateUid() {
        return updateUid;
    }

    public void setUpdateUid(String updateUid) {
        this.updateUid = updateUid == null ? null : updateUid.trim();
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}