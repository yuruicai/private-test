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
public class UserVo {

    private long total;
    private List rows;


    public UserVo() {
    }

    public UserVo(long total, List rows) {
        this.total = total;
        this.rows = rows;
    }
    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }


}
