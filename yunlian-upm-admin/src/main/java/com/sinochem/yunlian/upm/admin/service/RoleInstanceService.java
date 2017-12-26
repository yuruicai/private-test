package com.sinochem.yunlian.upm.admin.service;

import com.sinochem.yunlian.upm.admin.domain.AclRoleInstanceContext;
import com.sinochem.yunlian.upm.admin.domain.AclRoleInstanceContextExample;
import com.sinochem.yunlian.upm.admin.mapper.AclRoleInstanceContextMapper;
import com.sinochem.yunlian.upm.filter.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 
 * @author wangwei
 * @version 1.0
 * @created 2013-05-21
 */
@Service
public class RoleInstanceService {
    private static final Logger LOG = LoggerFactory.getLogger(RoleInstanceService.class);

    @Resource
    private AclRoleInstanceContextMapper ricMapper;

    public void addInstance(AclRoleInstanceContext context){
        List<AclRoleInstanceContext> contexts = getInstanceList(context);
        if(CollectionUtils.isEmpty(contexts)){
            context.setId(UUID.randomUUID().toString());
            ricMapper.insert(context);
        }
    }

    public void deleteInstance(AclRoleInstanceContext context){
        AclRoleInstanceContextExample contextExample = getAclRoleInstanceContextExample(context);
        ricMapper.deleteByExample(contextExample);
    }

    public List<AclRoleInstanceContext> getInstanceList(AclRoleInstanceContext context){
        AclRoleInstanceContextExample contextExample = getAclRoleInstanceContextExample(context);
        return ricMapper.selectByExample(contextExample);
    }

    private AclRoleInstanceContextExample getAclRoleInstanceContextExample(AclRoleInstanceContext context) {
        AclRoleInstanceContextExample contextExample = new AclRoleInstanceContextExample();
        AclRoleInstanceContextExample.Criteria criteria = contextExample.or().andUserRoleRltIdEqualTo(context.getUserRoleRltId());

        if(context.getApplicationId() != null) {
            criteria.andApplicationIdEqualTo(context.getApplicationId());
        }else{
            criteria.andApplicationIdIsNull();
        }

        if(context.getOrgId() != null){
            criteria.andOrgIdEqualTo(context.getOrgId());
        }else{
            criteria.andOrgIdIsNull();
        }
        return contextExample;
    }

    public List<AclRoleInstanceContext> selectByRltId(String rltId){
        AclRoleInstanceContextExample contextExample = new AclRoleInstanceContextExample();
        contextExample.or().andUserRoleRltIdEqualTo(rltId);

        return ricMapper.selectByExample(contextExample);
    }

    public void deleteByRltId(String rltId){
        AclRoleInstanceContextExample contextExample = new AclRoleInstanceContextExample();
        contextExample.or().andUserRoleRltIdEqualTo(rltId);

        ricMapper.deleteByExample(contextExample);
    }
}
