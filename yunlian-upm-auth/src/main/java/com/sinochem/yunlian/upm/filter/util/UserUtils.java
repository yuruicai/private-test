package com.sinochem.yunlian.upm.filter.util;

import com.sinochem.yunlian.upm.filter.cache.Cache;
import com.sinochem.yunlian.upm.tools.Menu;
import com.sinochem.yunlian.upm.tools.UpmAuthService;
import com.sinochem.yunlian.upm.tools.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangxi
 * @created 13-6-19
 */
public class UserUtils {

    public static final String CACHE_NAME = "com.sinochem.yunlian.upm.filter.cache";

    private static final String THREAD_LOCAL_USER = "sinochem.yunlian_upm_auth_user";
    private static final String THREAD_LOCAL_MENUS = "sinochem.yunlian_upm_auth_menus";
    private static final String THREAD_LOCAL_PERMISSIONS = "sinochem.yunlian_upm_auth_permissions";
    private static final String THREAD_LOCAL_ROLES = "sinochem.yunlian_upm_auth_roles";

    private static final String CACHE_USER_KEY_PREFIX = "user_";
    private static final String CACHE_MENUS_KEY_PREFIX = "menus_";
    private static final String CACHE_PERMISSIONS_KEY_PREFIX = "permissions_";
    private static final String CACHE_ROLES_KEY_PREFIX = "roles_";

    private static final String ROLE_NAMES_DELIMETER = ",";
    private static final String PERMISSION_NAMES_DELIMETER = ",";



    public static User getUser(){
        return  (User) ThreadContext.get(THREAD_LOCAL_USER);
    }

    public static List<Menu> getMenus() {
        return (List<Menu>)ThreadContext.get(THREAD_LOCAL_MENUS);
    }

    public static User fetchUser(String sid){
        if(!StringUtils.hasText(sid)){
            return null;
        }
        //从threadlocal里取，如果存在说明已经加载，直接返回
        User user = (User) ThreadContext.get(THREAD_LOCAL_USER);
        if(user != null){
            return user;
        }

        //如果cache开启，直接从cache里边取，如果cache存在，则设置到threadlocal并返回
        if(ObjectHolder.getCacheManager() != null) {
            Cache<String, User> cache = ObjectHolder.getCacheManager().getCache(CACHE_NAME);
            user = cache.get(userKey(sid));
            if (user != null) {
                ThreadContext.put(THREAD_LOCAL_USER, user);
                return user;
            }
        }

        //调用接口从upm服务端获取，如果存在则刷新会threadlocal和cache，并返回
        UpmAuthService upmAuthService = ObjectHolder.getUpmAuthService();
        User temp = upmAuthService.getUser(sid);
        if (temp != null) {
            user = new User();
            user.setId(temp.getId());
            user.setLogin(temp.getLogin());
            user.setName(temp.getName());
            user.setType(temp.getType());

            ThreadContext.put(THREAD_LOCAL_USER, user);

            if(ObjectHolder.getCacheManager() != null) {
                Cache<String, User> cache = ObjectHolder.getCacheManager().getCache(CACHE_NAME);
                cache.put(userKey(sid), user);
            }
        }
        return user;
    }

    public static List<Menu> fetchMenus(){
        //判断用户是否存在
        User user = getUser();
        if(user == null || !StringUtils.hasText(user.getId())){
            return null;
        }
        //从threadlocal里取，如果存在说明已经加载，直接返回
        List<Menu> menus = (List<Menu>) ThreadContext.get(THREAD_LOCAL_MENUS);
        if(menus != null){
            return menus;
        }

        //从cache里取，如果存在则回写threadlocal并返回
        if(ObjectHolder.getCacheManager() != null) {
            Cache<String, List<Menu>> cache = ObjectHolder.getCacheManager().getCache(CACHE_NAME);
            menus = cache.get(menusKey(user.getId()));
            if (menus != null) {
                ThreadContext.put(THREAD_LOCAL_MENUS, menus);
                return menus;
            }
        }
        //调用接口加载
        UpmAuthService upmAuthService = ObjectHolder.getUpmAuthService();
        menus = upmAuthService.getMenus(upmAuthService.getClientId(), user.getId());
        if(menus != null){
            ThreadContext.put(THREAD_LOCAL_MENUS, menus);
            if(ObjectHolder.getCacheManager() != null) {
                Cache<String, List<Menu>> cache = ObjectHolder.getCacheManager().getCache(CACHE_NAME);
                cache.put(menusKey(user.getId()), menus);
            }
        }
        return menus;
    }


    public static Map<String, String> getRolesAsMap() {
        User user = getUser();
        if(user == null || !StringUtils.hasText(user.getId())){
            return new HashMap<String, String>();
        }

        //从threadlocal里取，如果存在说明已经加载，直接返回
        Map<String, String> rolesMap = (Map<String, String>) ThreadContext.get(THREAD_LOCAL_ROLES);
        if(rolesMap != null){
            return rolesMap;
        }

        //从cache里取，如果存在则回写threadlocal并返回
        if(ObjectHolder.getCacheManager() != null) {
            Cache<String, Map<String, String>> cache = ObjectHolder.getCacheManager().getCache(CACHE_NAME);
            rolesMap = cache.get(rolesKey(user.getId()));
            if (rolesMap != null) {
                ThreadContext.put(THREAD_LOCAL_ROLES, rolesMap);
                return rolesMap;
            }
        }

        //调用接口加载
        UpmAuthService upmAuthService = ObjectHolder.getUpmAuthService();
        List<String> roles = upmAuthService.getRoles(upmAuthService.getClientId(), getUser().getId());
        rolesMap = new HashMap<String, String>();
        if(roles != null){
            for(String role : roles){
                rolesMap.put(role, null);
            }

            ThreadContext.put(THREAD_LOCAL_ROLES, rolesMap);
            if(ObjectHolder.getCacheManager() != null) {
                Cache<String, Map<String, String>> cache = ObjectHolder.getCacheManager().getCache(CACHE_NAME);
                cache.put(rolesKey(user.getId()), rolesMap);
            }
        }
        return rolesMap;
    }

