package com.sinochem.yunlian.upm.sso.service;

import com.sinochem.yunlian.upm.sso.domain.IpLog;
import com.sinochem.yunlian.upm.sso.domain.IpLogExample;
import com.sinochem.yunlian.upm.sso.mapper.IpLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 登錄日誌記錄
 */
@Service
@Slf4j
public class IpLogService {
    @Autowired
    private IpLogMapper ipLogMapper;

    public void addLog(IpLog ipLog) {
        int i = ipLogMapper.insertSelective(ipLog);
        if( !(i>0)){
            log.info("日志写入失败！");
        }
        log.info("ip={}  的用户登录！");
    }
}
