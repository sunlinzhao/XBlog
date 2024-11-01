package com.slz.xblog.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 统计用户上次活跃时间
 * </p>
 *
 * @author slz
 * @since 2024-10-28
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("access")
@ApiModel(value = "AccessEntity对象", description = "用户访问记录表")
public class AccessEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId("Id")
    private String Id;

    @ApiModelProperty("用户Id")
    @TableField("userId")
    private String userId;

    @ApiModelProperty("会话Id")
    @TableField("sessionId")
    private String sessionId;

    @ApiModelProperty("ip地址")
    @TableField("ip")
    private String ip;
}
