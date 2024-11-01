-- 字典类型表
DROP TABLE IF EXISTS dict_type;
CREATE TABLE dict_type
(
    typeId     VARCHAR(20) PRIMARY KEY COMMENT '字典类型ID',
    typeName   VARCHAR(50) NOT NULL COMMENT '字典类型名称',
    createTime DATETIME     DEFAULT NULL COMMENT '创建时间',
    updateTime DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    createBy   VARCHAR(50)  DEFAULT NULL COMMENT '创建人',
    updateBy   VARCHAR(50) COMMENT '更新人',
    del        TINYINT(1)   DEFAULT 0 COMMENT '删除标记',
    remark     VARCHAR(255) DEFAULT '' COMMENT '备注',
    version    INT  DEFAULT 0 COMMENT '版本号'
);

-- 字典表
DROP TABLE IF EXISTS dict;
CREATE TABLE dict
(
    dictId     VARCHAR(20) PRIMARY KEY COMMENT '字典ID',
    typeId     VARCHAR(20) NOT NULL COMMENT '字典类型ID',
    value      VARCHAR(50) NOT NULL COMMENT '字典值',
    `desc`     VARCHAR(255) COMMENT '字典描述',
    createTime DATETIME     DEFAULT NULL COMMENT '创建时间',
    updateTime DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    createBy   VARCHAR(50)  DEFAULT NULL COMMENT '创建人',
    updateBy   VARCHAR(50) COMMENT '更新人',
    del        TINYINT(1)   DEFAULT 0 COMMENT '删除标记',
    remark     VARCHAR(255) DEFAULT '' COMMENT '备注',
    version    INT  DEFAULT 0 COMMENT '版本号'
);

-- 用户表
DROP TABLE IF EXISTS user;
CREATE TABLE user
(
    userId     VARCHAR(20) PRIMARY KEY COMMENT '用户ID',
    userName   VARCHAR(50)  NOT NULL COMMENT '用户名',
    password   VARCHAR(100) NOT NULL COMMENT '密码',
    email      VARCHAR(100) NOT NULL COMMENT '邮箱',
    role       VARCHAR(20)  NOT NULL COMMENT '角色',
    createTime DATETIME     DEFAULT NULL COMMENT '创建时间',
    updateTime DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    createBy   VARCHAR(50)  DEFAULT NULL COMMENT '创建人',
    updateBy   VARCHAR(50) COMMENT '更新人',
    del        TINYINT(1)   DEFAULT 0 COMMENT '删除标记',
    remark     VARCHAR(255) DEFAULT '' COMMENT '备注',
    version    INT  DEFAULT 0 COMMENT '版本号'
);

-- 文章表
DROP TABLE IF EXISTS post;
CREATE TABLE post
(
    postId     VARCHAR(20) PRIMARY KEY COMMENT '文章ID',
    userId     VARCHAR(20)  NOT NULL COMMENT '用户ID',
    title      VARCHAR(100) NOT NULL COMMENT '标题',
    content    TEXT         NOT NULL COMMENT '内容',
    status     tinyint(1)   DEFAULT 0 COMMENT '状态',
    createTime DATETIME     DEFAULT NULL COMMENT '创建时间',
    updateTime DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    createBy   VARCHAR(50)  DEFAULT NULL COMMENT '创建人',
    updateBy   VARCHAR(50) COMMENT '更新人',
    del        TINYINT(1)   DEFAULT 0 COMMENT '删除标记',
    remark     VARCHAR(255) DEFAULT '' COMMENT '备注',
    version    INT  DEFAULT 0 COMMENT '版本号'
);

-- 分类表
DROP TABLE IF EXISTS cate;
CREATE TABLE cate
(
    cateId     VARCHAR(20) PRIMARY KEY COMMENT '分类ID',
    cateName   VARCHAR(50) NOT NULL COMMENT '分类名称',
    `desc`     VARCHAR(255) COMMENT '描述',
    createTime DATETIME     DEFAULT NULL COMMENT '创建时间',
    updateTime DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    createBy   VARCHAR(50)  DEFAULT NULL COMMENT '创建人',
    updateBy   VARCHAR(50) COMMENT '更新人',
    del        TINYINT(1)   DEFAULT 0 COMMENT '删除标记',
    remark     VARCHAR(255) DEFAULT '' COMMENT '备注',
    version    INT  DEFAULT 0 COMMENT '版本号'
);

-- 文章-分类关联表
DROP TABLE IF EXISTS post_cate;
CREATE TABLE post_cate
(
    postId     VARCHAR(20) NOT NULL COMMENT '文章ID',
    cateId     VARCHAR(20) NOT NULL COMMENT '分类ID',
    createTime DATETIME     DEFAULT NULL COMMENT '创建时间',
    updateTime DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    createBy   VARCHAR(50)  DEFAULT NULL COMMENT '创建人',
    updateBy   VARCHAR(50) COMMENT '更新人',
    del        TINYINT(1)   DEFAULT 0 COMMENT '删除标记',
    remark     VARCHAR(255) DEFAULT '' COMMENT '备注',
    version    INT  DEFAULT 0 COMMENT '版本号',
    PRIMARY KEY (postId, cateId)
);

