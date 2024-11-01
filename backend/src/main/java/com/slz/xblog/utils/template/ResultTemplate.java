package com.slz.xblog.utils.template;

import com.slz.xblog.constant.CodeConstant;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : SunLZ
 * @project : Blog
 * @date : 2024/10/21
 */
@Data
@Accessors(chain = true)
public class ResultTemplate { // 封装结果模板
    private Boolean success;
    private Integer code;
    private String msg;
    private Map<String, Object> data;

    public static ResultTemplate success(){
        return new ResultTemplate().setSuccess(true)
                .setCode(CodeConstant.RESPONSE_CODE_SUCCESS)
                .setMsg("ok")
                .setData(new HashMap<>());
    }
    public static ResultTemplate error(){
        return new ResultTemplate().setSuccess(false)
                .setCode(CodeConstant.RESPONSE_CODE_FAILURE)
                .setMsg("fail")
                .setData(new HashMap<>());
    }
    public ResultTemplate setData(Map<String, Object> data){
        this.data = data;
        return this;
    }
    public ResultTemplate setData(String key, Object value){
        this.data.put(key, value);
        return this;
    }
}
