package com.slz.xblog.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : SunLZ
 * @project : backend
 * @date : 2024/10/30
 */
@Data
public class TagVo implements Serializable { // 可序列化，便于存入缓存
    private static final long serialVersionUID = 1L; // 推荐添加一个 serialVersionUID

    private String tagId;
    private String tagName;
    private Integer postNum; // 标签下的文章数量
}
