package com.sinochem.yunlian.upm.api.service.impl;

import com.github.pagehelper.PageHelper;
import com.sinochem.yunlian.upm.admin.domain.*;
import com.sinochem.yunlian.upm.admin.mapper.*;
import com.sinochem.yunlian.upm.api.service.CompanyUserManageService;
import com.sinochem.yunlian.upm.api.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author gaowei
 * @Description:
 * @date 2018/1/9.10:45
 */
@Service
@Transactional
public class CompanyUserManageServiceImpl implements CompanyUserManageService {

    @Autowired
    private CompanyMapper companyDao;

    @Autowired
    private CompanyUserRelationMapper companyUserDao;

    @Autowired
    private AclUserMapper userDao;

    @Autowired
    private AclUserRoleRltMapper userRoleRltDao;

    @Autowired
    private AclRoleMapper roleDao;

    @Override
    public PageInfo getListOfCompany(String companyName, int page, int rows) {
        PageHelper.startPage(page, rows);
        String name = "".equals(companyName) ? null : companyName;
        List<Company> companyList = companyDao.findAllByCompanyName(name);
        List<CompanyVo> companyVos = companyList.stream().map(c -> new CompanyVo(c)).collect(Collectors.toList());
        com.github.pagehelper.PageInfo pageInfo = new com.github.pagehelper.PageInfo(companyVos);
        return new PageInfo(pageInfo.getPageNum(),pageInfo.getPageSize(),pageInfo.getPages(),(int) pageInfo.getTotal(),pageInfo.getList());
    }

    @Override
    public int updateCompanyStatus(String id) {
        Company company = null;
        if(id!=null){
             company = companyDao.findById(Integer.parseInt(id));
            int i = company.getStatus() == 0 ? 1 : 0;
            company.setStatus(new Short(i+""));
        }
        return companyDao.updateDynamicById(company);
    }

    @Override
    public PageInfo getListOfUser(String id, Integer page, int rows) {
        PageHelper.startPage(page, rows);
        CompanyUserRelationExample selectByExample = new CompanyUserRelationExample();
        CompanyUserRelationExample.Criteria criteria = selectByExample.createCriteria();
        criteria.andCompanyIdEqualTo(Integer.parseInt(id));
        List<CompanyUserRelation> companyUserList = companyUserDao.selectByExample(selectByExample);
        List CompanyUserVoList = new ArrayList<CompanyUserVo>();
        CompanyUserVo companyUserVo = new CompanyUserVo();
        //todo:遍历所有companyuser对象，进行操作
        for(CompanyUserRelation companyUser:companyUserList){
            //根据每一个对象的userid查询用户表获取每一个对象的姓名，存到CompanyUserVo
            String userId = companyUser.getUserId();
            String name = userDao.selectByPrimaryKey(userId).getName();
            companyUserVo.setName(name);
            //根据每一个对象的userid查询角色表获取每一个对象的角色id
            AclUserRoleRltExample userRoleRltExample = new AclUserRoleRltExample();
            AclUserRoleRltExample.Criteria criteria1 = userRoleRltExample.createCriteria();
            criteria1.andUserIdEqualTo(userId);
            List<AclUserRoleRlt> userRoleRlts = userRoleRltDao.selectByExample(userRoleRltExample);
            String roleId = null;
            for(AclUserRoleRlt userRoleRlt:userRoleRlts){
                 roleId = userRoleRlt.getRoleId();
            }
            // 根据角色id查询出角色，存到CompanyUserVo
            AclRole role = roleDao.selectByPrimaryKey(roleId);
            companyUserVo.setRole(role.getName());
            //查询每一个对象是否是管理员，存储到CompanyUserVo
            companyUserVo.setAdmin(companyUser.getAdmin());
            //查询每一个对象的状态，存储到CompanyUserVo
            companyUserVo.setStatus(companyUser.getStatus());
            CompanyUserVoList.add(companyUserVo);
        }
        com.github.pagehelper.PageInfo info = new com.github.pagehelper.PageInfo(CompanyUserVoList);
        return new PageInfo(info.getPageNum(),info.getPageSize(),info.getPages(),(int) info.getTotal(),info.getList());
    }

