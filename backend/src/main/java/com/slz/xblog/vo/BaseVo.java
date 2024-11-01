package com.slz.xblog.vo;

import lombok.Data;

/**
 * @author : SunLZ
 * @project : backend
 * @date : 2024/10/27
 */
@Data
public class BaseVo {
    private String userId;
    private int pageNum = 1;
    private int pageSize = 10;
}
