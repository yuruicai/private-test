package com.sinochem.yunlian.upm.sso.util;

import com.sinochem.yunlian.upm.sso.bean.Result;

import java.util.HashMap;
import java.util.Map;

/**
 * ajax return map
 */
final public class AjaxResultUtil {

    /**
     * 返回包含成功Result的Map
     */
    public static Map<String, Object> createAjaxSuccessMap() {
        Map<String, Object> relMap = new HashMap<String, Object>();
        Result rel = new Result(true);
        relMap.put("result", rel);
        relMap.put("status", 200);
        return relMap;
    }

    /**
     * 返回包含成功Result的Map
     */
    public static Map<String, Object> createAjaxSuccessMap(String msg) {
        Map<String, Object> relMap = new HashMap<String, Object>();
        Result rel = new Result(true, msg);
        relMap.put("result", rel);
        relMap.put("status", 200);
        return relMap;
    }

    /**
     * 返回包含失败Result的Map
     */
    public static Map<String, Object> createAjaxFailureMap(String msg) {
        Map<String, Object> relMap = new HashMap<String, Object>();
        Result rel = new Result(false, msg);
        relMap.put("result", rel);
        return relMap;
    }

    public static Map<String, Object> create(Object data) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", 1);
        result.put("data", data);
        return result;
    }

    public static Map<String, Object> fail(String msg) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", 0);
        result.put("msg", msg);
        return result;
    }
    public static Map<String, Object> resultAjax(String msg,int status,Object data) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", status);
        result.put("msg", msg);
        result.put("data",data);
        return result;
    }

    public static Map<String, Object> resultSuccessAjax(Object data) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", 20000);
        result.put("msg", "");
        result.put("data",data);
        return result;
    }
}
