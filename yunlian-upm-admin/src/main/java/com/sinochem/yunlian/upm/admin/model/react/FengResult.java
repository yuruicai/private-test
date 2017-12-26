/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sinochem.yunlian.upm.admin.model.react;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Setter
@Getter
@ToString
public class FengResult<T>  implements Serializable {

    private T data = null;
    private List<FengError> error = new ArrayList<FengError>();

    public FengResult() {
    }

    public FengResult(T data) {
        this.data = data;
    }

    public FengResult(List<FengError> list) {
        this.error = list;
    }

    public boolean isSuccess() {
        return this.error.isEmpty();
    }

    public FengResult<T> addError(FengError error) {
        this.error.add(error);
        return this;
    }

    public FengResult<T> addError(String message, String type, String value) {
        this.error.add(new FengError(message, type, value));
        return this;
    }
    
    public FengResult<T> addError(FengErrorCode apiErrorCode, String type, String value) {
        this.error.add(new FengError(apiErrorCode.name(), type, value));
        return this;
    }
    
    public FengResult<T> addError(FengErrorCode apiErrorCode, String type) {
        this.error.add(new FengError(apiErrorCode.name(), type));
        return this;
    }
    
    public FengResult<T> addError(FengErrorCode apiErrorCode) {
        this.error.add(new FengError(apiErrorCode.name()));
        return this;
    }
}
