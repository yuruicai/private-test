package com.sinochem.yunlian.upm.api.domain;

import com.sinochem.yunlian.upm.admin.constant.UserStatus;
import com.sinochem.yunlian.upm.admin.constant.UserType;
import com.sinochem.yunlian.upm.admin.domain.AclUser;

import java.util.List;

/**
 *
 */
public abstract class RichUtil {
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
