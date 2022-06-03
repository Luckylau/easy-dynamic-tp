package com.luckylau.easy.dynamic.tp.common.thread.reject;

import com.luckylau.easy.dynamic.tp.common.ex.DtpException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import static com.luckylau.easy.dynamic.tp.common.em.RejectedType.*;

/**
 * @Author luckylau
 * @Date 2022/5/2
 */
@Slf4j
public class RejectHandler {

    public static String getRejectedHandlerType(RejectedExecutionHandler handler) {
        if (handler instanceof ThreadPoolExecutor.AbortPolicy) {
            return ABORT_POLICY.getName();
        } else if (handler instanceof ThreadPoolExecutor.CallerRunsPolicy) {
            return CALLER_RUNS_POLICY.getName();
        } else if (handler instanceof ThreadPoolExecutor.DiscardOldestPolicy) {
            return DISCARD_OLDEST_POLICY.getName();
        } else if (handler instanceof ThreadPoolExecutor.DiscardPolicy) {
            return DISCARD_POLICY.getName();
        }
        return handler.getClass().getSimpleName();
    }


    private static RejectedExecutionHandler buildRejectedHandler(String name) {
        if (Objects.equals(name, ABORT_POLICY.getName())) {
            return new ThreadPoolExecutor.AbortPolicy();
        } else if (Objects.equals(name, CALLER_RUNS_POLICY.getName())) {
            return new ThreadPoolExecutor.CallerRunsPolicy();
        } else if (Objects.equals(name, DISCARD_OLDEST_POLICY.getName())) {
            return new ThreadPoolExecutor.DiscardOldestPolicy();
        } else if (Objects.equals(name, DISCARD_POLICY.getName())) {
            return new ThreadPoolExecutor.DiscardPolicy();
        }

        ServiceLoader<RejectedExecutionHandler> serviceLoader = ServiceLoader.load(RejectedExecutionHandler.class);
        for (RejectedExecutionHandler handler : serviceLoader) {
            String handlerName = handler.getClass().getSimpleName();
            if (name.equalsIgnoreCase(handlerName)) {
                return handler;
            }
        }

        log.error("Cannot find specified rejectedHandler {}", name);
        throw new DtpException("Cannot find specified rejectedHandler " + name);
    }

    public static RejectedExecutionHandler getProxy(String name) {
        return getProxy(buildRejectedHandler(name));
    }

    public static RejectedExecutionHandler getProxy(RejectedExecutionHandler handler) {
        return (RejectedExecutionHandler) Proxy
                .newProxyInstance(handler.getClass().getClassLoader(),
                        new Class[]{RejectedExecutionHandler.class},
                        new RejectedInvocationHandler(handler));
    }

    private static class RejectedInvocationHandler implements InvocationHandler {

        private final Object target;

        public RejectedInvocationHandler(Object target) {
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            try {
                ThreadPoolExecutor executor = (ThreadPoolExecutor) args[1];
                return method.invoke(target, args);
            } catch (InvocationTargetException ex) {
                throw ex.getCause();
            }
        }
    }
}
