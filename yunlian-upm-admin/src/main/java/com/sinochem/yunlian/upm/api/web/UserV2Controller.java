package com.sinochem.yunlian.upm.api.web;

import com.sinochem.yunlian.upm.admin.bean.UserListBean;
import com.sinochem.yunlian.upm.util.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author huangyang
 * @Description: ${todo}(这里用一句话描述这个类的作用)
 * @date 2018/01/05 下午5:13
 */
@Controller
@RequestMapping("users")
public class UserV2Controller {

    @RequestMapping("list")
    @ResponseBody
    public Object userList(UserListBean userListBean, Page page){
        return null;
    }
}
