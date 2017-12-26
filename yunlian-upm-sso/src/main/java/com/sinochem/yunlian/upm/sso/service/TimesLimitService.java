package com.sinochem.yunlian.upm.sso.service;

import com.sinochem.yunlian.upm.sso.model.TimesLimitInfo;
import com.sinochem.yunlian.upm.sso.cache.SsoCacheFacade;
import com.sinochem.yunlian.upm.sso.util.Constants;
import com.sinochem.yunlian.upm.sso.util.StringUtil;
import com.sinochem.yunlian.upm.sso.util.TraceContext;
import com.sinochem.yunlian.upm.sso.constant.TimesLimitConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created with IntelliJ IDEA.
 * User: zhanghongze
 * Date: 14-7-25
 * Time: 上午11:16
 * To change this template use File | Settings | File Templates.
 */
@Service
public class TimesLimitService {

    private static final Logger LOG = LoggerFactory.getLogger(TimesLimitService.class);

    private String keyPrefix = "sso.timeslimt."; //次数限制cache的key前缀

    private final static int EXPIRY_TIME = 5;//每天早晨5点过期

    private final static int IP_LOGIN_MAX_FAIL_TIMES = 100; //IP每天失败100次

    private static final int FREEZE_NOTICE_COUNT = 3;
    private static final int FREEZE_COUNT = 5;

    private static final long FREEZE_EFFECT_TIME = 1000 * 3600;
    public static final long FREEZE_NOTICE_EFFECT_TIME = 1000 * 3600 * 24;

    @Resource
    SsoCacheFacade ssoCacheFacade;

    private String buildKey(int type,String key1,String key2){
        if(type == TimesLimitConstant.IP){
            return keyPrefix + key1;
        }else if(type ==  TimesLimitConstant.USERNAME){
            return keyPrefix + key1;
        }else if(type == TimesLimitConstant.IP_USERNAME){
            return keyPrefix + key1 + key2;
        }else{
            return keyPrefix + key1;
        }
    }

    public static int getExpireSecond()
    {
        GregorianCalendar g = new GregorianCalendar();
        int nowHour = g.get(GregorianCalendar.HOUR_OF_DAY);
        g.set(GregorianCalendar.MINUTE, 0);
        g.set(GregorianCalendar.SECOND, 0);

        if(nowHour >= EXPIRY_TIME)
        {
            g.add(GregorianCalendar.DAY_OF_MONTH, 1);
        }
        g.set(GregorianCalendar.HOUR_OF_DAY, EXPIRY_TIME);
        int expireSecond = (int)((g.getTime().getTime()- System.currentTimeMillis())/1000);

        return expireSecond;
    }

    public TimesLimitInfo getTimesLimitInfo(int type, String key1, String key2){
        return ssoCacheFacade.getObject(buildKey(type, key1, key2), TimesLimitInfo.class);
    }

    public void setTimesLimitInfo(int type,String key1,String key2,TimesLimitInfo timesLimitInfo ){
        try{
            ssoCacheFacade.setObject(buildKey(type, key1, key2), getExpireSecond(), timesLimitInfo);
        }catch (Exception e){
           LOG.error("medis setTimesLimitInfo error", e);
        }
    }

    public boolean isIpLoginTimesOver(){
        try{
            String ip = (String) TraceContext.get(Constants.CONTEXT_IP_KEY);
            String username = (String) TraceContext.get(Constants.CONTEXT_USER_NAME);

            TimesLimitInfo timesLimitInfo = getTimesLimitInfo(TimesLimitConstant.IP, ip, null);
            if(timesLimitInfo != null){
                 if(timesLimitInfo.getFailLoginTimes() > IP_LOGIN_MAX_FAIL_TIMES){
                     LOG.error("ip {} login over times for user {}", new Object[]{ip,username});
                     return true;
                 }
            }
        }catch (Exception e){
            LOG.error("isIpLoginTimesOver error",  e);
        }

        return false;
    }

