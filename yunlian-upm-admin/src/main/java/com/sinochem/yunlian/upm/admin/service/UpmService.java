package com.sinochem.yunlian.upm.admin.service;

import com.sinochem.yunlian.upm.admin.domain.AclApplication;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.sinochem.yunlian.upm.admin.constant.Constants;
import com.sinochem.yunlian.upm.admin.mapper.UpmMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author zhangxi
 * @created 13-7-9
 */
@Service
public class UpmService {

    @Resource
    private UserRoleService userRoleService;
    @Resource
    private ApplicationService applicationService;
    @Resource
    private UpmMapper upmMapper;



    public List<AclApplication> getAllowedApps(String userId) {
        if (null == userId) {
            return Lists.newArrayList();
        }
        if (isSupper(userId)) {
            return applicationService.getList();
        }
        Map<String, AclApplication> apps = new HashMap<String, AclApplication>();
        for (AclApplication app : upmMapper.selectApps4Manager(userId)) {
            apps.put(app.getId(), app);
        }
        return Lists.newArrayList(apps.values());
    }

    public Set<String> getAllowedAppIds(String userId) {
        return Sets.newHashSet(Lists.transform(getAllowedApps(userId), new Function<AclApplication, String>() {
            @Override
            public String apply(AclApplication input) {
                return input.getId();
            }
        }));
    }

    public boolean isSupper(String userId) {
        return null == userId ? false : userRoleService.isHaveRole(userId, Constants.SUPER);
    }
    public List<AclApplication> selectAppsByUser(String userId){
        if (null == userId) {
            return Lists.newArrayList();
        }
        AclApplication sso = null;
        AclApplication upm = null;
        List<AclApplication> list = Lists.newArrayList();
        List<AclApplication> applications = upmMapper.selectAppsByUser(userId);
        for(AclApplication aclApplication : applications){
            if("sso".equalsIgnoreCase(aclApplication.getAppkey())){
                sso = aclApplication;
            }else if("upm".equalsIgnoreCase(aclApplication.getAppkey())){
                upm = aclApplication;
            }else{
                list.add(aclApplication);
            }
        }
        if(upm != null){
            list.add(0,upm);
        }
        if(sso != null){
            list.add(0,sso);
        }

        return list;
    }
}
