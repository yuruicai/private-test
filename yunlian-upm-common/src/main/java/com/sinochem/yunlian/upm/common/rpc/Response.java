package com.sinochem.yunlian.upm.common.rpc;

/**
 * @author zhangxi
 * @created 13-1-16
 */
public interface Response<T> {

    T getData();

    Paging getPaging();

    ErrorCode getError();
}
