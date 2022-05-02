package com.luckylau.easy.dynamic.tp.autoconfigure;

import com.luckylau.easy.dynamic.tp.common.config.DtpProperties;
import com.luckylau.easy.dynamic.tp.common.constant.DynamicTpConst;
import com.luckylau.easy.dynamic.tp.container.handler.TomcatTpHandler;
import com.luckylau.easy.dynamic.tp.container.listener.WebServerRefreshListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author luckylau
 * @Date 2022/5/2
 */
@Configuration
@EnableConfigurationProperties(DtpProperties.class)
@ConditionalOnWebApplication
@ConditionalOnProperty(name = DynamicTpConst.DTP_ENABLED_PROP, matchIfMissing = true, havingValue = "true")
public class WebServerTpAutoConfiguration {

    @Bean
    @ConditionalOnBean(name = {"tomcatServletWebServerFactory"})
    public TomcatTpHandler tomcatTpHandler() {
        return new TomcatTpHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public WebServerRefreshListener dtpWebRefreshListener() {
        return new WebServerRefreshListener();
    }

}
