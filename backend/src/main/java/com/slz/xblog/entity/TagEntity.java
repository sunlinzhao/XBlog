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
@TableName("tag")
@ApiModel(value = "TagEntity对象", description = "")
public class TagEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("标签ID")
    @TableId("tagId")
    private String tagId;

    @ApiModelProperty("标签名称")
    @TableField("tagName")
    private String tagName;


}
