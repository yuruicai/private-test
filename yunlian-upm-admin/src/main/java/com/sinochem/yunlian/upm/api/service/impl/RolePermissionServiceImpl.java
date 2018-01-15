package com.sinochem.yunlian.upm.api.service.impl;

import com.sinochem.yunlian.upm.admin.domain.*;
import com.sinochem.yunlian.upm.admin.mapper.*;
import com.sinochem.yunlian.upm.api.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gaowei
 * @Description:
 * @date 2018/1/11.10:53
 */
@Service
@Transactional
public class RolePermissionServiceImpl implements RolePermissionService {

    @Autowired
    private AclRoleInstanceContextMapper roleInstanceContextDao;

    @Autowired
    private RoleMenuMapper roleMenuDao;

    @Autowired
    private RoleOperationMapper roleOperationDao;

    @Autowired
    private RoleResourceDataMapper roleResourceDataDao;

    @Autowired
    private OperationMapper operationDao;

    @Autowired
    private AclMenuMapper menuDao;

    @Autowired
    private ResourceDataMapper resourceDataDao;

    @Override
    public Map getAll(String roleId) {
        //创建一个map用来存储应用，菜单，元素，操作，数据的id
        Map map = new HashMap();
        //查找该角色下所有的应用
        AclRoleInstanceContextExample applicationExample = new AclRoleInstanceContextExample();
        AclRoleInstanceContextExample.Criteria criteria = applicationExample.createCriteria();
        criteria.andUserRoleRltIdEqualTo(roleId);
        List<AclRoleInstanceContext> aclRoleInstanceContexts = roleInstanceContextDao.selectByExample(applicationExample);
        //遍历应用列表，将每一个应用的应用id查询出来添加到list，将应用Id_list添加到map
        List ApplicationIdList = new ArrayList();
        for(AclRoleInstanceContext aclRoleInstanceContext:aclRoleInstanceContexts){
            ApplicationIdList.add(aclRoleInstanceContext.getApplicationId());
        }
        map.put("ApplicationIdList",ApplicationIdList);

        //查找该角色下所有的菜单
        RoleMenuExample menuExample = new RoleMenuExample();
        RoleMenuExample.Criteria criteria1 = menuExample.createCriteria();
        criteria1.andRoleIdEqualTo(roleId);
        List<RoleMenu> roleMenus = roleMenuDao.selectByExample(menuExample);
        //遍历菜单列表，将每一个菜单的菜单id查询出来添加带list，将应用Id_list添加到map
        List MenuIdList = new ArrayList();
        for(RoleMenu roleMenu:roleMenus){
            MenuIdList.add(roleMenu.getMenuId());
        }
        map.put("MenuIdList",MenuIdList);

        //查找该角色下的所有元素/操作
        RoleOperationExample roleOperationExample = new RoleOperationExample();
        RoleOperationExample.Criteria criteria2 = roleOperationExample.createCriteria();
        criteria2.andRoleIdEqualTo(roleId);
        List<RoleOperation> roleOperations = roleOperationDao.selectByExample(roleOperationExample);
        //遍历操作列表，将每一个操作的操作id，按照类型分别添加到元素列表和操作列表，将列表添加到map
        List elementIdList = new ArrayList();
        List operateIdList = new ArrayList();
        for(RoleOperation roleOperation:roleOperations){
            Operation operation = operationDao.selectByPrimaryKey(Integer.parseInt(roleOperation.getOperationId()));
            if(operation.getType()==1){
                elementIdList.add(roleOperation.getOperationId());
            }else{
                operateIdList.add(roleOperation.getOperationId());
            }
        }
        map.put("elementIdList",elementIdList);
        map.put("operateIdList",operateIdList);
        //查找该角色下所有的数据
        RoleResourceDataExample dataExample = new RoleResourceDataExample();
        RoleResourceDataExample.Criteria criteria3 = dataExample.createCriteria();
        criteria3.andRoleIdEqualTo(roleId);
        List<RoleResourceData> roleResourceDatas = roleResourceDataDao.selectByExample(dataExample);
        List dataIdList = new ArrayList();
        for(RoleResourceData roleResourceData:roleResourceDatas){
            dataIdList.add(roleResourceData.getResourceDataId());
        }
        map.put("dataIdList",dataIdList);
        return map;
    }

    @Override
    public List getOne(String id, String typeId) {
        //todo:typeId:0：菜单  1：元素  2：操作  3：数据
        List list = new ArrayList();
        switch(typeId){
            case "0":
                AclMenu menu = menuDao.selectByPrimaryKey(id);
                list.add(menu);
                break;
            case "1":
            case "2":
                Operation operation = operationDao.selectByPrimaryKey(Integer.parseInt(id));
                list.add(operation);
                break;
            case "3":
                ResourceData resourceData = resourceDataDao.selectByPrimaryKey(Integer.parseInt(id));
                list.add(resourceData);
                break;
            default:
                list.add(null);
                break;
        }
        return list;
    }

