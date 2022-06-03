package com.luckylau.easy.dynamic.tp.autoconfigure;

import com.luckylau.easy.dynamic.tp.alert.AlertWorker;
import com.luckylau.easy.dynamic.tp.common.DtpRegistry;
import com.luckylau.easy.dynamic.tp.common.config.DtpProperties;
import com.luckylau.easy.dynamic.tp.common.constant.DynamicTpConst;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author luckylau
 * @Date 2022/5/2
 */
@Configuration
@EnableConfigurationProperties(DtpProperties.class)
@ConditionalOnProperty(name = DynamicTpConst.DTP_ENABLED_PROP, matchIfMissing = true, havingValue = "true")
public class BaseBeanAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public DtpRegistry dtpRegistry() {
        return new DtpRegistry();
    }

    @Bean
    @ConditionalOnMissingBean
    public AlertWorker alertWorker() {
        return new AlertWorker();
    }
}

