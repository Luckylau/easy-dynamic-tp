package com.luckylau.easy.dynamic.tp.common.event;

import com.luckylau.easy.dynamic.tp.common.config.DtpChangeConfig;
import org.springframework.context.ApplicationEvent;

/**
 * @Author luckylau
 * @Date 2022/5/2
 */
public class RefreshEvent extends ApplicationEvent {

    public RefreshEvent(DtpChangeConfig source) {
        super(source);
    }
}
