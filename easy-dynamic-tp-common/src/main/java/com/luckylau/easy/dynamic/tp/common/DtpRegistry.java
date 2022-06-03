package com.luckylau.easy.dynamic.tp.common;

import com.google.common.collect.Lists;
import com.luckylau.easy.dynamic.tp.common.config.DtpChangeConfig;
import com.luckylau.easy.dynamic.tp.common.constant.DynamicTpConst;
import com.luckylau.easy.dynamic.tp.common.model.DtpDesc;
import com.luckylau.easy.dynamic.tp.common.util.equator.Equator;
import com.luckylau.easy.dynamic.tp.common.util.equator.FieldInfo;
import com.luckylau.easy.dynamic.tp.common.util.equator.GetterBaseEquator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.stream.Collectors.toList;

/**
 * @Author luckylau
 * @Date 2022/5/2
 */
@Slf4j
public class DtpRegistry implements ApplicationListener<ContextRefreshedEvent>, Ordered {


    private static final Map<String, DtpExecutor> DTP_REGISTRY = new ConcurrentHashMap<>();

    private static final Equator EQUATOR = new GetterBaseEquator();


    public static void registerDtp(DtpExecutor executor) {
        log.info("DynamicTp register dtpExecutor, executor: {}",
                executor.dtpDesc);
        DTP_REGISTRY.putIfAbsent(executor.getThreadPoolName(), executor);
    }

    public static List<DtpExecutor> listAllDtpExecutors() {
        return Lists.newArrayList(DTP_REGISTRY.values());
    }

    public static void refresh(DtpChangeConfig changeConfig) {
        if (Objects.isNull(changeConfig) || changeConfig.getExecutors() == null) {
            log.warn("DynamicTp refresh, empty DtpConfig.");
            return;
        }
        changeConfig.getExecutors().forEach(x -> {
            if (StringUtils.isBlank(x.getThreadPoolName())) {
                log.warn("DynamicTp refresh, threadPoolName must not be empty.");
                return;
            }
            DtpExecutor dtpExecutor = DTP_REGISTRY.get(x.getThreadPoolName());
            if (Objects.isNull(dtpExecutor)) {
                log.warn("DynamicTp refresh, cannot find specified dtpExecutor, name: {}.", x.getThreadPoolName());
                return;
            }
            refresh(dtpExecutor, x);
        });
    }

    private static void refresh(DtpExecutor executor, DtpDesc dtpDesc) {
        if (dtpDesc.getCorePoolSize() < 0 ||
                dtpDesc.getMaxPoolSize() <= 0 ||
                dtpDesc.getMaxPoolSize() < dtpDesc.getCorePoolSize() ||
                dtpDesc.getKeepAliveTime() < 0) {
            log.error("DynamicTp refresh, invalid parameters exist, properties: {}", dtpDesc);
            return;
        }
        DtpDesc oldDesc = executor.dtpDesc;
        executor.doRefresh(dtpDesc);
        List<FieldInfo> diffFields = EQUATOR.getDiffFields(oldDesc, dtpDesc);
        List<String> diffKeys = diffFields.stream().map(FieldInfo::getFieldName).collect(toList());
        log.info("DynamicTp refresh, name: [{}], changed keys: {}, corePoolSize: [{}], maxPoolSize: [{}], " +
                        "queueType: [{}], queueCapacity: [{}], keepAliveTime: [{}], rejectedType: [{}] ",
                executor.getThreadPoolName(),
                diffKeys,
                String.format(DynamicTpConst.PROPERTIES_CHANGE_SHOW_STYLE, oldDesc.getCorePoolSize(), dtpDesc.getCorePoolSize()),
                String.format(DynamicTpConst.PROPERTIES_CHANGE_SHOW_STYLE, oldDesc.getMaxPoolSize(), dtpDesc.getMaxPoolSize()),
                String.format(DynamicTpConst.PROPERTIES_CHANGE_SHOW_STYLE, oldDesc.getDtpQueue().getQueueType(), dtpDesc.getDtpQueue().getQueueType()),
                String.format(DynamicTpConst.PROPERTIES_CHANGE_SHOW_STYLE, oldDesc.getDtpQueue().getCapacity(), dtpDesc.getDtpQueue().getCapacity()),
                String.format(DynamicTpConst.PROPERTIES_CHANGE_SHOW_STYLE, oldDesc.getKeepAliveTime(), dtpDesc.getKeepAliveTime()),
                String.format(DynamicTpConst.PROPERTIES_CHANGE_SHOW_STYLE, oldDesc.getRejectedHandlerType(), dtpDesc.getRejectedHandlerType()));
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
