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
 * 反馈意见表
 * </p>
 *
 * @author slz
 * @since 2024-10-31
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("feedback")
@ApiModel(value = "FeedbackEntity对象", description = "反馈意见表")
public class FeedbackEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("设置ID")
    @TableId("feedbackId")
    private String feedbackId;

    @ApiModelProperty("用户Id 2 为游客")
    @TableField("userId")
    private String userId;

    @ApiModelProperty("反馈内容")
    @TableField("content")
    private String content;


}
