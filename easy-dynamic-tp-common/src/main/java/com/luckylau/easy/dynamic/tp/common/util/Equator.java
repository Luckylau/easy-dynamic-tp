package com.luckylau.easy.dynamic.tp.common.util;

import java.util.List;

/**
 * @Author luckylau
 * @Date 2022/5/2
 */
public interface Equator {
    /**
     * 两个对象是否全相等
     *
     * @param first  对象1
     * @param second 对象2
     * @return 两个对象是否全相等
     */
    boolean isEquals(Object first, Object second);

    /**
     * 获取不相等的属性
     *
     * @param first  对象1
     * @param second 对象2
     * @return 不相等的属性，键为属性名，值为属性类型
     */
    List<FieldInfo> getDiffFields(Object first, Object second);
}
