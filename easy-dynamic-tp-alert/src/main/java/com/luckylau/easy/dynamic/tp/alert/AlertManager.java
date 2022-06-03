package com.luckylau.easy.dynamic.tp.alert;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.luckylau.easy.dynamic.tp.common.DtpExecutor;
import com.luckylau.easy.dynamic.tp.common.config.DtpProperties;
import com.luckylau.easy.dynamic.tp.common.em.NotifyType;
import com.luckylau.easy.dynamic.tp.common.model.DtpDesc;
import com.luckylau.easy.dynamic.tp.common.util.ApplicationContextHolder;
import com.luckylau.easy.dynamic.tp.common.util.NameThreadFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @Author luckylau
 * @Date 2022/5/29
 */
@Slf4j
public class AlertManager {

    private static Cache<String, NotifyType> ALERT_LIMITER;

    private static Map<String, AlertCounter> ALERT_COUNT_CACHE = new ConcurrentHashMap<>();

    private final ExecutorService ALARM_EXECUTOR = new ThreadPoolExecutor(2, 2, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(), NameThreadFactory.create("alert-manager", false));

    private AlertManager() {
        DtpProperties dtpProperties = ApplicationContextHolder.getBean(DtpProperties.class);
        int interval = dtpProperties.getDtpAlert().getInterval();
        ALERT_LIMITER = CacheBuilder.newBuilder()
                .expireAfterWrite(interval, TimeUnit.SECONDS)
                .build();
    }

    public static AlertManager getInstance() {
        return AlertManagerHolder.INSTANCE;
    }

    public void doAlert(DtpExecutor executor, List<NotifyType> notifyTypes) {
        notifyTypes.forEach(x -> doAlert(executor, x));
    }

    public void doAlert(DtpExecutor executor, NotifyType notifyType) {
        DtpDesc dtpDesc = executor.getDtpDesc();
        String threadPoolName = dtpDesc.getThreadPoolName();
        alertCounter(threadPoolName, notifyType);
        ALARM_EXECUTOR.execute(() -> {
            if (!dtpDesc.isAlertEnabled()) {
                return;
            }
            if (!ifAlert(threadPoolName, notifyType)) {
                return;
            }
            AlertCounter alertCounter = getCounter(threadPoolName, notifyType);


        });
    }

    private void alertCounter(String threadPoolName, NotifyType notifyType) {
        String key = buildKey(threadPoolName, notifyType);
        AlertCounter alertCounter = ALERT_COUNT_CACHE.get(key);
        if (alertCounter == null) {
            alertCounter = AlertCounter.builder().type(notifyType).build();
            alertCounter.incCounter();
            ALERT_COUNT_CACHE.put(key, alertCounter);
        }
    }

    private AlertCounter getCounter(String threadPoolName, NotifyType notifyType) {
        String key = buildKey(threadPoolName, notifyType);
        return ALERT_COUNT_CACHE.get(key);
    }

    private String buildKey(String threadPoolName, NotifyType notifyType) {
        return threadPoolName + ":" + notifyType.name();
    }

    private synchronized boolean ifAlert(String threadPoolName, NotifyType notifyType) {
        String key = buildKey(threadPoolName, notifyType);
        NotifyType cache = ALERT_LIMITER.getIfPresent(key);
        if (cache == null) {
            ALERT_LIMITER.put(key, notifyType);
            return false;
        }
        return true;
    }

    private static class AlertManagerHolder {
        private static final AlertManager INSTANCE = new AlertManager();
    }


}
