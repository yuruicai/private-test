package com.sinochem.yunlian.upm.util;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author zhangxi
 * @created 13-5-24
 */
public class TreeNode {
    private String name;
    private String id;
    private List<TreeNode> members = Lists.newArrayList();

    public TreeNode(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<TreeNode> getMembers() {
        return members;
    }

    public void setMembers(List<TreeNode> members) {
        this.members = members;
    }
}
