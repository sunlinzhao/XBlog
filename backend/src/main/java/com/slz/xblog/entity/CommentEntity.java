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
 * @since 2024-10-26
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("comment")
@ApiModel(value = "CommentEntity对象", description = "")
public class CommentEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("评论ID")
    @TableId("commentId")
    private String commentId;

    @ApiModelProperty("文章ID")
    @TableField("postId")
    private String postId;

    @ApiModelProperty("用户ID")
    @TableField("userId")
    private String userId;

    @ApiModelProperty("内容")
    @TableField("content")
    private String content;

    @ApiModelProperty("状态")
    @TableField("status")
    private Boolean status;


}
