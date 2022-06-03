package com.luckylau.easy.dynamic.tp.common.util;

import org.apache.commons.lang3.time.FastDateFormat;

import java.util.Date;

/**
 * @Author luckylau
 * @Date 2022/6/3
 */
public class DateUtil {

    public static final FastDateFormat NORM_DATETIME_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");

    public static String now() {
        return formatDateTime(new Date());
    }

    public static String formatDateTime(Date date) {
        return null == date ? null : NORM_DATETIME_FORMAT.format(date);
    }
}
