package com.luckylau.easy.dynamic.tp.common.alert;

import com.luckylau.easy.dynamic.tp.common.em.NotifyType;
import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author luckylau
 * @Date 2022/5/29
 */
@Data
public class DtpAlertIndicator {

    /**
     * Total reject count.
     */
    private final AtomicInteger rejectCount = new AtomicInteger(0);
    /**
     * Count run timeout tasks.
     */
    private final AtomicInteger runTimeoutCount = new AtomicInteger();
    /**
     * Count queue wait timeout tasks.
     */
    private final AtomicInteger queueTimeoutCount = new AtomicInteger();
    private String threadPoolName;

    public DtpAlertIndicator(String threadPoolName) {
        this.threadPoolName = threadPoolName;
    }

    public void indicator(NotifyType notifyType) {
        if (notifyType == NotifyType.QUEUE_TIMEOUT) {

        } else if (notifyType == NotifyType.RUN_TIMEOUT) {

        } else if (notifyType == NotifyType.REJECT) {

        }
    }


}
