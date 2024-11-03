
DROP TABLE IF EXISTS access;
create table access
(
    id         varchar(20)                            not null comment '主键'
        primary key,
    sessionId  varchar(50)                            not null comment '会话Id',
    userId     varchar(20)  default '2'               null comment '用户ID，2，表示游客访问',
    ip         varchar(50)                            null comment 'ip地址',
    createTime datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime     default CURRENT_TIMESTAMP null comment '更新时间',
    createBy   varchar(50)                            null comment '创建人',
    updateBy   varchar(50)                            null comment '更新人',
    version    int          default 0                 not null comment '版本号',
    del        tinyint(1)   default 0                 not null comment '删除标志',
    remark     varchar(255) default ''                null comment '备注'
)
    comment '用户访问记录表';

DROP TABLE IF EXISTS comment;
create table comment
(
    commentId  varchar(20)                            not null comment '评论ID'
        primary key,
    postId     varchar(20)                            not null comment '文章ID',
    content    text                                   not null comment '内容',
    status     tinyint(1)   default 0                 null comment '状态',
    createTime datetime                               null comment '创建时间',
    updateTime datetime     default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    createBy   varchar(50)                            null comment '创建人',
    updateBy   varchar(50)                            null comment '更新人',
    del        tinyint(1)   default 0                 null comment '删除标记',
    remark     varchar(255) default ''                null comment '备注',
    version    int          default 0                 null comment '版本号',
    userId     varchar(20)                            not null comment '用户Id'
);

DROP TABLE IF EXISTS feedback;
create table feedback
(
    feedbackId varchar(20)                            not null comment '设置ID'
        primary key,
    userId     varchar(20)  default '2'               null comment '用户Id 2 为游客',
    content    varchar(255)                           not null comment '反馈内容',
    createTime datetime                               null comment '创建时间',
    updateTime datetime     default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    createBy   varchar(50)                            null comment '创建人',
    updateBy   varchar(50)                            null comment '更新人',
    del        tinyint(1)   default 0                 null comment '删除标记',
    remark     varchar(255) default ''                null comment '备注',
    version    int          default 0                 null comment '版本号'
)
    comment '反馈意见表';

DROP TABLE IF EXISTS post;
create table post
(
    postId     varchar(20)                            not null comment '文章ID'
        primary key,
    userId     varchar(20)                            not null comment '用户ID',
    title      varchar(100)                           not null comment '标题',
    content    text                                   not null comment '内容',
    status     varchar(1)   default '0'               not null comment '状态 0 草稿，1 发布',
    createTime datetime                               null comment '创建时间',
    updateTime datetime     default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    createBy   varchar(50)                            null comment '创建人',
    updateBy   varchar(50)                            null comment '更新人',
    del        tinyint(1)   default 0                 null comment '删除标记',
    remark     varchar(255) default ''                null comment '备注',
    version    int          default 0                 null comment '版本号',
    viewCount  int          default 0                 not null comment '浏览量'
);

DROP TABLE IF EXISTS post_tag;
create table post_tag
(
    postId     varchar(20)                            not null comment '文章ID',
    tagId      varchar(20)                            not null comment '标签ID',
    createTime datetime                               null comment '创建时间',
    updateTime datetime     default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    createBy   varchar(50)                            null comment '创建人',
    updateBy   varchar(50)                            null comment '更新人',
    del        tinyint(1)   default 0                 null comment '删除标记',
    remark     varchar(255) default ''                null comment '备注',
    version    int          default 0                 null comment '版本号',
    primary key (postId, tagId)
);

DROP TABLE IF EXISTS role;
create table role
(
    roleId     varchar(20)                            not null comment '角色ID'
        primary key,
    roleName   varchar(50)                            not null comment '角色名称',
    `desc`     varchar(255)                           null comment '描述',
    createTime datetime                               null comment '创建时间',
    updateTime datetime     default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    createBy   varchar(50)                            null comment '创建人',
    updateBy   varchar(50)                            null comment '更新人',
    del        tinyint(1)   default 0                 null comment '删除标记',
    remark     varchar(255) default ''                null comment '备注',
    version    int          default 0                 null comment '版本号'
);


DROP TABLE IF EXISTS tag;
create table tag
(
    tagId      varchar(20)                            not null comment '标签ID'
        primary key,
    tagName    varchar(50)                            not null comment '标签名称',
    createTime datetime                               null comment '创建时间',
    updateTime datetime     default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    createBy   varchar(50)                            null comment '创建人',
    updateBy   varchar(50)                            null comment '更新人',
    del        tinyint(1)   default 0                 null comment '删除标记',
    remark     varchar(255) default ''                null comment '备注',
    version    int          default 0                 null comment '版本号'
);

DROP TABLE IF EXISTS user;
create table user
(
    userId     varchar(20)                            not null comment '用户ID'
        primary key,
    userName   varchar(50)                            not null comment '用户名',
    password   varchar(100)                           not null comment '密码',
    email      varchar(100)                           not null comment '邮箱',
    role       varchar(20)                            not null comment '角色',
    createTime datetime                               null comment '创建时间',
    updateTime datetime     default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    createBy   varchar(50)                            null comment '创建人',
    updateBy   varchar(50)                            null comment '更新人',
    del        tinyint(1)   default 0                 null comment '删除标记',
    remark     varchar(255) default ''                null comment '备注',
    version    int          default 0                 null comment '版本号'
);