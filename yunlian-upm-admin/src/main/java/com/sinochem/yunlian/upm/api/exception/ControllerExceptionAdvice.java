package com.sinochem.yunlian.upm.api.exception;

import com.sinochem.yunlian.upm.api.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author huangyang
 * @Description: 全局异常处理
 * @date 2017/12/28 下午2:45
 */
@ControllerAdvice
@Slf4j
public class ControllerExceptionAdvice {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response handleDefaultException(Exception ex) {
        log.error("系统异常", ex);
        return Response.fail("系统错误");

    }

    @ExceptionHandler({ApiException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response handleSsoException(ApiException ex) {
        return Response.fail(ex.getMessage());
    }


}
