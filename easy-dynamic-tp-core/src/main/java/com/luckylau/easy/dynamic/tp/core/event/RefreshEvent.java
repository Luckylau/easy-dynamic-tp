package com.luckylau.easy.dynamic.tp.core.event;

import org.springframework.context.ApplicationEvent;

/**
 * @Author luckylau
 * @Date 2022/5/2
 */
public class RefreshEvent extends ApplicationEvent {
    public RefreshEvent(Object source) {
        super(source);
    }
}