    public static List<WildcardPermission> getPermissions() {
        User user = getUser();
        if(user == null || !StringUtils.hasText(user.getId())){
            return new ArrayList<WildcardPermission>();
        }
        //先从threadlocal里取
        List<WildcardPermission> wildcardPermissions = (List<WildcardPermission>)ThreadContext.get(THREAD_LOCAL_PERMISSIONS);
        if(wildcardPermissions!=null){
            return wildcardPermissions;
        }
        //cache如果开启，从cache里取，如果存在则写回到threadlocal里
        if(ObjectHolder.getCacheManager() != null) {
            Cache<String, List<WildcardPermission>> cache = ObjectHolder.getCacheManager().getCache(CACHE_NAME);
            wildcardPermissions = cache.get(permissionKey(user.getId()));
            if (wildcardPermissions != null) {
                ThreadContext.put(THREAD_LOCAL_PERMISSIONS, wildcardPermissions);
                return wildcardPermissions;
            }
        }

        wildcardPermissions = new ArrayList<WildcardPermission>();
        UpmAuthService upmAuthService = ObjectHolder.getUpmAuthService();
        List<String> perms = upmAuthService.getPermissions(upmAuthService.getClientId(), getUser().getId());
        if(perms != null){
            for (String p : perms){
                if(StringUtils.hasText(p)) {
                    wildcardPermissions.add(new WildcardPermission(p));
                }
            }

            ThreadContext.put(THREAD_LOCAL_PERMISSIONS, wildcardPermissions);

            if(ObjectHolder.getCacheManager() != null) {
                Cache<String, List<WildcardPermission>> cache = ObjectHolder.getCacheManager().getCache(CACHE_NAME);
                cache.put(permissionKey(user.getId()), wildcardPermissions);
            }
        }
        return wildcardPermissions;
    }

    public static boolean hasPermission(String perm){
        List<WildcardPermission> permissions = getPermissions();
        if (CollectionUtils.isEmpty(permissions)) {
            return false;
        }
        for(WildcardPermission permission : permissions){
            if(permission.implies(new WildcardPermission(perm))){
                return true;
            }
        }
        return false;
    }

    public static boolean hasPermissions(String[] perms){
        if(perms == null || perms.length == 0){
            return false;
        }
        List<WildcardPermission> permissions = getPermissions();
        if (CollectionUtils.isEmpty(permissions)) {
            return false;
        }

        for(String perm : perms) {
            if(!hasPermission(perm)){
                return false;
            }
        }
        return true;
    }

    public static boolean hasPermissions(String perms){
        if(!StringUtils.hasText(perms)){
            return false;
        }
        String [] permsArray = perms.split(PERMISSION_NAMES_DELIMETER);
        return hasPermissions(permsArray);
    }

    public static boolean hasAnyPermissions(String[] perms){
        for (String permission : perms){
            if (UserUtils.hasPermission(permission)){
                return true;
            }
        }
        return false;
    }

    public static boolean hasAnyPermissions(String perms){
        if(!StringUtils.hasText(perms)){
            return false;
        }
        String [] permsArray = perms.split(PERMISSION_NAMES_DELIMETER);
        return hasAnyPermissions(permsArray);
    }

    public static boolean hasRole(String role){
        Map<String,String> rolesMap = getRolesAsMap();
        if(CollectionUtils.isEmpty(rolesMap)){
            return false;
        }
        if(rolesMap.containsKey(role)){
            return true;
        }
        return false;
    }

    public static boolean hasRoles(String[] roles){
        if(roles == null || roles.length == 0){
            return false;
        }
        Map<String, String> roleMap = getRolesAsMap();
        if(CollectionUtils.isEmpty(roleMap)){
            return false;
        }

        for (String role : roles){
            if(!roleMap.containsKey(role)){
                return false;
            }
        }
        return false;
    }

    public static boolean hasRoles(String roles){
        if(!StringUtils.hasText(roles)){
            return false;
        }
        String [] roleArray = roles.split(ROLE_NAMES_DELIMETER);
        return hasRoles(roleArray);
    }

    public static boolean hasAnyRoles(String[] roles){
        for (String role : roles) {
            if (UserUtils.hasRole(role)){
                return true;
            }
        }
        return false;
    }

    public static boolean hasAnyRoles(String roles){
        if(!StringUtils.hasText(roles)){
            return false;
        }
        String [] roleArray = roles.split(ROLE_NAMES_DELIMETER);
        return hasAnyRoles(roleArray);
    }

    private static String userKey(String sid){
        return CACHE_USER_KEY_PREFIX + sid;
    }

    private static String menusKey(String sid){
        return CACHE_MENUS_KEY_PREFIX + sid;
    }

    private static String permissionKey(String sid){
        return CACHE_PERMISSIONS_KEY_PREFIX + sid;
    }

    private static String rolesKey(String sid){
        return  CACHE_ROLES_KEY_PREFIX + sid;
    }

    public static void clean() {
        ThreadContext.remove(THREAD_LOCAL_USER);
        ThreadContext.remove(THREAD_LOCAL_MENUS);
        ThreadContext.remove(THREAD_LOCAL_PERMISSIONS);
        ThreadContext.remove(THREAD_LOCAL_ROLES);
    }
}
