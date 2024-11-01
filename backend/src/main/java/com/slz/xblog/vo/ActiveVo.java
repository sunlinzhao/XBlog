package com.slz.xblog.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : SunLZ
 * @project : backend
 * @date : 2024/10/28
 */
@Data
@Accessors(chain = true)
public class ActiveVo {
    private String userId;
    private String ip;
    private Integer count;
}
