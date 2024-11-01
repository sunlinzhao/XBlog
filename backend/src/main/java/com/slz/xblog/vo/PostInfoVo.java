package com.slz.xblog.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author : SunLZ
 * @project : backend
 * @date : 2024/10/25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class PostInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String postId; // 文章ID
    private String title; // 标题
    private String content; // 内容
    private String userId; // 用户ID
    private String userName; // 用户名
    private String status; // 状态 0 草稿，1 已发布
    private String tagIds; // 标签
    private String tagNames; // 标签名称
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastUpdateTime; // 最后更新时间
    private String viewCount; // 浏览量
    private List<String> TagIdList; // getTagIdList() 有要添加相应字段，避免出现序列化时找不到字段的情况而报错

    private List<String> TagNameList;
    private String statusDisplay;


    public List<String> getTagIdList() {
        return tagIds != null ? Arrays.asList(tagIds.split(",")) : Collections.emptyList();
    }

    public List<String> getTagNameList() {
        return tagNames != null ? Arrays.asList(tagNames.split(",")) : Collections.emptyList();
    }

    public String getStatusDisplay() {
        return status != null ? (status.equals("0") ? "草稿" : "已发布") : "";
    }
}
