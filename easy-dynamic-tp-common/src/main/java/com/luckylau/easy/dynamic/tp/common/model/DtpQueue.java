package com.luckylau.easy.dynamic.tp.common.model;

import com.luckylau.easy.dynamic.tp.common.em.QueueType;
import lombok.Data;

/**
 * @Author luckylau
 * @Date 2022/5/2
 */
@Data
public class DtpQueue {
    private QueueType queueType;
    private int capacity;
    private boolean fair;

    public DtpQueue(QueueType queueType, int capacity) {
        this.queueType = queueType;
        this.capacity = capacity;
    }

    public DtpQueue(QueueType queueType) {
        this.queueType = queueType;
        this.capacity = Integer.MAX_VALUE;
    }

    public DtpQueue(boolean fair) {
        this.queueType = QueueType.SYNCHRONOUS_QUEUE;
        this.fair = fair;
    }

    public DtpQueue(int capacity, boolean fair) {
        this.queueType = QueueType.ARRAY_BLOCKING_QUEUE;
        this.fair = fair;
        this.capacity = capacity;
    }
}
