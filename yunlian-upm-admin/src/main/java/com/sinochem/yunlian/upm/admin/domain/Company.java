package com.sinochem.yunlian.upm.admin.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 企业信息实体
 */
@Data
public class Company {
    private Integer id;

    private String companyName;
    //公司地址
    private String address;

    private String logoUrl;

    //企业性质
    private String kind;

    //联系人
    private String contacter;

    private String contacterPhone;

    private String contacterEmail;

    private String fax;
    //营业范围
    private String bizScope;
    //注册资本
    private BigDecimal registerCapital;

    private Date registerTime;

    private String registerAddress;
    //统一社会信用编码
    private String uscCode;

    private String postCode;

    private String webSite;

    //法人代表
    private String legalRepresent;

    //法人身份证正面
    private String legalFrontImage;

    //法人身份证反面
    private String legalVersoImage;

    //营业执照过期日期
    private Date bizLicenceExpire;

    //营业执照图片地址
    private String bizLicenceImage;

    private String province;

    private String city;

    private String area;

    private String block;

    //认证状态：0-未认证，1-已认证，2：未通过
    private Short status;

    private String remark;

    private Date createTime;

}

