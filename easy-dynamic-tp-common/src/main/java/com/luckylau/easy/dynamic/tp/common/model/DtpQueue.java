package com.luckylau.easy.dynamic.tp.common.model;

import com.luckylau.easy.dynamic.tp.common.em.QueueTypeEnum;
import lombok.Data;

/**
 * @Author luckylau
 * @Date 2022/5/2
 */
@Data
public class DtpQueue {
    private QueueTypeEnum queueTypeEnum;
    private int capacity;
    private boolean fair;

    public DtpQueue(QueueTypeEnum queueTypeEnum, int capacity) {
        this.queueTypeEnum = queueTypeEnum;
        this.capacity = capacity;
    }

    public DtpQueue(boolean fair) {
        this.queueTypeEnum = QueueTypeEnum.SYNCHRONOUS_QUEUE;
        this.fair = fair;
    }
}
