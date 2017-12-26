package com.sinochem.yunlian.upm.tools;

/**
 * @author zhangxi
 * @created 13-1-16
 */
interface Response<T> {

    T getData();

    Paging getPaging();

    ErrorCode getError();
}
