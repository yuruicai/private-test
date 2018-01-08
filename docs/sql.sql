CREATE TABLE `acl_user` (
  `id` varchar(64) NOT NULL COMMENT '主键ID',
  `type` smallint(5) DEFAULT NULL COMMENT '用户类型：1：货主、2：物流公司、3：车主',
  `code` varchar(30) DEFAULT NULL COMMENT '工号代码',
  `login_name` varchar(255) NOT NULL COMMENT '登录名',
  `source` varchar(255) NOT NULL COMMENT '用户来源',
  `email` varchar(255) DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(30) NOT NULL COMMENT '手机号码',
  `name` varchar(255) DEFAULT NULL COMMENT '用户真实姓名',
  `password` varchar(512) NOT NULL COMMENT '登录密码',
  `qq_code` varchar(30) DEFAULT NULL COMMENT 'qq号码',
  `org_id` varchar(64) DEFAULT NULL COMMENT 'org节点id(所属组织id)',
  `comment` varchar(255) DEFAULT NULL,
  `status` smallint(5) NOT NULL DEFAULT '1' COMMENT '0：待激活 1：启用 2：冻结 3：停用',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `id_code` varchar(30) DEFAULT NULL COMMENT '身份证',
  `salt` varchar(128) DEFAULT NULL COMMENT '随机盐',
  `password_update_time` datetime DEFAULT NULL COMMENT '修改密码时间',
  `head_image` varchar(100) DEFAULT '' COMMENT '头像地址',
  `gender` smallint(1) DEFAULT 0 COMMENT '性别：1-男，2-女',
  `create_uid` varchar(64) DEFAULT NULL COMMENT '创建者',
  `update_uid` varchar(64) DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`id`),
  UNIQUE KEY `login_name` (`login_name`),
  KEY `idx_update_time` (`update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;


CREATE TABLE `acl_role` (
  `id` varchar(64) NOT NULL COMMENT '主键ID',
  `code` varchar(64) NOT NULL COMMENT '角色编码',
  `name` varchar(255) NOT NULL COMMENT '角色名称',
  `status` smallint(5) NOT NULL DEFAULT '0' COMMENT '状态（0：正常、1：禁用）',
  `comment` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `role_type` int(11) unsigned DEFAULT '0' COMMENT '角色类型',
  `application_id` varchar(64) DEFAULT NULL COMMENT '所属应用',
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;


CREATE TABLE `acl_user_role_rlt` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_id` varchar(64) NOT NULL COMMENT '用户ID',
  `user_id` varchar(64) NOT NULL COMMENT '角色ID',
  `rlt_type` smallint(5) NOT NULL DEFAULT '1' COMMENT '定义角色与用户之间的关系类型： 1：拥有关系，用户拥有这个角色对应的权限 2：授权关系，用户可以将对应的角色授给任意其他人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` smallint(5) NOT NULL DEFAULT '0' COMMENT '状态（0：正常、1：禁用）',
  `comment` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_user_role` (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;


CREATE TABLE `acl_menu` (
  `id` varchar(64) NOT NULL,
  `parent_id` varchar(64) DEFAULT NULL ,
  `title` varchar(255) NOT NULL,
  `url` varchar(255) DEFAULT NULL,
  `sort_num` smallint(5) NOT NULL DEFAULT '9999',
  `show_type` smallint(5) NOT NULL DEFAULT '1' COMMENT '1：在原页面打开目标页面 2：在新页面打开目标页面',
  `is_show` smallint(5) DEFAULT NULL,
  `permission` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `status` smallint(5) NOT NULL DEFAULT '0',
  `operator_id` varchar(64) NOT NULL DEFAULT '0',
  `application_id` varchar(64) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_permission` (`permission`),
  KEY `idx_parent` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;


create table company(
  id  int AUTO_INCREMENT,
  company_name varchar(50) NOT NULL comment '公司名',
  address varchar(50) NOT NULL  comment '公司地址',
  logo_url varchar(100) comment 'logo地址',
  kind varchar(50) NOT NULL comment '公司性质',
  contacter varchar(50) comment '联系人',
  contacter_phone  varchar(20) comment '联系人电话',
  contacter_email varchar(100) comment '联系人邮箱',
  fax varchar(30) comment '公司传真',
  biz_scope  varchar(100) comment '营业范围',
  register_capital decimal(20,2) comment '注册资本',
  register_time datetime DEFAULT NULL comment '注册时间',
  register_address varchar(100) comment '注册地址',
  usc_code varchar(50) comment '统一社会信用代码',
  post_code varchar(20) comment '邮政编码',
  web_site varchar(50) comment '公司网址',
  legal_represent varchar(50) comment '法定代表人',
  legal_front_image varchar(100) comment '法人证件正面',
  legal_verso_image varchar(100) comment '法人证件反面',
  biz_licence_expire datetime DEFAULT NULL comment '营业执照失效日期',
  biz_licence_image varchar(100) comment '营业执照图片地址',
  province varchar(50) comment '',
  city varchar(50) comment '',
  area varchar(50) comment '',
  block varchar(50) comment '街道',
  status smallint(1) DEFAULT 0  COMMENT '认证状态：0-未认证，1-已认证，2：未通过',
  remark varchar(200) DEFAULT '' COMMENT '未通过原因',
  create_time timestamp DEFAULT CURRENT_TIMESTAMP  comment '创建时间' ,
  update_time timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间' ,
  primary key(id)
)ENGINE=InnoDB  DEFAULT CHARSET=utf8  COMMENT='公司信息表';