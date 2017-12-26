package com.sinochem.yunlian.upm.admin.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhangxi
 * @created 13-7-1
 */
public class Menu implements Serializable {
    private String id;
    private String parentId;
    private String title;
    private String url;
    private String code;
    private int sort; // 菜单项顺序，越小越前
    private int type; // '1：在原页面打开目标页面 2：在新页面打开目标页面'
    private int createTime;
    private List<Menu> menus; // 子菜单

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Menu");
        sb.append("{id=").append(id);
        sb.append(", title='").append(title).append('\'');
        sb.append(", url='").append(url).append('\'');
        sb.append(", code='").append(code).append('\'');
        sb.append(", sort=").append(sort);
        sb.append(", type=").append(type);
        sb.append(", createTime=").append(createTime);
        sb.append(", menus=").append(menus);
        sb.append('}');
        return sb.toString();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }
}
