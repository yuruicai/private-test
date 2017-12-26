package com.sinochem.yunlian.upm.sso.service;

import com.sinochem.yunlian.upm.sso.cache.SsoCacheFacade;
import com.google.common.base.Joiner;
import nl.captcha.Captcha;
import nl.captcha.text.renderer.DefaultWordRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.awt.*;
import java.util.*;

/**
 * @author wangwei
 * @version 1.0
 * @created 2013-08-13
 */
@Service
public class SsoCaptchaService {
    private static final Logger LOG = LoggerFactory.getLogger(SsoCaptchaService.class);

    @Resource
    private SsoCacheFacade ssoCacheFacade;

    private static int _width = 150;
    private static int _height = 50;
    private static final java.util.List<Color> COLORS = new ArrayList();
    private static final java.util.List<Font> FONTS = new ArrayList(3);

    static {
        COLORS.add(Color.CYAN);
        COLORS.add(Color.magenta);
        COLORS.add(Color.DARK_GRAY);
        COLORS.add(Color.PINK);
        FONTS.add(new Font("Geneva", 0, 48));
        FONTS.add(new Font("Courier", 2, 48));
        FONTS.add(new Font("Arial", 2, 48));
    }

    public Boolean validateResponseForID(String captchaId, String captcha) {
        Boolean flag = false;
        String cap = getCaptchaString(captchaId);
        if (!"".equals(cap) && cap.equals(captcha)) {
            flag = true;
        } else {
            LOG.info(Joiner.on(",").useForNull("null").join(captchaId, cap, captcha));
        }
        removeCaptcha(captchaId);
        return flag;
    }

    public Captcha generateAndStoreCaptcha(String ID) {
        DefaultWordRenderer wordRenderer = new DefaultWordRenderer(COLORS, FONTS);
        Captcha captcha = (new Captcha.Builder(_width, _height)).addText(wordRenderer).gimp().addNoise().build();
        storeCaptcha(ID, captcha.getAnswer());
        return captcha;
    }

    private String keyPrefix = "sso.captcha";

    private static final Logger log = LoggerFactory.getLogger(MemCaptchaStore.class);

    public void storeCaptcha(String id, String cap){
        ssoCacheFacade.set((buildKey(id)), cap, 120);
        log.info("captcha gen {}, id {}", cap, id);
    }

    public String getCaptchaString(String id) {
        String cap = ssoCacheFacade.get(buildKey(id));
        if (cap == null) {
            cap = "";
        }
        log.info("captcha get {}, id {}", cap, id);
        return cap;
    }

    public boolean removeCaptcha(String id) {
        ssoCacheFacade.delete(buildKey(id));
        return true;
    }

    private String buildKey(String id) {
        return keyPrefix + id;
    }

}
