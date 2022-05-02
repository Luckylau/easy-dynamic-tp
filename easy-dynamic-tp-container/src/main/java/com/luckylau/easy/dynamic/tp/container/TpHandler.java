package com.luckylau.easy.dynamic.tp.container;

import com.luckylau.easy.dynamic.tp.common.config.DtpChangeConfig;
import com.luckylau.easy.dynamic.tp.common.config.DtpProperties;

import java.util.concurrent.Executor;

/**
 * @Author luckylau
 * @Date 2022/5/2
 */
public interface TpHandler {
    /**
     * Get specify thread pool.
     *
     * @return the specify executor
     */
    Executor getTp();

    /**
     * Update thread pool with specify properties.
     *
     * @param dtpChangeConfig the targeted dtpProperties
     */
    void updateTp(DtpChangeConfig dtpChangeConfig);

    /**
     * Update thread pool with specify properties.
     *
     * @param dtpProperties the targeted dtpProperties
     */
    void updateTp(DtpProperties dtpProperties);
}
