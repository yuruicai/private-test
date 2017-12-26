package com.sinochem.yunlian.upm.admin.bean;

/**
 * ajax请求时,contronller返回的结果.
 *
 * @author wangwei
 * @version 1.0
 * @created 2013-05-06
 */

import java.io.Serializable;

public class Result implements Serializable {

    private static final long serialVersionUID = -4886455731442337867L;

    /**
     * 请求结果是否成功
     */
    private boolean isSuccess = false;

    /**
     * 返回的信息
     */
    private String msg;

    public Result() {
    }

    public Result(boolean isSuccess, String msg) {
        this.isSuccess = isSuccess;
        this.msg = msg;
    }

    public Result(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public boolean getIsSuccess() {
        return isSuccess;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * 成功
     */
    public void success() {
        this.isSuccess = true;
    }

    /**
     * 失败
     */
    public void failure() {
        this.isSuccess = false;
    }

}