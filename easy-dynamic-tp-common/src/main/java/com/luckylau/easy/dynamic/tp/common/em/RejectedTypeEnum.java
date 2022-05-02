package com.luckylau.easy.dynamic.tp.common.em;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author luckylau
 * @Date 2022/5/2
 */
@Slf4j
public enum RejectedTypeEnum {
    /**
     * RejectedExecutionHandler type while triggering reject policy.
     */
    ABORT_POLICY("AbortPolicy"),

    CALLER_RUNS_POLICY("CallerRunsPolicy"),

    DISCARD_OLDEST_POLICY("DiscardOldestPolicy"),

    DISCARD_POLICY("DiscardPolicy");

    private final String name;

    RejectedTypeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
