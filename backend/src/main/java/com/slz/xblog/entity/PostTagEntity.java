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
@TableName("post_tag")
@ApiModel(value = "PostTagEntity对象", description = "")
public class PostTagEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("文章ID")
    @TableField("postId")
    private String postId;

    @ApiModelProperty("标签ID")
    @TableField("tagId")
    private String tagId;


}