    public void increaseIpFailLoginTimes(){
        try{
            String ip = (String) TraceContext.get(Constants.CONTEXT_IP_KEY);

            TimesLimitInfo timesLimitInfo = getTimesLimitInfo(TimesLimitConstant.IP, ip, null);
            if(timesLimitInfo == null){
                timesLimitInfo = new TimesLimitInfo();
            }
            timesLimitInfo.setFailLoginTimes(timesLimitInfo.getFailLoginTimes() + 1);
            //如果最近5分钟有失败记录则加1
            if((System.currentTimeMillis() - timesLimitInfo.getFailLoginTimeStamp()) < 1000*60*5){
                timesLimitInfo.setFailLoginTimes5m(timesLimitInfo.getFailLoginTimes5m()+1);
            }else{
                timesLimitInfo.setFailLoginTimes5m(1);
            }
            timesLimitInfo.setLoginTimeStamp(System.currentTimeMillis());
            timesLimitInfo.setFailLoginTimeStamp(System.currentTimeMillis());
            setTimesLimitInfo(TimesLimitConstant.IP, ip, null, timesLimitInfo);
        }catch (Exception e){
            LOG.error("increaseIpFailLoginTimes error", e);
        }
    }

    public void increaseUsernameFailLoginTimes(String username){
        try{
            TimesLimitInfo timesLimitInfo = getTimesLimitInfo(TimesLimitConstant.USERNAME, username, null);
            if(timesLimitInfo == null){
                timesLimitInfo = new TimesLimitInfo();
            }
            timesLimitInfo.setFailLoginTimes(timesLimitInfo.getFailLoginTimes()+1);
            //如果最近5分钟有失败记录则加1
            if((System.currentTimeMillis() - timesLimitInfo.getFailLoginTimeStamp()) < 1000*60*5){
                timesLimitInfo.setFailLoginTimes5m(timesLimitInfo.getFailLoginTimes5m()+1);
            }else{
                timesLimitInfo.setFailLoginTimes5m(1);
            }
            timesLimitInfo.setLoginTimeStamp(System.currentTimeMillis());
            timesLimitInfo.setFailLoginTimeStamp(System.currentTimeMillis());

            setTimesLimitInfo(TimesLimitConstant.USERNAME, username, null, timesLimitInfo);
        }catch (Exception e){
            LOG.error("increaseIpFailLoginTimes error", e);
        }
    }

    public void increaseUsernameSuccessLoginTimes(String username){
        TimesLimitInfo timesLimitInfo = getTimesLimitInfo(TimesLimitConstant.USERNAME, username, null);
        if(timesLimitInfo == null){
            timesLimitInfo = new TimesLimitInfo();
        }
        timesLimitInfo.setLoginTimeStamp(System.currentTimeMillis());
        timesLimitInfo.setSuccessLoginTimes(timesLimitInfo.getSuccessLoginTimes() + 1);

        setTimesLimitInfo(TimesLimitConstant.USERNAME, username, null, timesLimitInfo);
    }

    public void removeUsernameTimesLimitInfo(String username){
        try{
            ssoCacheFacade.delete(buildKey(TimesLimitConstant.USERNAME, username, null));
        }catch (Exception e){
            LOG.error("medis setTimesLimitInfo error", e);
        }
    }

    public boolean executeIsFreeze(String username){
        if (StringUtil.isNotBlank(username)) {
            TimesLimitInfo timesLimitInfo = getTimesLimitInfo(TimesLimitConstant.USERNAME, username, null);
            if (timesLimitInfo != null && timesLimitInfo.getFailLoginTimes() >= FREEZE_COUNT
                    && (System.currentTimeMillis() - timesLimitInfo.getLoginTimeStamp()) < FREEZE_EFFECT_TIME) {
                LOG.info("retry >=8 in 1 hour, freeze login");
                return true;
            } else if (timesLimitInfo != null && timesLimitInfo.getFailLoginTimes() >= FREEZE_COUNT
                    && (System.currentTimeMillis() - timesLimitInfo.getLoginTimeStamp()) > FREEZE_EFFECT_TIME) {
                LOG.info("retry >=8 already after 1 hour, reset try_times");
                timesLimitInfo.setLoginTimeStamp(0);
                setTimesLimitInfo(TimesLimitConstant.USERNAME, username, null, timesLimitInfo);
            }
        }
        return false;

    }

