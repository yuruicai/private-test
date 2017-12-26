package com.sinochem.yunlian.upm.admin.model;

import java.io.Serializable;
import java.util.*;

/**
 * @author zhangxi
 * @created 13-6-21
 */
public class AuthResource implements Serializable {
    private static final long serialVersionUID = 5371914386591401691L;
    private String key;
    private Map<String, Set<String>> resources = new HashMap<String, Set<String>>();

    public AuthResource() {
    }

    public AuthResource(String key) {
        this.key = key;
    }

    public AuthResource add(String appkey, Collection<String> urls) {
        Set<String> resource = resources.get(appkey);
        if (resource == null) {
            resource = new HashSet();
        }
        resource.clear();
        resource.addAll(urls);
        resources.put(appkey, resource);
        return this;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Map<String, Set<String>> getResources() {
        return resources;
    }

    public void setResources(Map<String, Set<String>> resources) {
        this.resources = resources;
    }

    public Set<String> getUrls(String appkey) {
        Set<String> resource = resources.get(appkey);
        if (resource == null) {
            resource = new HashSet();
        }
        return resource;
    }
}
