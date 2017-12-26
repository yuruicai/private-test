package com.sinochem.yunlian.upm.common.rpc;

import java.lang.reflect.Type;

/**
 * @author zhangxi
 * @created 13-2-18
 */
public interface InvokeAction {

    Response doInvoke(Request request, Type type, Invoker invoker);
}
