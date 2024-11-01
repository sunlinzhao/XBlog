package com.slz.xblog.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author : SunLZ
 * @project : Blog
 * @date : 2024/10/21
 */
@ApiModel(value = "实体类基类", description = "所有实体类需要继承此类，包含基本的公共字段")
@Data
public class BaseEntity {
    @ApiModelProperty(value = "创建人")
    @TableField(value = "createBy", fill = FieldFill.INSERT)
    private String createBy;
    @ApiModelProperty(value = "修改人")
    @TableField(value = "updateBy", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;
    @TableField(value = "createTime", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @TableField(value = "updateTime", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "修改时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    @ApiModelProperty(value = "删除状态")
    @TableField(value = "del")
    private Integer del;
    @ApiModelProperty(value = "备注")
    @TableField(value = "remark", exist = false)
    private String remark;
    @ApiModelProperty(value = "版本号")
    @TableField(value = "version")
    @Version
    private Integer version;
}
