package com.sinochem.yunlian.upm.api.service;

import com.sinochem.yunlian.upm.admin.domain.AclMenu;
import com.sinochem.yunlian.upm.admin.domain.Operation;
import com.sinochem.yunlian.upm.admin.domain.ResourceData;

import java.util.List;

/**
 * @author huangyang
 * @Description:
 * @date 2018/01/08 下午5:55
 */
public interface PermissionService {
    List<AclMenu> getByUserIdAndAppKey(String appKey, String userId);

    List<AclMenu> getFirstLevelByUserIdAndAppKey(String appKey, String userId);

    //根据应用id和类型查询出该应用下所有的元素记录
    List<Operation> selectElement(String appId,String typeId);

    //随机生成12位字符串
    String randomStr();

    //保存元素/操作资源
    int saveOperation(Operation operation);

    //根据元素/操作id删除元素/操作
    int updateStatus(String id);

    //根据元素/操作id查询纪录
    Operation getOne(String id);

    //根据appId查询出该应用下所有的数据记录
    List<ResourceData> selectData(String appId);

    //根据数据id删除数据记录
    int updateStatusOfData(String id);

    //保存数据资源
    int saveData(ResourceData resourceData);

    //根据数据编码查询记录
    ResourceData getDataOne(String code);
}
