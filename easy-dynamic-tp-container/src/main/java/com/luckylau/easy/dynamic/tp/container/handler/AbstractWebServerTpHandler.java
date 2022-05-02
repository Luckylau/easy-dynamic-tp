package com.luckylau.easy.dynamic.tp.container.handler;

import com.luckylau.easy.dynamic.tp.common.config.DtpProperties;
import com.luckylau.easy.dynamic.tp.common.util.ApplicationContextHolder;
import com.luckylau.easy.dynamic.tp.container.TpHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.context.WebServerApplicationContext;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;

import java.util.concurrent.Executor;

/**
 * @Author luckylau
 * @Date 2022/5/2
 */
@Slf4j
public abstract class AbstractWebServerTpHandler implements TpHandler, ApplicationListener<ServletWebServerInitializedEvent> {

    protected volatile Executor webServerExecutor;

    @Override
    public Executor getTp() {
        if (webServerExecutor == null) {
            synchronized (AbstractWebServerTpHandler.class) {
                if (webServerExecutor == null) {
                    ApplicationContext applicationContext = ApplicationContextHolder.getInstance();
                    WebServer webServer = ((WebServerApplicationContext) applicationContext).getWebServer();
                    webServerExecutor = doGetTp(webServer);
                }
            }
        }
        return webServerExecutor;
    }


    @Override
    public void onApplicationEvent(ServletWebServerInitializedEvent event) {
        try {
            DtpProperties dtpProperties = ApplicationContextHolder.getBean(DtpProperties.class);
            updateTp(dtpProperties);
        } catch (Exception e) {
            log.error("Init web server thread pool failed.", e);
        }
    }

    protected abstract Executor doGetTp(WebServer webServer);
}
