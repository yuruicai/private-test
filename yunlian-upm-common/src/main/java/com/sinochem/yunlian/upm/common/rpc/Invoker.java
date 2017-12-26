package com.sinochem.yunlian.upm.common.rpc;

import java.lang.reflect.Type;

/**
 * @author zhangxi
 * @created 13-1-24
 */
public interface Invoker {

    <T> Response<T> invoke(Request request, Type type);

    <T> Response<T> invokeDirect(Request request, Type type);
}
