package com.luckylau.easy.dynamic.tp.core.thread;

import com.luckylau.easy.dynamic.tp.core.DtpRegistry;
import com.luckylau.easy.dynamic.tp.core.event.RefreshEvent;
import com.luckylau.easy.dynamic.tp.core.model.DtpDesc;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.ApplicationListener;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author luckylau
 * @Date 2022/5/2
 */
public class DtpExecutor extends ThreadPoolExecutor implements DisposableBean, ApplicationListener<RefreshEvent> {

    private DtpDesc dtpDesc;

    public DtpExecutor(String threadPoolName, int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        setDtpDesc(threadPoolName, corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, this.getRejectedExecutionHandler());
        DtpRegistry.registerDtp(this);
    }

    public DtpExecutor(String threadPoolName, int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
        setDtpDesc(threadPoolName, corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, this.getRejectedExecutionHandler());
        DtpRegistry.registerDtp(this);
    }

    public String getThreadPoolName() {
        return dtpDesc.getThreadPoolName();
    }

    private DtpDesc setDtpDesc(String threadPoolName, int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        DtpDesc dtpDesc = new DtpDesc();
        dtpDesc.setCorePoolSize(corePoolSize);
        dtpDesc.setThreadPoolName(threadPoolName);
        dtpDesc.setKeepAliveTime(keepAliveTime);
        dtpDesc.setUnit(unit);
        dtpDesc.setMaximumPoolSize(maximumPoolSize);
        dtpDesc.setWorkQueue(workQueue);
        dtpDesc.setHandler(handler);
        return dtpDesc;
    }

    @Override
    public void destroy() throws Exception {

    }

    @Override
    public void onApplicationEvent(RefreshEvent event) {

    }

    public BeanDefinition toBeanDefinition() {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(DtpExecutor.class);
        beanDefinitionBuilder
                .addConstructorArgValue(this.dtpDesc.getThreadPoolName())
                .addConstructorArgValue(this.dtpDesc.getCorePoolSize())
                .addConstructorArgValue(this.dtpDesc.getMaximumPoolSize())
                .addConstructorArgValue(this.dtpDesc.getKeepAliveTime())
                .addConstructorArgValue(this.dtpDesc.getUnit())
                .addConstructorArgValue(this.dtpDesc.getWorkQueue())
                .addConstructorArgValue(this.dtpDesc.getHandler());
        return beanDefinitionBuilder.getBeanDefinition();
    }


}
