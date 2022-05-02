package com.luckylau.easy.dynamic.tp.core;

import com.luckylau.easy.dynamic.tp.core.converter.ExecutorConverter;
import com.luckylau.easy.dynamic.tp.core.thread.DtpExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author luckylau
 * @Date 2022/5/2
 */
@Slf4j
public class DtpRegistry implements ApplicationListener<ContextRefreshedEvent>, Ordered {


    private static final Map<String, DtpExecutor> DTP_REGISTRY = new ConcurrentHashMap<>();


    public static void registerDtp(DtpExecutor executor) {
        log.info("DynamicTp register dtpExecutor, executor: {}",
                ExecutorConverter.convert(executor));
        DTP_REGISTRY.putIfAbsent(executor.getThreadPoolName(), executor);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        DTP_REGISTRY.forEach((k, v) -> {
            try {
                applicationContext.getBean(k, DtpExecutor.class);
            } catch (NoSuchBeanDefinitionException e) {
                DefaultListableBeanFactory factory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
                factory.registerBeanDefinition(k, v.toBeanDefinition());
            }
        });
    }

}
