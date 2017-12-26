/**
 * Copyright (C) 2015 Sankuai, Inc. All Rights Reserved.
 */
package com.sinochem.yunlian.upm.admin.model.react;

import java.io.Serializable;

/**
 * @author Gavin Yang
 * react 响应 数据结构  
 */
public class ReactResult<T > implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7338179722325370585L;
	private T data; //数据
	private int status;//数据状态
	
	
	public T getData() {
		return data;
	}


	public void setData(T data) {
		this.data = data;
	}


	public int getStatus() {
		return status;
	}


	public void setStatus(int status) {
		this.status = status;
	}


	public static <T> T newInstance(){
		return ReactResult.newInstance();
	}  

}