-- 评论表
DROP TABLE IF EXISTS comment;
CREATE TABLE comment
(
    commentId   VARCHAR(20) PRIMARY KEY COMMENT '评论ID',
    postId      VARCHAR(20) NOT NULL COMMENT '文章ID',
    userId      VARCHAR(20) NOT NULL COMMENT '用户ID',
    content     TEXT        NOT NULL COMMENT '内容',
    commentDate DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
    status      tinyint(1)           DEFAULT 0 COMMENT '状态',
    createTime  DATETIME             DEFAULT NULL COMMENT '创建时间',
    updateTime  DATETIME             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    createBy    VARCHAR(50)          DEFAULT NULL COMMENT '创建人',
    updateBy    VARCHAR(50) COMMENT '更新人',
    del         TINYINT(1)           DEFAULT 0 COMMENT '删除标记',
    remark      VARCHAR(255)         DEFAULT '' COMMENT '备注',
    version    INT  DEFAULT 0 COMMENT '版本号'
);

-- 标签表
DROP TABLE IF EXISTS tag;
CREATE TABLE tag
(
    tagId      VARCHAR(20) PRIMARY KEY COMMENT '标签ID',
    tagName    VARCHAR(50) NOT NULL COMMENT '标签名称',
    createTime DATETIME     DEFAULT NULL COMMENT '创建时间',
    updateTime DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    createBy   VARCHAR(50)  DEFAULT NULL COMMENT '创建人',
    updateBy   VARCHAR(50) COMMENT '更新人',
    del        TINYINT(1)   DEFAULT 0 COMMENT '删除标记',
    remark     VARCHAR(255) DEFAULT '' COMMENT '备注',
    version    INT  DEFAULT 0 COMMENT '版本号'
);

-- 文章-标签关联表
DROP TABLE IF EXISTS post_tag;
CREATE TABLE post_tag
(
    postId     VARCHAR(20) NOT NULL COMMENT '文章ID',
    tagId      VARCHAR(20) NOT NULL COMMENT '标签ID',
    createTime DATETIME     DEFAULT NULL COMMENT '创建时间',
    updateTime DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    createBy   VARCHAR(50)  DEFAULT NULL COMMENT '创建人',
    updateBy   VARCHAR(50) COMMENT '更新人',
    del        TINYINT(1)   DEFAULT 0 COMMENT '删除标记',
    remark     VARCHAR(255) DEFAULT '' COMMENT '备注',
    version    INT  DEFAULT 0 COMMENT '版本号',
    PRIMARY KEY (postId, tagId)
);

-- 设置表
DROP TABLE IF EXISTS setting;
CREATE TABLE setting
(
    settingId    VARCHAR(20) PRIMARY KEY COMMENT '设置ID',
    settingName  VARCHAR(100) NOT NULL COMMENT '设置项',
    settingValue VARCHAR(255) NOT NULL COMMENT '设置值',
    createTime   DATETIME     DEFAULT NULL COMMENT '创建时间',
    updateTime   DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    createBy     VARCHAR(50)  DEFAULT NULL COMMENT '创建人',
    updateBy     VARCHAR(50) COMMENT '更新人',
    del          TINYINT(1)   DEFAULT 0 COMMENT '删除标记',
    remark       VARCHAR(255) DEFAULT '' COMMENT '备注',
    version    INT  DEFAULT 0 COMMENT '版本号'
);

-- 角色表
DROP TABLE IF EXISTS role;
CREATE TABLE role
(
    roleId     VARCHAR(20) PRIMARY KEY COMMENT '角色ID',
    roleName   VARCHAR(50) NOT NULL COMMENT '角色名称',
    `desc`     VARCHAR(255) COMMENT '描述',
    createTime DATETIME     DEFAULT NULL COMMENT '创建时间',
    updateTime DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    createBy   VARCHAR(50)  DEFAULT NULL COMMENT '创建人',
    updateBy   VARCHAR(50) COMMENT '更新人',
    del        TINYINT(1)   DEFAULT 0 COMMENT '删除标记',
    remark     VARCHAR(255) DEFAULT '' COMMENT '备注',
    version    INT  DEFAULT 0 COMMENT '版本号'
);

-- 权限表
DROP TABLE IF EXISTS auth;
CREATE TABLE auth
(
    authId     VARCHAR(20) PRIMARY KEY COMMENT '权限ID',
    authName   VARCHAR(50) NOT NULL COMMENT '权限名称',
    `desc`     VARCHAR(255) COMMENT '描述',
    createTime DATETIME     DEFAULT NULL COMMENT '创建时间',
    updateTime DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    createBy   VARCHAR(50)  DEFAULT NULL COMMENT '创建人',
    updateBy   VARCHAR(50) COMMENT '更新人',
    del        TINYINT(1)   DEFAULT 0 COMMENT '删除标记',
    remark     VARCHAR(255) DEFAULT '' COMMENT '备注',
    version    INT  DEFAULT 0 COMMENT '版本号'
);

