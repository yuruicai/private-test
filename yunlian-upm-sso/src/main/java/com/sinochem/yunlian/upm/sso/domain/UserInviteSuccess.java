package com.sinochem.yunlian.upm.sso.domain;

import java.util.Date;

public class UserInviteSuccess {
    private Long id;

    private Integer userId;

    private Integer beUserId;

    private Date createTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getBeUserId() {
        return beUserId;
    }

    public void setBeUserId(Integer beUserId) {
        this.beUserId = beUserId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}