package com.luckylau.easy.dynamic.tp.alert;

import com.luckylau.easy.dynamic.tp.common.em.NotifyType;
import com.luckylau.easy.dynamic.tp.common.util.DateUtil;
import lombok.Builder;
import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author luckylau
 * @Date 2022/5/31
 */
@Builder
@Data
public class AlertCounter {
    private final AtomicInteger counter = new AtomicInteger(0);
    private NotifyType type;
    private String lastAlarmTime;

    public void incCounter() {
        counter.incrementAndGet();
    }

    public void reset() {
        lastAlarmTime = DateUtil.now();
        counter.set(0);
    }

    public int getCount() {
        return counter.get();
    }

}
