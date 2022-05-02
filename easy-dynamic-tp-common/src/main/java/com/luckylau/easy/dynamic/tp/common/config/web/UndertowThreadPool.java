package com.luckylau.easy.dynamic.tp.common.config.web;

import lombok.Data;

/**
 * @Author luckylau
 * @Date 2022/5/2
 */
@Data
public class UndertowThreadPool {
    /**
     * Number of io threads.
     */
    private int ioThreads = 8;

    /**
     * Number of core worker threads, internal default the coreThreads = maxThreads
     */
    private int coreWorkerThreads = 8 * ioThreads;

    /**
     * Number of max worker threads
     */
    private int maxWorkerThreads = 8 * ioThreads;

    /**
     * Worker thread keep alive, unit s
     */
    private int workerKeepAlive;
}