    public int executeFreezeLimit(String username) {
        if (StringUtil.isNotBlank(username)) {
            TimesLimitInfo timesLimitInfo = getTimesLimitInfo(TimesLimitConstant.USERNAME, username, null);
            if (timesLimitInfo != null && (System.currentTimeMillis() - timesLimitInfo.getLoginTimeStamp()) > FREEZE_NOTICE_EFFECT_TIME) {
                timesLimitInfo.setFailLoginTimes(0);
                setTimesLimitInfo(TimesLimitConstant.USERNAME, username, null, timesLimitInfo);
            } else if (timesLimitInfo != null && timesLimitInfo.getFailLoginTimes() >= FREEZE_NOTICE_COUNT && (System.currentTimeMillis() - timesLimitInfo.getLoginTimeStamp()) < FREEZE_NOTICE_EFFECT_TIME) {
                LOG.info("retry >=5 in 24 hours, need freeze notice");
                return FREEZE_COUNT - timesLimitInfo.getFailLoginTimes();
            }
        }
        return -1;
    }

    public Date getTimesLimitExpireTime(int type,String key1,String key2){
        int seconds = ssoCacheFacade.ttl(buildKey(type, key1, key2));

        if(seconds >0){
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.SECOND, seconds);

            return cal.getTime();
        }

        return null;
    }

    public boolean isTimesOverNeedCaptcha(String username){
        try {
            TimesLimitInfo timesLimitInfo = getTimesLimitInfo(TimesLimitConstant.USERNAME, username, null);
            if(timesLimitInfo == null){
                return false;
            }
            if (timesLimitInfo.getFailLoginTimes() >= 2) {
                return true;
            }
            long interval = System.currentTimeMillis() - timesLimitInfo.getFailLoginTimeStamp();
            if (timesLimitInfo.getFailLoginTimes5m() >= 1 && interval < 1000 * 60 * 5) {
                return true;
            }
            return false;
        }catch (Exception e){
            LOG.error("isTimesOverNeedCaptcha fail "+username, e);
        }
        return false;
    }

    public boolean isTimesOverNeedMobile(String username){
        try {
            TimesLimitInfo timesLimitInfo = getTimesLimitInfo(TimesLimitConstant.USERNAME, username, null);
            if(timesLimitInfo == null){
                return false;
            }

            if (timesLimitInfo.getFailLoginTimes() >= 3) {
                return true;
            }
            long interval = System.currentTimeMillis() - timesLimitInfo.getFailLoginTimeStamp();
            if (timesLimitInfo.getFailLoginTimes5m() >= 2 && interval < 1000 * 60 * 5) {
                return true;
            }
            return false;
        }catch (Exception e){
            LOG.error("isTimesOverNeedCaptcha fail "+username, e);
        }
        return false;
    }

    public boolean isIpTimesOverNeedCaptcha(String ip){
        try {
            TimesLimitInfo timesLimitInfo = getTimesLimitInfo(TimesLimitConstant.IP, ip, null);
            if(timesLimitInfo == null){
                return false;
            }

            long interval = System.currentTimeMillis() - timesLimitInfo.getFailLoginTimeStamp();
            //5分钟内错误3次10分钟内必须出示图片验证码
            if (timesLimitInfo.getFailLoginTimes() >= 50) {
                return true;
            }
            if (timesLimitInfo.getFailLoginTimes5m() >= 3 && (interval < 1000 * 60 * 60)) {
                return true;
            }
            return false;
        }catch (Exception e){
            LOG.error("isTimesOverNeedCaptcha fail "+ip, e);
        }
        return false;
    }
}
