package com.luckylau.easy.dynamic.tp.core.refresh;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luckylau.easy.dynamic.tp.common.config.DtpChangeConfig;
import com.luckylau.easy.dynamic.tp.core.DtpRegistry;
import com.luckylau.easy.dynamic.tp.core.event.RefreshEvent;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ApplicationEventMulticaster;

import javax.annotation.Resource;

/**
 * @Author luckylau
 * @Date 2022/5/2
 */
@Slf4j
public class AbstractRefresher implements Refresher {

    private static final ObjectMapper MAPPER;

    static {
        MAPPER = new ObjectMapper();
    }

    @Resource
    private ApplicationEventMulticaster applicationEventMulticaster;

    @Override
    public void refresh(String content) throws Exception {
        if (StringUtils.isBlank(content)) {
            log.warn("DynamicTp refresh, empty content.");
            return;
        }
        DtpChangeConfig dtpChangeConfig = MAPPER.readValue(content, DtpChangeConfig.class);
        doRefresh(dtpChangeConfig);


    }

    private void doRefresh(DtpChangeConfig dtpChangeConfig) {
        DtpRegistry.refresh(dtpChangeConfig);
        publishEvent(dtpChangeConfig);

    }

    private void publishEvent(DtpChangeConfig dtpChangeConfig) {
        RefreshEvent event = new RefreshEvent(dtpChangeConfig);
        applicationEventMulticaster.multicastEvent(event);
    }


}
