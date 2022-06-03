package com.luckylau.easy.dynamic.tp.common.em;

/**
 * @Author luckylau
 * @Date 2022/5/29
 */
public enum NotifyType {
    /**
     * Config change notify.
     */
    CHANGE("change"),

    /**
     * ThreadPool liveliness notify.
     * liveliness = activeCount / maximumPoolSize
     */
    LIVELINESS("liveliness"),

    /**
     * Capacity threshold notify
     */
    CAPACITY("capacity"),

    /**
     * Reject notify.
     */
    REJECT("reject"),

    /**
     * Task run timeout alarm.
     */
    RUN_TIMEOUT("run_timeout"),

    /**
     * Task queue wait timeout alarm.
     */
    QUEUE_TIMEOUT("queue_timeout");

    private final String value;

    NotifyType(String value) {
        this.value = value;
    }

    public static NotifyType of(String value) {
        for (NotifyType type : NotifyType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        return null;
    }

}
