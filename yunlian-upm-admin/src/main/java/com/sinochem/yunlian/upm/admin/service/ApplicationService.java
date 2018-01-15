package com.sinochem.yunlian.upm.admin.service;

//import com.sinochem.yunlian.upload.UploadClient;
import com.sinochem.yunlian.upm.admin.bean.AppManagerBean;
import com.sinochem.yunlian.upm.admin.bean.ApplicationBean;
import com.sinochem.yunlian.upm.admin.bean.ApplicationRoleBean;
import com.sinochem.yunlian.upm.admin.constant.CommonStatus;
import com.sinochem.yunlian.upm.admin.mapper.AclUserMapper;
import com.sinochem.yunlian.upm.admin.mapper.UpmMapper;
import com.sinochem.yunlian.upm.filter.util.CollectionUtils;
import com.sinochem.yunlian.upm.util.Page;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.sinochem.yunlian.upm.admin.mapper.AclApplicationMapper;
import com.sinochem.yunlian.upm.util.StringUtil;
import com.sinochem.yunlian.upm.admin.constant.Constants;
import com.sinochem.yunlian.upm.admin.constant.UserRoleSourceType;
import com.sinochem.yunlian.upm.admin.constant.UserStatus;
import com.sinochem.yunlian.upm.admin.domain.*;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;

/**
 * @author wangwei
 * @version 1.0
 * @created 2013-05-07
 */
@Service
public class ApplicationService {
    private static final Logger LOG = LoggerFactory.getLogger(ApplicationService.class);

    @Resource
    AclApplicationMapper aclApplicationMapper;
    @Resource
    private UserRoleService userRoleService;
    @Resource
    private RoleService roleService;
    @Resource
    private UpmMapper upmMapper;
    @Resource
    private RoleInstanceService roleInstanceService;
    @Resource
    private MenuService menuService;
    @Resource
    private AclUserMapper aclUserMapper;

    public static Map<String, AclApplication> APPLICATION_CACHE = new HashMap<String, AclApplication>();

    @PostConstruct
    @Scheduled(fixedRate = 300000)
    public void init(){
        List<AclApplication> applications = getList();
        Map<String, AclApplication> map = new HashMap<String, AclApplication>();
        if(!CollectionUtils.isEmpty(applications)){
            for (AclApplication application : applications){
                map.put(application.getAppkey(), application);
            }
        }
        APPLICATION_CACHE = map;
    }

    public String getSecretByAppkey(String appkey){
        AclApplication aclApplication = APPLICATION_CACHE.get(appkey);
        if(aclApplication == null){
            return null;
        }
        return aclApplication.getSecret();
    }


    public List<AclApplication> getList() {
        AclApplicationExample example = new AclApplicationExample();
        example.or().andStatusEqualTo((short) CommonStatus.ACTIVE.getIndex());
        return aclApplicationMapper.selectByExample(example);
    }

     public String getNameById(String id) {
        AclApplication app = null;
        if (StringUtil.isNotBlank(id)) {
            app = aclApplicationMapper.selectByPrimaryKey(id);
        }
        return app != null ? app.getName() : "";
    }

    public String getAppkeyById(String id) {
        AclApplication app = null;
        if (StringUtil.isNotBlank(id)) {
            app = aclApplicationMapper.selectByPrimaryKey(id);
        }
        return app != null ? app.getAppkey() : "";
    }

    public String getIdByAppkey(String appkey) {
        if (appkey == null) {
            return null;
        }
        AclApplicationExample example = new AclApplicationExample();
        example.or().andAppkeyEqualTo(appkey);
        List<AclApplication> list = aclApplicationMapper.selectByExample(example);
        if (list.size() > 1) {
            LOG.warn("more then one record for " + appkey);
        }
        return list.isEmpty() ? null : list.get(0).getId();
    }

    public void saveOrUpdate(AclApplication application) {
        if (application == null || StringUtil.isBlank(application.getAppkey())) {
            return;
        }

        if(StringUtil.isNotBlank(application.getId())){
            aclApplicationMapper.updateByPrimaryKeySelective(application);
        }else {
            application.setId(UUID.randomUUID().toString());
            application.setSecret(UUID.randomUUID().toString());
            application.setStatus((short)CommonStatus.ACTIVE.getIndex());
            aclApplicationMapper.insertSelective(application);
            menuService.initAppTopMenu(application.getId());
        }
    }