    @Override
    public int updateStatus(String id) {
        //根据用户id查询出数据id
        CompanyUserRelationExample selectByExample = new CompanyUserRelationExample();
        CompanyUserRelationExample.Criteria criteria = selectByExample.createCriteria();
        criteria.andUserIdEqualTo(id);
        List<CompanyUserRelation> companyUserRelations = companyUserDao.selectByExample(selectByExample);
        //更新用户状态
        CompanyUserRelation companyUserRelation = companyUserDao.selectByPrimaryKey(companyUserRelations.get(0).getId());
        int status = companyUserRelation.getStatus() == 0 ? 1 : 0;
        companyUserRelation.setStatus(new Short(status+""));
        return companyUserDao.updateByPrimaryKey(companyUserRelation);
    }

    @Override
    public int updateAdmin(String id) {
       try{
           //查询当前用户所在公司
           CompanyUserRelationExample selectByExample = new CompanyUserRelationExample();
           CompanyUserRelationExample.Criteria criteria = selectByExample.createCriteria();
           criteria.andUserIdEqualTo(id);
           List<CompanyUserRelation> companyUserRelations = companyUserDao.selectByExample(selectByExample);
           CompanyUserRelation companyUserRelation = companyUserRelations.get(0);
           Integer companyId = companyUserRelation.getCompanyId();
           //遍历该公司的人员，查找当前的管理员
           CompanyUserRelationExample selectByExample1 = new CompanyUserRelationExample();
           CompanyUserRelationExample.Criteria criteria1 = selectByExample1.createCriteria();
           criteria1.andCompanyIdEqualTo(companyId);
           criteria1.andAdminEqualTo(new Short(1+""));
           //修改当前管理员的状态
           List<CompanyUserRelation> companyUserRelations1 = companyUserDao.selectByExample(selectByExample1);
           CompanyUserRelation companyUserRelation1 = companyUserRelations1.get(0);
           companyUserRelation1.setAdmin(new Short(0+""));
           companyUserDao.updateByPrimaryKey(companyUserRelation1);
//        companyUserDao.updateByExample(selectByExample1);
           //将当前用户设置为管理员
           companyUserRelation.setAdmin(new Short(1+""));
           companyUserDao.updateByPrimaryKey(companyUserRelation);
       }catch(Exception e){
           throw new RuntimeException("设置失败");
       }
        return 1;
    }

    @Override
    public PageInfo getAllListOfUser(String loginName, String mobile, String name, Integer page, int rows) {
        PageHelper.startPage(page, rows);
        //查询符合条件的成员
        AclUserExample selectByExample = new AclUserExample();
        AclUserExample.Criteria criteria = selectByExample.createCriteria();
        if(loginName!=null){
            criteria.andLoginNameLike("%"+loginName+"%");
        }
        if(mobile!=null){
            criteria.andMobileLike("%"+mobile+"%");
        }
        if(name!=null){
            criteria.andNameLike("%"+name+"%");
        }
        List<AclUser> aclUsers = userDao.selectByExample(selectByExample);
        List userCompanyVos = new ArrayList<UserCompanyVo>();
       if(aclUsers.size()>0){
           //遍历每个成员，查询出每个成员所在的公司，封装到UserCompanyVo
           for(AclUser user:aclUsers){
               //先将基本信息封装
               UserCompanyVo userCompanyVo = new UserCompanyVo(user);
               CompanyUserRelationExample example = new CompanyUserRelationExample();
               CompanyUserRelationExample.Criteria criteria1 = example.createCriteria();
               criteria1.andUserIdEqualTo(user.getId());
               List<CompanyUserRelation> companyUserRelations = companyUserDao.selectByExample(example);
               Integer companyId = companyUserRelations.get(0).getCompanyId();
               //根据公司id查询公司名称
               Company company = companyDao.findById(companyId);
               userCompanyVo.setCompanyName(company.getCompanyName());
               userCompanyVos.add(userCompanyVo);
           }
       }
        com.github.pagehelper.PageInfo info = new com.github.pagehelper.PageInfo(userCompanyVos);
        return new PageInfo(info.getPageNum(),info.getPageSize(),info.getPages(),(int) info.getTotal(),info.getList());
    }
}
