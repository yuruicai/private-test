package com.sinochem.yunlian.upm.api.service.impl;

import com.github.pagehelper.PageHelper;
import com.sinochem.yunlian.upm.api.vo.ApplicationVo;
import com.sinochem.yunlian.upm.api.vo.PageInfo;
import com.sinochem.yunlian.upm.admin.domain.AclApplication;
import com.sinochem.yunlian.upm.admin.domain.AclApplicationExample;
import com.sinochem.yunlian.upm.admin.mapper.AclApplicationMapper;
import com.sinochem.yunlian.upm.api.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author gaowei
 * @Description:
 * @date 2018/1/6.17:42
 */
@Service
@Transactional
public class ApplicationServiceImpl implements ApplicationService {

    @Autowired
    private AclApplicationMapper applicationDao;

    @Override
    public String insert(AclApplication application) {
        applicationDao.insert(application);
        return application.getId();
    }

    @Override
    public String updateStatus(String id) {
        //根据id查询这条记录
        AclApplication app = applicationDao.selectByPrimaryKey(id);
        //更改这条记录的状态值
        int t = app.getStatus() == 0 ? 1 : 0;
        app.setStatus(new Short(t+""));
        //将更改后的数据存回
        applicationDao.updateByPrimaryKey(app);
        return id;
    }

    @Override
    public AclApplication select(String id) {
        return applicationDao.selectByPrimaryKey(id);
    }

    @Override
    public int update(AclApplication application) {
        //根据id先将本条数据查出来
        AclApplication newApplication = applicationDao.selectByPrimaryKey(application.getId());
        if(application.getName()!=null){
            newApplication.setName(application.getName());
        }
        if(application.getRoleId()!=null){
            newApplication.setRoleId(application.getRoleId());
        }
        if(application.getAppkey()!=null){
            newApplication.setAppkey(application.getAppkey());
        }
        if(application.getSecret()!=null){
            newApplication.setSecret(application.getSecret());
        }
        if(application.getUrl()!=null){
            newApplication.setUrl(application.getUrl());
        }
        if(application.getStatus()!=null){
            newApplication.setStatus(application.getStatus());
        }
        if(application.getImage1()!=null){
            newApplication.setImage1(application.getImage1());
        }
        if(application.getImage2()!=null){
            newApplication.setImage1(application.getImage2());
        }
        if(application.getUseUpm()!=null){
            newApplication.setUseUpm(application.getUseUpm());
        }
        return applicationDao.updateByPrimaryKey(newApplication);
    }


    @Override
    public PageInfo search(String appkey,String name,int page,int rows) {
        PageHelper.startPage(page, rows);
        AclApplicationExample applicationExample = new AclApplicationExample();
        AclApplicationExample.Criteria criteria = applicationExample.createCriteria();
        if(!StringUtils.isEmpty(appkey)){
            criteria.andAppkeyLike("%"+appkey+"%");
        }
        if(!StringUtils.isEmpty(name)){
            criteria.andNameLike("%"+name+"%");
        }

        List<AclApplication> list = applicationDao.selectByExample(applicationExample);
        List<ApplicationVo>  ApplicationVos= list.stream().map(app -> new ApplicationVo(app)).collect(Collectors.toList());
        com.github.pagehelper.PageInfo info = new com.github.pagehelper.PageInfo(ApplicationVos);
        return new PageInfo(info.getPageNum(),info.getPageSize(),info.getPages(),(int) info.getTotal(),info.getList());
    }

    @Override
    public int delete(String[] ids) {
        int t = 0;
        for(String id:ids){
            int i = applicationDao.deleteByPrimaryKey(id);
            if(i==1){
                t++;
            }
            if(ids.length!=t){
                throw new RuntimeException("删除异常");
            }
        }
        return ids.length==t?0:-1;
    }
}