    public void delete(String id){
        AclApplication application = new AclApplication();
        application.setId(id);
        application.setStatus((short) CommonStatus.DELETE.getIndex());

        aclApplicationMapper.updateByPrimaryKeySelective(application);
    }
    public List<ApplicationBean> getApplicationBean(Page page) {
        RowBounds rowBounds = new RowBounds(page.getStart(), page.getPageSize());
        AclApplicationExample aclApplicationExample = new AclApplicationExample();
        aclApplicationExample.setOrderByClause("id");
        aclApplicationExample.or().andStatusEqualTo((short)CommonStatus.ACTIVE.getIndex());
        List<AclApplication> apps = aclApplicationMapper.selectByExample(aclApplicationExample);
        //page.setTotalCount(aclApplicationMapper.countByExample(aclApplicationExample));
        if (apps != null){
            page.setTotalCount(apps.size());
        }
        return Lists.transform(apps, new Function<AclApplication, ApplicationBean>() {
            @Override
            public ApplicationBean apply(AclApplication input) {
                ApplicationBean bean = new ApplicationBean();
                bean.setId(input.getId());
                bean.setName(input.getName());
                bean.setRoleId(input.getRoleId());
                bean.setRoleName(roleService.getNameById(bean.getRoleId()));
                bean.setAppKey(input.getAppkey());
                bean.setSecret(input.getSecret());
                bean.setUrl(input.getUrl());
                return bean;
            }
        });
    }

    public ApplicationBean getApplicationBean(String appId) {
        AclApplication app = getById(appId);
        if (null == app) {
            return null;
        }
        ApplicationBean bean = new ApplicationBean();
        bean.setId(app.getId());
        bean.setName(app.getName());
        bean.setRoleId(app.getRoleId());
        bean.setRoleName(roleService.getNameById(app.getRoleId()));
        bean.setAppKey(app.getAppkey());
        bean.setSecret(app.getSecret());
        bean.setUrl(app.getUrl());
        return bean;
    }

    public List<AclApplication> getByIds(List<String> ids) {
        AclApplicationExample aclApplicationExample = new AclApplicationExample();
        aclApplicationExample.or().andIdIn(ids);
        return aclApplicationMapper.selectByExample(aclApplicationExample);
    }

    public AclApplication getById(String id) {
        if (id == null) {
            return null;
        }
        return aclApplicationMapper.selectByPrimaryKey(id);
    }

    public void updateRole(String appId, String roleId) {
        AclApplication aclApplication = getById(appId);
        String oldRoleId = aclApplication.getRoleId();
        if(StringUtil.isNotBlank(roleId)){
            aclApplication.setRoleId(roleId);
        }else{
            aclApplication.setRoleId(null);
        }
        aclApplicationMapper.updateByPrimaryKey(aclApplication);
        LOG.info("update default role of application: appId=" + appId + " newRoleId=" + roleId + " oldRoleId=" + oldRoleId);

        ApplicationRoleBean applicationRoleBean = new ApplicationRoleBean();
        applicationRoleBean.setAppId(aclApplication.getId());
        applicationRoleBean.setOldRoleId(oldRoleId);
        applicationRoleBean.setNewRoleId(roleId);

        if (oldRoleId != null && !oldRoleId.equals(0)) {
            List<AclUser> users = userRoleService.getUsersHaveRole(oldRoleId);
            for (AclUser user : users) {
                applicationRoleBean.setUserId(user.getId());
            }

        }
    }

    public Set<String> getAllDefaultRoles() {
        List<AclApplication> aclApplications = getList();
        Set<String> ids = new HashSet<String>();
        for (AclApplication aclApplication : aclApplications) {
            if (StringUtil.isNotBlank(aclApplication.getRoleId())) {
                ids.add(aclApplication.getRoleId());
            }
        }
        return ids;
    }

    /*
    *查询指定应用所有管理员列表
     */
    public List<AppManagerBean> getAppManager(String appId) {
        List<AppManagerBean> managerBeans = upmMapper.selectAppManagers(appId);
        return managerBeans;
    }

