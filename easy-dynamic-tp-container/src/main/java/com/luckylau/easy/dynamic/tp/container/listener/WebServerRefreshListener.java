package com.luckylau.easy.dynamic.tp.container.listener;

import com.luckylau.easy.dynamic.tp.common.config.DtpChangeConfig;
import com.luckylau.easy.dynamic.tp.common.event.RefreshEvent;
import com.luckylau.easy.dynamic.tp.common.util.ApplicationContextHolder;
import com.luckylau.easy.dynamic.tp.container.TpHandler;
import com.luckylau.easy.dynamic.tp.container.handler.AbstractWebServerTpHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;

/**
 * @Author luckylau
 * @Date 2022/5/2
 */
@Slf4j
public class WebServerRefreshListener implements ApplicationListener<RefreshEvent> {

    @Override
    public void onApplicationEvent(@NonNull RefreshEvent event) {
        try {
            TpHandler webServerTpHandler = ApplicationContextHolder.getBean(AbstractWebServerTpHandler.class);
            webServerTpHandler.updateTp((DtpChangeConfig) event.getSource());
        } catch (Exception e) {
            log.error("DynamicTp refresh, update web server thread pool failed.", e);
        }
    }
}
