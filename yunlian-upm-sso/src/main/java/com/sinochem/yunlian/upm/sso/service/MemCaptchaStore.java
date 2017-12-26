package com.sinochem.yunlian.upm.sso.service;

import com.sinochem.yunlian.upm.sso.cache.SsoCacheFacade;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author wangwei
 * @version 1.0
 * @created 2013-08-05
 */
@Component
public class MemCaptchaStore {

    @Resource
    private SsoCacheFacade ssoCacheFacade;


}
