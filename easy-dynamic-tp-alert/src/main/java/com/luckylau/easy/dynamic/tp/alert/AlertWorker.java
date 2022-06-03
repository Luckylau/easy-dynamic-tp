package com.luckylau.easy.dynamic.tp.alert;

import com.google.common.collect.Lists;
import com.luckylau.easy.dynamic.tp.common.DtpExecutor;
import com.luckylau.easy.dynamic.tp.common.DtpRegistry;
import com.luckylau.easy.dynamic.tp.common.config.DtpProperties;
import com.luckylau.easy.dynamic.tp.common.em.NotifyType;
import com.luckylau.easy.dynamic.tp.common.util.NameThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author luckylau
 * @Date 2022/5/31
 */
@Slf4j
public class AlertWorker implements ApplicationRunner, Ordered {

    private static final List<NotifyType> ALARM_TYPES = Lists.newArrayList(NotifyType.LIVELINESS, NotifyType.CAPACITY);

    private static final ScheduledExecutorService ALERT_EXECUTOR = new ScheduledThreadPoolExecutor(
            1, new NameThreadFactory("alert-worker", true));


    @Resource
    private DtpProperties dtpProperties;

    @Override
    public void run(ApplicationArguments args) {
        ALERT_EXECUTOR.scheduleWithFixedDelay(this::run, 0, dtpProperties.getDtpAlert().getInterval()
                , TimeUnit.SECONDS);
        log.info("alert-worker start success");
    }

    private void run() {
        List<DtpExecutor> dtpExecutors = DtpRegistry.listAllDtpExecutors();
        dtpExecutors.forEach(x -> {
            AlertManager.getInstance().doAlert(x, ALARM_TYPES);
        });
    }


    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 2;
    }
}
