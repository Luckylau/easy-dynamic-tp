package com.luckylau.easy.dynamic.tp.common.model;

import lombok.Data;

import java.util.concurrent.TimeUnit;

/**
 * @Author luckylau
 * @Date 2022/5/2
 */
@Data
public class DtpDesc {
    /**
     * Name of Dynamic ThreadPool.
     */
    private String threadPoolName;

    /**
     * CoreSize of ThreadPool.
     */
    private int corePoolSize;

    /**
     * MaxSize of ThreadPool.
     */
    private int maximumPoolSize;

    /**
     * When the number of threads is greater than the core,
     * this is the maximum time that excess idle threads
     * will wait for new tasks before terminating.
     */
    private long keepAliveTime;

    /**
     * Timeout unit.
     */
    private TimeUnit unit;

    private DtpQueue dtpQueue;

    private String rejectedHandlerType;

    public long getKeepAliveTime(TimeUnit unit) {
        return unit.convert(keepAliveTime, TimeUnit.NANOSECONDS);
    }
}
