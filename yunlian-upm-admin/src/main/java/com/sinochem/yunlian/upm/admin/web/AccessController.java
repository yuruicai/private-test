package com.sinochem.yunlian.upm.admin.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("")
public class AccessController {

    @RequestMapping("/access/denied")
    public ModelAndView accessDenied() {
        return new ModelAndView("/access/denied");
    }
}
