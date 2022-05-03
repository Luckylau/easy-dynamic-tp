package com.luckylau.easy.dynamic.tp.common.em;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author luckylau
 * @Date 2022/5/2
 */
@Slf4j
public enum QueueType {
    /**
     * BlockingQueue type.
     */
    ARRAY_BLOCKING_QUEUE(1, "ArrayBlockingQueue"),

    LINKED_BLOCKING_QUEUE(2, "LinkedBlockingQueue"),

    PRIORITY_BLOCKING_QUEUE(3, "PriorityBlockingQueue"),

    DELAY_QUEUE(4, "DelayQueue"),

    SYNCHRONOUS_QUEUE(5, "SynchronousQueue"),

    LINKED_TRANSFER_QUEUE(6, "LinkedTransferQueue"),

    LINKED_BLOCKING_DEQUE(7, "LinkedBlockingDeque"),

    VARIABLE_LINKED_BLOCKING_QUEUE(8, "VariableLinkedBlockingQueue");

    private final Integer code;
    private final String name;

    QueueType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
