package com.sinochem.yunlian.upm.sso.service;

import com.sinochem.yunlian.upm.sso.domain.AclApplication;
import com.sinochem.yunlian.upm.sso.mapper.AclApplicationMapper;
import com.sinochem.yunlian.upm.sso.constant.CommonStatus;
import com.sinochem.yunlian.upm.sso.domain.AclApplicationExample;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ApplicationService {
    @Resource
    private AclApplicationMapper aclApplicationMapper;

    public static Map<String, AclApplication> APPLICATION_CACHE;
    public static List<AclApplication> APPLICATION_LIST_CACHE;

    @PostConstruct
    @Scheduled(fixedRate = 300000)
    public void init(){
        List<AclApplication> applications = getAllApplications();
        Map<String, AclApplication> map = new HashMap<String, AclApplication>();
        if(!CollectionUtils.isEmpty(applications)){
            APPLICATION_LIST_CACHE=applications;
            for (AclApplication application : applications){
                map.put(application.getAppkey(), application);
            }
        }
        APPLICATION_CACHE = map;
    }

    public String getSecretByAppkey(String appkey){
        AclApplication aclApplication = APPLICATION_CACHE.get(appkey);
        if(aclApplication == null){
            return null;
        }
        return aclApplication.getSecret();
    }

    public List<AclApplication> getAllApplications(){
        AclApplicationExample example = new AclApplicationExample();
        example.or().andStatusEqualTo((short) CommonStatus.ACTIVE.getIndex());
        return aclApplicationMapper.selectByExample(example);
    }

    public AclApplication getByAppkey(String appkey){
        //先从缓存里读取
        if(APPLICATION_CACHE != null){
            AclApplication application = APPLICATION_CACHE.get(appkey);
            if(application != null){
                return application;
            }
        }

        AclApplicationExample example = new AclApplicationExample();
        example.or().andStatusEqualTo((short) CommonStatus.ACTIVE.getIndex()).andAppkeyEqualTo(appkey);
        List<AclApplication> list = aclApplicationMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        return list.get(0);
    }

}