    /**
     * 增加应用管理员
     * @param appId
     * @param userId
     */
    public String addAppManager(String appId, String userId){
        String upmId = getIdByAppkey(Constants.APPLICATION_UPM);
        AclRole appmRole = roleService.getByApplicationIdAndCode(upmId, Constants.APPM);
        if(appmRole == null){
            return "应用管理员角色不存在";
        }
        String rltId = userRoleService.addRltByUserAndRole(userId, appmRole.getId(), appmRole.getId(), UserRoleSourceType.APP.getIndex());

        AclRoleInstanceContext roleInstanceContext = new AclRoleInstanceContext();
        roleInstanceContext.setUserRoleRltId(rltId);
        roleInstanceContext.setApplicationId(appId);

        List<AclRoleInstanceContext> instanceContexts = roleInstanceService.getInstanceList(roleInstanceContext);
        if(!CollectionUtils.isEmpty(instanceContexts)){
            return "该用户已经是应用管理员";
        }

        roleInstanceService.addInstance(roleInstanceContext);
        return null;
    }

    /**
     * 删除应用管理员
     * @param appId
     * @param userId
     */
    public void deleteAppManager(String appId, String userId){
        String upmId = getIdByAppkey(Constants.APPLICATION_UPM);
        AclRole appmRole = roleService.getByApplicationIdAndCode(upmId, Constants.APPM);
        if(appmRole == null){
            return;
        }
        AclUserRoleRlt rlt = userRoleService.getRltByUserAndRole(userId, appmRole.getId());
        if(rlt == null){
            return;
        }

        AclRoleInstanceContext roleInstanceContext = new AclRoleInstanceContext();
        roleInstanceContext.setUserRoleRltId(rlt.getId());
        roleInstanceContext.setApplicationId(appId);
        roleInstanceService.deleteInstance(roleInstanceContext);

        //如果已经没有应用管理的应用，则删除应用管理员角色与用户的关联
        List<AclRoleInstanceContext> roleInstanceContexts = roleInstanceService.selectByRltId(rlt.getId());
        if(CollectionUtils.isEmpty(roleInstanceContexts)){
            userRoleService.deleteRltByRltId(rlt.getId());
        }
    }

    public void initDefaultRole(){
        AclUserExample example = new AclUserExample();
        example.or().andStatusEqualTo((short) UserStatus.ACTIVE.getIndex());
        List<AclUser> aclUsers = aclUserMapper.selectByExample(example);
        for(AclUser aclUser : aclUsers){
            userRoleService.addByAppAuto(aclUser.getId());
        }
    }

    public List<ApplicationBean> selectApps4Manager(String userId, Page page){
        List<AclApplication> apps = upmMapper.selectApps4Manager(userId);
        if (apps != null){
            page.setTotalCount(apps.size());
        }
        return Lists.transform(apps, new Function<AclApplication, ApplicationBean>() {
            @Override
            public ApplicationBean apply(AclApplication input) {
                ApplicationBean bean = new ApplicationBean();
                bean.setId(input.getId());
                bean.setName(input.getName());
                bean.setRoleId(input.getRoleId());
                bean.setRoleName(roleService.getNameById(bean.getRoleId()));
                bean.setAppKey(input.getAppkey());
                bean.setSecret(input.getSecret());
                bean.setUrl(input.getUrl());
                return bean;
            }
        });
    }

    public String uploadImage(String appId, String imageId, MultipartFile image) throws Exception{
//        String imageRelativePath = UploadClient.uploadImage(UploadClient.toByteArray(image.getInputStream()), image.getOriginalFilename());
//        AclApplication aclApplication = new AclApplication();
//        aclApplication.setId(appId);
//        String imageUrl = fengjrPicUrl + imageRelativePath;
//        if("image1".equals(imageId)){
//            aclApplication.setImage1(imageUrl);
//        }else{
//            aclApplication.setImage2(imageUrl);
//        }
//        aclApplicationMapper.updateByPrimaryKeySelective(aclApplication);

//        return imageUrl;
        // TODO: 2017/10/28 更换图片上传api
        return "";
    }
}
