package com.sinochem.yunlian.upm.util;

import com.sinochem.yunlian.upm.admin.bean.Result;

import java.util.HashMap;
import java.util.List;
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

    public static Map<String, Object> success(Object data) {
        Map<String, Object> relMap = createAjaxSuccessMap();
        relMap.put("data", data);
        return relMap;
    }

    @Deprecated
    public static Map<String, Object> create(Object data) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", 1);
        result.put("data", data);
        return result;
    }
    @Deprecated
    public static Map<String, Object> create(List<TreeNode> roles) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", 1);
        result.put("data", roles);
        return result;
    }
    @Deprecated
    public static Map<String, Object> fail(String msg) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", 0);
        result.put("msg", msg);
        return result;
    }

    /**
     * 返回包含失败Result的Map
     */
    public static Map<String, Object> fail(int code, String type, String message) {
        Map<String, Object> relMap = new HashMap<String, Object>();
        Error error = new Error(code, type, message);
        relMap.put("error", error);
        return relMap;
    }



    static class Error {
        int code;
        String type;
        String message;

        public Error(int code, String type, String message) {
            this.code = code;
            this.type = type;
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
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
