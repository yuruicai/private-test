package com.sinochem.yunlian.upm.util;

import org.apache.commons.io.FileUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.*;

@Component
public class ConstantUtil{

    private static final Logger log = LoggerFactory.getLogger(ConstantUtil.class);

    @Autowired
    WebApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        supportEnumInFreemarker();
    }

    private void supportEnumInFreemarker() {
        String webAppRealPath = applicationContext.getServletContext().getRealPath("/") ;
        File includeFile = new File(webAppRealPath
                + "/WEB-INF/views/helper/includeHelper.ftl");
        Reflections reflections = new Reflections("com.sinochem.yunlian.upm.admin.constant");
        try {
            Set<Class<? extends Enum>> allClasses = reflections.getSubTypesOf(Enum.class);
            log.info("constant:" + allClasses);
            String inputStr = "";
            for (Class clazz : allClasses) {
                String enumName = clazz.getSimpleName();
                String enumValue = clazz.getName();
                inputStr += "<#assign " + enumName + "=statics[\"" + enumValue + "\"]>\r\n";
            }
            log.info("begin create " + includeFile.getAbsolutePath());
            FileUtils.writeStringToFile(includeFile, inputStr, "UTF8", false);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
