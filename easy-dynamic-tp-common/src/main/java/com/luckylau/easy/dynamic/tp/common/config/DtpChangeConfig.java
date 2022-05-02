package com.luckylau.easy.dynamic.tp.common.config;

import com.luckylau.easy.dynamic.tp.common.config.web.JettyThreadPool;
import com.luckylau.easy.dynamic.tp.common.config.web.TomcatThreadPool;
import com.luckylau.easy.dynamic.tp.common.config.web.UndertowThreadPool;
import com.luckylau.easy.dynamic.tp.common.model.DtpDesc;
import lombok.Data;

import java.util.List;

/**
 * @Author luckylau
 * @Date 2022/5/2
 */
@Data
public class DtpChangeConfig {

    private List<DtpDesc> executors;

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

}
