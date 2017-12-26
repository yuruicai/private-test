/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sinochem.yunlian.upm.admin.model.react;

import lombok.Data;

import java.io.Serializable;

/**
 *
 */
@Data
public class FengError implements Serializable {
    
    /**
     *错误消息代码
     */
    private String message;
    /**
     * 错误类别，如果是用户提交表单产生的错误，可以给出产生错误对应的 input name，方便客户端提示
     */
    private String type;
    /**
     * 如果是用户提交表单产生的错误，给出产生错误对应的 input value
     */
    private String value;
    /**
     * 错误代码
     */
    private int code;

    public FengError() {
        this("", "", "", 0);
    }

    public FengError(final String message) {
        this(message, "", "", 0);
    }

    public FengError(final String message, final String type) {
        this(message, type, "", 0);
    }

    public FengError(final String message, final String type, final String value) {
        this(message, type, value, 0);
    }

    public FengError(final FengErrorCode errorCode, final String type, final String value) {
        this(errorCode.name(), type, value, 0);
    }
    
    public FengError(final String message, final String type, final String value, final int code) {
        this.message = message;
        this.type = type;
        this.value = value;
        this.code = code;
    }
}
