package com.luckylau.easy.dynamic.tp.common.config;

import lombok.Data;

import java.util.List;

/**
 * @Author luckylau
 * @Date 2022/5/29
 */
@Data
public class DtpAlert {
    private List<String> platforms;

    /**
     * If enabled notify.
     */
    private boolean enabled = true;

    /**
     * Alarm threshold.
     */
    private int threshold;

    /**
     * Alarm interval, time unit（s）
     */
    private int interval = 120;
}
