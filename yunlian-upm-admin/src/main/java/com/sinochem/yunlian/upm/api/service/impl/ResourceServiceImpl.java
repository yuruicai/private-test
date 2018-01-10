package com.sinochem.yunlian.upm.api.service.impl;

import com.alibaba.fastjson.JSON;
import com.sinochem.yunlian.upm.admin.domain.AclMenu;
import com.sinochem.yunlian.upm.admin.domain.AclMenuExample;
import com.sinochem.yunlian.upm.admin.mapper.AclMenuMapper;
import com.sinochem.yunlian.upm.api.exception.ApiException;
import com.sinochem.yunlian.upm.api.service.ResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author huangyang
 * @Description:
 * @date 2018/01/10 下午2:13
 */
@Slf4j
@Service
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    private AclMenuMapper menuDao;

    @Override
    public void add(AclMenu menu) {
        if (StringUtils.isEmpty(menu.getApplicationId())) {
            throw ApiException.of("菜单所属应用为空");
        }
        if (StringUtils.isEmpty(menu.getTitle())) {
            throw ApiException.of("菜单标题为空");
        }
        menuDao.insert(menu);
        log.info("新增菜单：" + JSON.toJSONString(menu));
    }

    @Override
    public void modifyById(AclMenu menu) {
        menuDao.updateByPrimaryKey(menu);
        log.info("更新菜单: " + JSON.toJSONString(menu));
    }

    @Override
    public void deleteById(String id) {
        menuDao.deleteByPrimaryKey(id);
        log.info("删除菜单，id=" + id);

    }

    @Override
    public AclMenu getById(String id) {
        return menuDao.selectByPrimaryKey(id);
    }

    @Override
    public List<AclMenu> getByAppId(String appId) {
        if (appId == null) {
            throw ApiException.of("appId参数为空");
        }
        AclMenuExample example = new AclMenuExample();
        example.or().andApplicationIdEqualTo(appId);
        return menuDao.selectByExample(example);
    }
}
