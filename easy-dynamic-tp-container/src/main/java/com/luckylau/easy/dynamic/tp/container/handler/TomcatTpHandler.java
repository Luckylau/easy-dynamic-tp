package com.luckylau.easy.dynamic.tp.container.handler;

import com.luckylau.easy.dynamic.tp.common.config.DtpChangeConfig;
import com.luckylau.easy.dynamic.tp.common.config.DtpProperties;
import com.luckylau.easy.dynamic.tp.common.config.web.TomcatThreadPool;
import com.luckylau.easy.dynamic.tp.common.ex.DtpException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.boot.web.server.WebServer;

import java.util.Objects;
import java.util.concurrent.Executor;

import static com.luckylau.easy.dynamic.tp.common.constant.DynamicTpConst.PROPERTIES_CHANGE_SHOW_STYLE;

/**
 * @Author luckylau
 * @Date 2022/5/2
 */
@Slf4j
public class TomcatTpHandler extends AbstractWebServerTpHandler {


    @Override
    public void updateTp(DtpProperties dtpProperties) {
        TomcatThreadPool tomcatTp = dtpProperties.getTomcatTp();
        updateTomcatThreadPool(tomcatTp);
    }

    @Override
    protected Executor doGetTp(WebServer webServer) {
        TomcatWebServer tomcatWebServer = (TomcatWebServer) webServer;
        return tomcatWebServer.getTomcat().getConnector().getProtocolHandler().getExecutor();
    }

    @Override
    public void updateTp(DtpChangeConfig dtpChangeConfig) {
        TomcatThreadPool tomcatTp = dtpChangeConfig.getTomcatTp();
        updateTomcatThreadPool(tomcatTp);

    }

    private void updateTomcatThreadPool(TomcatThreadPool tomcatTp) {
        if (Objects.isNull(tomcatTp)) {
            return;
        }

        int oldMinSpare = getThreadPoolExecutor().getCorePoolSize();
        int oldMax = getThreadPoolExecutor().getMaximumPoolSize();

        getThreadPoolExecutor().setCorePoolSize(tomcatTp.getMin());
        getThreadPoolExecutor().setMaximumPoolSize(tomcatTp.getMax());

        log.info("DynamicTp tomcatWebServerTp refreshed end, minSpare: [{}], max: [{}]",
                String.format(PROPERTIES_CHANGE_SHOW_STYLE, oldMinSpare, tomcatTp.getMin()),
                String.format(PROPERTIES_CHANGE_SHOW_STYLE, oldMax, tomcatTp.getMax()));
    }


    private ThreadPoolExecutor getThreadPoolExecutor() {
        Executor executor = getTp();
        if (Objects.isNull(executor)) {
            log.warn("Tomcat web server threadPool is null.");
            throw new DtpException("Tomcat web server threadPool is null.");
        }
        return (ThreadPoolExecutor) executor;
    }
}
