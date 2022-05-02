package com.luckylau.easy.dynamic.tp.common.config.web;

import lombok.Data;

/**
 * @Author luckylau
 * @Date 2022/5/2
 */
@Data
public class TomcatThreadPool {

    /**
     * Maximum amount of worker threads.
     */
    private int max = 200;

    /**
     * Minimum amount of worker threads.
     */
    private int min = 10;
}
