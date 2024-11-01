package com.slz.xblog.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.slz.xblog.entity.BaseEntity;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author slz
 * @since 2024-10-25
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("post")
@ApiModel(value = "PostEntity对象", description = "")
public class PostEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("文章ID")
    @TableId("postId")
    private String postId;

    @ApiModelProperty("用户ID")
    @TableField("userId")
    private String userId;

    @ApiModelProperty("标题")
    @TableField("title")
    private String title;

    @ApiModelProperty("内容")
    @TableField("content")
    private String content;

    @ApiModelProperty("状态 0 草稿，1 发布")
    @TableField("status")
    private String status;

    @ApiModelProperty("浏览量")
    @TableField("viewCount")
    private String viewCount;

}
