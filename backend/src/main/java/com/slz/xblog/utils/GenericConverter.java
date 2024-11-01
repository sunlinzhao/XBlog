package com.slz.xblog.utils;

import org.springframework.beans.BeanUtils;

/**
 * @author : SunLZ
 * @project : backend
 * @date : 2024/10/22
 */
public class GenericConverter {
    /**
     * 将源对象转换为目标对象
     *
     * @param source 源对象
     * @param targetClass 目标对象的类类型
     * @param <S> 源对象类型
     * @param <T> 目标对象类型
     * @return 目标对象
     */
    public static <S, T> T convert(S source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }
        try {
            T target = targetClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(source, target);
            return target;
        } catch (Exception e) {
            throw new RuntimeException("转换失败", e);
        }
    }
}