    @Override
    public int save(Map map) {
        //定义一个数值记录操作状态  t: 1:处理成功;0:处理失败
        int t =1;
       try{
           //查询角色id
           String roleId = (String) map.get("roleId");
           //todo:1、先将该角色下的所有资源进行逻辑删除
           delete(roleId);

           //todo:2、将新勾选的数据进行保存
           save(map, roleId);
       }catch (Exception e){
           t=0;
           throw new RuntimeException("保存失败");
       }finally {
           if(t==0){
               return 0;
           }
           return 1;
       }

    }

    private void save(Map map, String roleId) {
        //todo:1、保存应用列表数据
        List applicationIdList = (List)map.get("applicationIdList");
        for(Object applicationId:applicationIdList){
            AclRoleInstanceContext roleInstanceContext = new AclRoleInstanceContext();
            roleInstanceContext.setUserRoleRltId(roleId);
            roleInstanceContext.setApplicationId((String)applicationId);
            roleInstanceContext.setStatus(new Byte(1+""));
            roleInstanceContextDao.insert(roleInstanceContext);
        }
        //todo:2、保存菜单列表数据
        List menuIdList = (List)map.get("menuIdList");
        for(Object menuId:menuIdList){
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId((String)menuId);
            roleMenu.setStatus(new Byte(1+""));
            roleMenuDao.insert(roleMenu);
        }
        //todo:3、保存操作列表数据
        List operationIdList = (List)map.get("operationIdList");
        for(Object operationId:operationIdList){
            RoleOperation roleOpeartion = new RoleOperation();
            roleOpeartion.setRoleId(roleId);
            roleOpeartion.setOperationId((String)operationId);
            roleOpeartion.setStatus(new Byte(1+""));
            roleOperationDao.insert(roleOpeartion);
        }
        //todo:4、保存数据列表数据
        List dataIdList = (List)map.get("dataIdList");
        for(Object dataId:dataIdList){
            RoleResourceData roleResourceData = new RoleResourceData();
            roleResourceData.setRoleId(roleId);
            roleResourceData.setResourceDataId((String)dataId);
            roleResourceData.setStatus(new Byte(1+""));
            roleResourceDataDao.insert(roleResourceData);
        }
    }

    private void delete(String roleId) {
        //todo:1、删除该角色下所有的应用
        //根据角色id查询出角色和应用关联列表数据
        AclRoleInstanceContextExample applicationExample = new AclRoleInstanceContextExample();
        AclRoleInstanceContextExample.Criteria criteria = applicationExample.createCriteria();
        criteria.andUserRoleRltIdEqualTo(roleId);
        List<AclRoleInstanceContext> aclRoleInstanceContexts = roleInstanceContextDao.selectByExample(applicationExample);
        //遍历角色和应用关联列表，将每一条数据的状态改为0,然后更新
        for(AclRoleInstanceContext aclRoleInstanceContext:aclRoleInstanceContexts){
            aclRoleInstanceContext.setStatus(new Byte(0+""));
            roleInstanceContextDao.updateByPrimaryKey(aclRoleInstanceContext);
        }
        //todo:2、删除该角色下所有的菜单
        //根据角色id查询出角色和菜单关联列表数据
        RoleMenuExample menuExample = new RoleMenuExample();
        RoleMenuExample.Criteria criteria1 = menuExample.createCriteria();
        criteria1.andRoleIdEqualTo(roleId);
        List<RoleMenu> roleMenus = roleMenuDao.selectByExample(menuExample);
        //遍历角色和菜单关联列表，将每一条数据的状态改为0，然后更新
        for(RoleMenu roleMenu:roleMenus){
            roleMenu.setStatus(new Byte(0+""));
            roleMenuDao.updateByPrimaryKey(roleMenu);
        }
        //todo:3、删除该角色下所有的元素和操作
        //根据角色id查询出角色和操作关联列表数据
        RoleOperationExample operationExample = new RoleOperationExample();
        RoleOperationExample.Criteria criteria2 = operationExample.createCriteria();
        criteria2.andRoleIdEqualTo(roleId);
        List<RoleOperation> roleOperations = roleOperationDao.selectByExample(operationExample);
        //遍历角色和操作关联列表，将每一条数据的状态改为0，然后更新
        for(RoleOperation roleOperation:roleOperations){
            roleOperation.setStatus(new Byte(0+""));
            roleOperationDao.updateByPrimaryKey(roleOperation);
        }
        //todo:4、删除该角色下所有的数据
        //根据角色id查询出角色和数据关联列表数据
        RoleResourceDataExample dataExample = new RoleResourceDataExample();
        RoleResourceDataExample.Criteria criteria3 = dataExample.createCriteria();
        criteria3.andRoleIdEqualTo(roleId);
        List<RoleResourceData> roleResourceDatas = roleResourceDataDao.selectByExample(dataExample);
        //遍历角色和数据关联列表，将每一条数据的状态改为0，然后更新
        for(RoleResourceData roleResourceData:roleResourceDatas){
            roleResourceData.setStatus(new Byte(0+""));
            roleResourceDataDao.updateByPrimaryKey(roleResourceData);
        }
    }
}
