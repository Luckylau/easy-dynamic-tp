package com.luckylau.easy.dynamic.tp.common.config;

import com.luckylau.easy.dynamic.tp.common.config.web.JettyThreadPool;
import com.luckylau.easy.dynamic.tp.common.config.web.TomcatThreadPool;
import com.luckylau.easy.dynamic.tp.common.config.web.UndertowThreadPool;
import com.luckylau.easy.dynamic.tp.common.constant.DynamicTpConst;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author luckylau
 * @Date 2022/5/2
 */
@Data
@ConfigurationProperties(prefix = DynamicTpConst.MAIN_PROPERTIES_PREFIX)
public class DtpProperties {

    /**
     * If enabled DynamicTp.
     */
    private boolean enabled = true;

    /**
     * Tomcat worker thread pool.
     */
    private TomcatThreadPool tomcatTp;

    /**
     * Jetty thread pool.
     */
    private JettyThreadPool jettyTp;

    /**
     * Undertow thread pool.
     */
    private UndertowThreadPool undertowTp;

    private DtpAlert dtpAlert;

}
