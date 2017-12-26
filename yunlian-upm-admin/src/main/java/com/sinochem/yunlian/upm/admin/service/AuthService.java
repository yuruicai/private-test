package com.sinochem.yunlian.upm.admin.service;

import com.sinochem.yunlian.upm.admin.component.UpmMatcher;
import com.sinochem.yunlian.upm.admin.domain.MenuItem;
import com.sinochem.yunlian.upm.admin.mapper.UpmMapper;
import com.sinochem.yunlian.upm.filter.util.CollectionUtils;
import com.sinochem.yunlian.upm.filter.util.WildcardPermission;
import com.sinochem.yunlian.upm.util.StringUtil;
import com.sinochem.yunlian.upm.util.TimeUtil;
import com.google.common.collect.Lists;
import com.googlecode.ehcache.annotations.Cacheable;
import com.sinochem.yunlian.upm.admin.domain.AclRole;
import com.sinochem.yunlian.upm.admin.model.AuthResource;
import com.sinochem.yunlian.upm.admin.model.Menu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author zhangxi
 * @created 13-6-17
 */
@Service
public class AuthService {
    private static final Logger LOG = LoggerFactory.getLogger(AuthService.class);

    @Resource
    private UpmMapper upmMapper;
    @Resource
    private UpmMatcher upmMatcher;

    public String auth(String appkey, String userId, String resource) {
        AuthResource resources = null;
        if (resources == null || resources.getUrls(appkey) == null
                || resources.getUrls(appkey).isEmpty()) {
            List<String> urls = urls(appkey, userId);
            resources = (resources == null ? new AuthResource(userId) : resources);
            resources.add(appkey, urls);
        }
        List<String>  appUrls = Lists.newArrayList();
        appUrls.addAll(resources.getUrls(appkey));
        if (LOG.isDebugEnabled()) {
            LOG.debug("domain={} userId={} , appUrls.size={} , appUrls={}", new Object[] {userId, appUrls.size(), appUrls});
        } else {
            LOG.info("domain={} userId={} , appUrls.size={}", new Object[]{userId, appUrls.size()});
        }
        for (String url : appUrls) {
            if (upmMatcher.match(url, resource)) {
                return url;
            }
        }
        return "";
    }

    public boolean authPermission(String appkey, String userId, String p) {
        List<String> permissions = getPermission(appkey, userId);
        for (String permission : permissions) {
            if(StringUtil.isNotBlank(permission)){
                WildcardPermission wildcardPermission = new WildcardPermission(p);
                if(wildcardPermission.implies(new WildcardPermission(p))){
                    return true;
                }
            }
        }
        return false;
    }

    @Cacheable(cacheName = "LRUCache-5m", cacheNull = false)
    public List<String> getPermission(String appkey, String userId){
        return upmMapper.selectPermission(userId, appkey);
    }

    @Cacheable(cacheName = "LRUCache-5m", cacheNull = false)
    public List<String> urls(String appkey, String userId) {
        return trimUrlParameter(upmMapper.selectResource(userId, appkey));
    }

    @Cacheable(cacheName = "LRUCache-5m", cacheNull = false)
    public List<String> urls(String appkey) {
        return trimUrlParameter(upmMapper.selectAppResource(appkey));
    }

    private List<String> trimUrlParameter(List<String> resources){
        List<String> urls = new ArrayList<String>();
        if(!CollectionUtils.isEmpty(resources)){
            for(String resource : resources){
                if(resource!= null && resource.indexOf("?") > 0){
                    resource = resource.substring(0, resource.indexOf("?"));
                }
                urls.add(resource);
            }
        }
        return urls;
    }

    @Cacheable(cacheName = "LRUCache-5m", cacheNull = false)
    public List<Menu> menus(String appkey, String userId) {
        List<MenuItem> list = upmMapper.selectMenu(userId, appkey);
        return buildMenu(list);
    }

    @Cacheable(cacheName = "LRUCache-5m", cacheNull = false)
    public List<Menu> appMenus(String appkey) {
        List<MenuItem> list = upmMapper.selectAppMenu(appkey);
        return buildMenu(list);
    }

    public List<Menu> buildMenu(List<MenuItem> list) {
        if (list.isEmpty()) {
            return Lists.newArrayList();
        }

        String rootId = null;
        Map<String, Menu> menuMap = new HashMap<String, Menu>();
        Map<String, List<Menu>> subMenuMap = new HashMap<String, List<Menu>>();

        for (MenuItem am : list) {
            Menu menu = toMenu(am);
            menuMap.put(menu.getId(), menu);
            if(subMenuMap.get(menu.getParentId()) == null){
                subMenuMap.put(menu.getParentId(),new ArrayList<Menu>());
            }
            subMenuMap.get(menu.getParentId()).add(menu);
        }

        for (MenuItem am : list) {
            Menu menu = menuMap.get(am.getId());
            List<Menu> subMenus = subMenuMap.get(am.getId());
            if (!CollectionUtils.isEmpty(subMenus)) {
                menu.setMenus(subMenus);
            }
            if(menu.getParentId() == null){
                rootId = menu.getId();
            }
        }

        Menu rootMenu = menuMap.get(rootId);
        if(rootMenu != null && rootMenu.getMenus()!= null){
            List<Menu> menus = rootMenu.getMenus();
            //sortMenu(menus);
            return menus;
        }

        return Lists.newArrayList();
    }

    private Menu toMenu(MenuItem am) {
        Menu menu = new Menu();
        menu.setId(am.getId());
        menu.setParentId(am.getParentId());
        menu.setTitle(am.getTitle());
        menu.setUrl(am.getUrl());
        menu.setSort(am.getSortNum());
        menu.setType(am.getShowType());
        menu.setCreateTime(am.getCreateTime() != null ? TimeUtil.unixtime(am.getCreateTime()) : 0);
        return menu;
    }

    @Cacheable(cacheName = "LRUCache-5m", cacheNull = false)
    public List<String> selectRoleCodes(String appkey, String userId) {
        List<AclRole> aclRoles = upmMapper.selectRole(userId, appkey);
        List<String> codes = new ArrayList<String>();

        for (AclRole aclRole : aclRoles) {
            codes.add(aclRole.getCode());
        }
        return codes;
    }
}