-- 角色-权限关联表
DROP TABLE IF EXISTS role_auth;
CREATE TABLE role_auth
(
    roleId     VARCHAR(20) NOT NULL COMMENT '角色ID',
    authId     VARCHAR(20) NOT NULL COMMENT '权限ID',
    createTime DATETIME     DEFAULT NULL COMMENT '创建时间',
    updateTime DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    createBy   VARCHAR(50)  DEFAULT NULL COMMENT '创建人',
    updateBy   VARCHAR(50) COMMENT '更新人',
    del        TINYINT(1)   DEFAULT 0 COMMENT '删除标记',
    remark     VARCHAR(255) DEFAULT '' COMMENT '备注',
    version    INT  DEFAULT 0 COMMENT '版本号',
    PRIMARY KEY (roleId, authId)
);

-- 用户-角色关联表
DROP TABLE IF EXISTS user_role;
CREATE TABLE user_role
(
    userId     VARCHAR(20) NOT NULL COMMENT '用户ID',
    roleId     VARCHAR(20) NOT NULL COMMENT '角色ID',
    createTime DATETIME     DEFAULT NULL COMMENT '创建时间',
    updateTime DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    createBy   VARCHAR(50)  DEFAULT NULL COMMENT '创建人',
    updateBy   VARCHAR(50) COMMENT '更新人',
    del        TINYINT(1)   DEFAULT 0 COMMENT '删除标记',
    remark     VARCHAR(255) DEFAULT '' COMMENT '备注',
    version    INT  DEFAULT 0 COMMENT '版本号',
    PRIMARY KEY (userId, roleId)
);

-- 页面表
DROP TABLE IF EXISTS page;
CREATE TABLE page
(
    pageId       VARCHAR(20) PRIMARY KEY COMMENT '页面ID',
    title        VARCHAR(100) NOT NULL COMMENT '页面标题',
    content      TEXT         NOT NULL COMMENT '页面内容',
    creationDate DATETIME     DEFAULT NULL COMMENT '创建日期',
    createTime   DATETIME     DEFAULT NULL COMMENT '创建时间',
    updateTime   DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    createBy     VARCHAR(50)  DEFAULT NULL COMMENT '创建人',
    updateBy     VARCHAR(50) COMMENT '更新人',
    del          TINYINT(1)   DEFAULT 0 COMMENT '删除标记',
    remark       VARCHAR(255) DEFAULT '' COMMENT '备注',
    version    INT  DEFAULT 0 COMMENT '版本号'
);

-- 菜单表
DROP TABLE IF EXISTS menu;
CREATE TABLE menu
(
    menuId     VARCHAR(20) PRIMARY KEY COMMENT '菜单ID',
    menuName   VARCHAR(100) NOT NULL COMMENT '菜单名称',
    pageId     VARCHAR(20)  NOT NULL COMMENT '页面ID',
    parentId   VARCHAR(20)  DEFAULT NULL COMMENT '父菜单ID',
    sortOrder  INT          DEFAULT 0 COMMENT '排序',
    createTime DATETIME     DEFAULT NULL COMMENT '创建时间',
    updateTime DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    createBy   VARCHAR(50)  DEFAULT NULL COMMENT '创建人',
    updateBy   VARCHAR(50) COMMENT '更新人',
    del        TINYINT(1)   DEFAULT 0 COMMENT '删除标记',
    remark     VARCHAR(255) DEFAULT '' COMMENT '备注',
    version    INT  DEFAULT 0 COMMENT '版本号'
);

-- 系统访问记录表
DROP TABLE IF EXISTS access;
CREATE TABLE access
(
    logId      VARCHAR(20) PRIMARY KEY COMMENT '访问记录ID',
    userId     VARCHAR(20)  NOT NULL COMMENT '用户ID',
    accessTime DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '访问时间',
    ipAddress  VARCHAR(45)  NOT NULL COMMENT 'IP地址',
    action     VARCHAR(255) NOT NULL COMMENT '访问动作',
    createTime DATETIME              DEFAULT NULL COMMENT '创建时间',
    updateTime DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    createBy   VARCHAR(50)           DEFAULT NULL COMMENT '创建人',
    updateBy   VARCHAR(50) COMMENT '更新人',
    del        TINYINT(1)            DEFAULT 0 COMMENT '删除标记',
    remark     VARCHAR(255)          DEFAULT '' COMMENT '备注',
    version    INT  DEFAULT 0 COMMENT '版本号'
);


-- 插入基本数据
-- TODO
