package com.sinochem.yunlian.upm.api.service.impl;

import com.github.pagehelper.PageHelper;
import com.sinochem.yunlian.upm.admin.domain.*;
import com.sinochem.yunlian.upm.admin.mapper.*;
import com.sinochem.yunlian.upm.api.service.CompanyUserManageService;
import com.sinochem.yunlian.upm.api.vo.CompanyUserVo;
import com.sinochem.yunlian.upm.api.vo.CompanyVo;
import com.sinochem.yunlian.upm.api.vo.PageInfo;
import com.sinochem.yunlian.upm.api.vo.UserCompanyVo;
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
            if(companyUserRelations1.size()!=0){
                CompanyUserRelation companyUserRelation1 = companyUserRelations1.get(0);
                companyUserRelation1.setAdmin(new Short(0+""));
                companyUserDao.updateByPrimaryKey(companyUserRelation1);
            }
           //将当前用户设置为管理员
           companyUserRelation.setAdmin(new Short(1+""));
           companyUserDao.updateByPrimaryKey(companyUserRelation);
       }catch(Exception e){
           throw new RuntimeException("设置失败");
       }
        return 1;
    }

    @Override
    public PageInfo getAllListOfUser(String id,String loginName, String mobile, String name, Integer page, int rows) {
        PageHelper.startPage(page, rows);
        //todo:查询符合条件的成员，并且过滤掉已经在当前企业下的成员
        //todo:1、查询符合条件的成员
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
               CompanyUserRelation companyUserRelation = companyUserRelations.get(0);
               Integer companyId = companyUserRelation.getCompanyId();
               //todo:2、如果该成员所在的公司就是当前公司则不显示
               //todo:2.1、如果用户没有选择企业，则将所有的成员全部展示
               if(StringUtils.isEmpty(id)){
                   //根据公司id查询公司名称
                   Company company = companyDao.findById(companyId);
                   userCompanyVo.setCompanyName(company.getCompanyName());
                   userCompanyVos.add(userCompanyVo);
               }
               //todo:2.2、如果当前用户选择了某个企业，则在显示成员的时候过滤掉当前企业的成员
               //todo:2.2.1、该用户之前属于这个企业但是被删除，状态改为0，这种情况不应该被过滤
               if(!StringUtils.isEmpty(id) && (Integer.parseInt(id)==companyId)&& companyUserRelation.getStatus()==0){
                   //根据公司id查询公司名称
                   Company company = companyDao.findById(companyId);
                   userCompanyVo.setCompanyName(company.getCompanyName());
                   userCompanyVos.add(userCompanyVo);
               }
               //todo:2.2.2、过滤掉当前企业的成员
               if(!StringUtils.isEmpty(id) && !(Integer.parseInt(id)==companyId)){
                   //根据公司id查询公司名称
                   Company company = companyDao.findById(companyId);
                   userCompanyVo.setCompanyName(company.getCompanyName());
                   userCompanyVos.add(userCompanyVo);
               }

           }
       }
        com.github.pagehelper.PageInfo info = new com.github.pagehelper.PageInfo(userCompanyVos);
        return new PageInfo(info.getPageNum(),info.getPageSize(),info.getPages(),(int) info.getTotal(),info.getList());
    }

    @Override
    public int updateStatusOfUser(String id) {
        AclUser user = userDao.selectByPrimaryKey(id);
        int i = user.getStatus() == 0 ? 1 : 0;
        user.setStatus(new Short(i+""));
        return userDao.updateByPrimaryKey(user);
    }

    @Override
    public int add(String[] userIds, String companyId) {
        /*
        遍历成员列表数组，获取到每一个成员
        将当前成员的状态改为0
        添加一条记录，companyid的值改成传入的companyid
         */
        try{
            for(String userId:userIds){
                //根据userId查询userAndCompany，并更改userAndCompany当前状态
                CompanyUserRelationExample selectByExample = new CompanyUserRelationExample();
                CompanyUserRelationExample.Criteria criteria = selectByExample.createCriteria();
                criteria.andUserIdEqualTo(userId);
                List<CompanyUserRelation> companyUserRelations = companyUserDao.selectByExample(selectByExample);
                CompanyUserRelation companyUserRelation = companyUserRelations.get(0);
                companyUserRelation.setStatus(new Short(0+""));
                //将更改后状态的userAndCompany保存到数据库
                companyUserDao.updateByPrimaryKey(companyUserRelation);
                //将新的数据插入到数据库
                CompanyUserRelation companyUserRelation1 = new CompanyUserRelation();
                //设置当前companyId
                companyUserRelation1.setCompanyId(Integer.parseInt(companyId));
                //设置userId
                companyUserRelation1.setUserId(userId);
                //设置adminStatus
                companyUserRelation1.setAdmin(new Short(0+""));
                //设置status
                companyUserRelation1.setStatus(new Short(1+""));
                companyUserDao.insert(companyUserRelation1);
            }
        }catch(Exception e){
            throw new RuntimeException("操作失败");
        }
        return 0;
    }

}
