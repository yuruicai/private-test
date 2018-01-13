package com.sinochem.yunlian.upm.util;

import com.sinochem.yunlian.upm.filter.util.UserUtils;
import com.sinochem.yunlian.upm.tools.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author wangwei
 * @version 1.0
 * @created 2013-05-20
 */
@Component
public class UpmCacheUtil {
    private static final Logger LOG = LoggerFactory.getLogger(UpmCacheUtil.class);


    public static final String KEY_APPID = "appId";
    public static final String KEY_APPNAME = "appName";
    public static final String KEY_ERR_MSG = "errMsg";
    public static String getAppId() {
        return (String) ThreadCacheUtil.getData(KEY_APPID);
    }

    public static String getCurrentUserId() {
        User user = UserUtils.getUser();
        if (null != user) {
            return user.getId();
        }
        if (null != ThreadCacheUtil.getData(KEY_OPERATOR_ID)) {
            return (String) ThreadCacheUtil.getData(KEY_OPERATOR_ID);
        }
        return null;
    }


    public static AclUser getCurrentUser() {
        User user = UserUtils.getUser();
        AclUser aclUser = new AclUser();
        if (user != null) {
            ObjectUtil.copy(user, aclUser);
        }
        return aclUser;
    }

    public static final String KEY_OPERATOR_ID = "operatorId";

    public static void setErrMsg(String errMsg) {
        ThreadCacheUtil.setData(KEY_ERR_MSG, errMsg);
    }

    public static String getErrMsg() {
        return (String) ThreadCacheUtil.getData(KEY_ERR_MSG);
    }
    public static void setOperatorId(Integer operatorId) {
        ThreadCacheUtil.setData(KEY_OPERATOR_ID, operatorId);
    }

}
