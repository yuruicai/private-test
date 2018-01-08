package com.sinochem.yunlian.upm.api.vo;

import com.sinochem.yunlian.upm.admin.bean.UserListBean;
import com.sinochem.yunlian.upm.admin.domain.AclUser;
import com.sinochem.yunlian.upm.util.Page;
import lombok.Data;

import java.util.List;

/**
 * User  model
 */
@Data
public class PageInfo<T> {
    //当前页
    private int curPage;
    //分页单位
    private int pageSize;
    //总页数
    private int totalPages;
    //全纪录
    private int totalRecords;
    //data
    private List<T> data;


    public PageInfo() {
    }

    public PageInfo(int totalRecords, List<T> data) {
        this.totalRecords = totalRecords;
        this.data = data;
    }

    public PageInfo(int curPage, int pageSize, int totalPages, int totalRecords, List<T> data) {
        this.curPage = curPage;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
        this.totalRecords = totalRecords;
        this.data = data;
    }

}
