package com.luckylau.easy.dynamic.tp.core;

import com.luckylau.easy.dynamic.tp.common.VariableLinkedBlockingQueue;
import com.luckylau.easy.dynamic.tp.common.model.DtpDesc;
import com.luckylau.easy.dynamic.tp.common.model.DtpQueue;
import com.luckylau.easy.dynamic.tp.core.thread.queue.QueueHandler;
import com.luckylau.easy.dynamic.tp.core.thread.reject.RejectHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;

import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.luckylau.easy.dynamic.tp.common.em.QueueTypeEnum.VARIABLE_LINKED_BLOCKING_QUEUE;

/**
 * @Author luckylau
 * @Date 2022/5/2
 */
@Slf4j
public class DtpExecutor extends ThreadPoolExecutor implements DisposableBean {

    private DtpDesc dtpDesc;

    public DtpExecutor(String threadPoolName, int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, DtpQueue dtpQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, QueueHandler.buildBlockingQueue(dtpQueue));
        setDtpDesc(threadPoolName, corePoolSize, maximumPoolSize, keepAliveTime, unit, dtpQueue, this.getRejectedExecutionHandler());
        DtpRegistry.registerDtp(this);
    }

    public DtpExecutor(String threadPoolName, int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, DtpQueue dtpQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, QueueHandler.buildBlockingQueue(dtpQueue), handler);
        setDtpDesc(threadPoolName, corePoolSize, maximumPoolSize, keepAliveTime, unit, dtpQueue, this.getRejectedExecutionHandler());
        DtpRegistry.registerDtp(this);
    }

    protected DtpDesc getDtpDesc() {
        return dtpDesc;
    }

    public String getThreadPoolName() {
        return dtpDesc.getThreadPoolName();
    }

    protected void doRefresh(DtpDesc dtpDesc) {
        if (!Objects.equals(this.getCorePoolSize(), dtpDesc.getCorePoolSize())) {
            this.setCorePoolSize(dtpDesc.getCorePoolSize());
            this.dtpDesc.setCorePoolSize(dtpDesc.getCorePoolSize());
        }

        if (!Objects.equals(this.getMaximumPoolSize(), dtpDesc.getMaximumPoolSize())) {
            this.setMaximumPoolSize(dtpDesc.getMaximumPoolSize());
            this.dtpDesc.setMaximumPoolSize(dtpDesc.getMaximumPoolSize());
        }

        if (!Objects.equals(this.getKeepAliveTime(this.getDtpDesc().getUnit()), dtpDesc.getKeepAliveTime(this.dtpDesc.getUnit()))) {
            this.setKeepAliveTime(dtpDesc.getKeepAliveTime(), dtpDesc.getUnit());
            this.dtpDesc.setKeepAliveTime(dtpDesc.getKeepAliveTime());
            this.dtpDesc.setUnit(dtpDesc.getUnit());
        }

        // update reject handler
        if (!Objects.equals(this.getDtpDesc().getRejectedHandlerType(), dtpDesc.getRejectedHandlerType())) {
            this.setRejectedExecutionHandler(RejectHandler.getProxy(dtpDesc.getRejectedHandlerType()));
            this.dtpDesc.setRejectedHandlerType(dtpDesc.getRejectedHandlerType());
        }

        // update work queue capacity
        DtpQueue oldQueue = this.getDtpDesc().getDtpQueue();
        DtpQueue newQueue = dtpDesc.getDtpQueue();
        if (!Objects.equals(oldQueue.getCapacity(), newQueue.getCapacity()) &&
                oldQueue.getQueueTypeEnum() == VARIABLE_LINKED_BLOCKING_QUEUE) {
            BlockingQueue<Runnable> blockingQueue = this.getQueue();
            if (blockingQueue instanceof VariableLinkedBlockingQueue) {
                ((VariableLinkedBlockingQueue<Runnable>) blockingQueue).setCapacity(dtpDesc.getDtpQueue().getCapacity());
                this.dtpDesc.getDtpQueue().setCapacity(dtpDesc.getDtpQueue().getCapacity());
            } else {
                log.error("DynamicTp refresh, the blockingqueue capacity cannot be reset, dtpName: {}, queueType {}",
                        this.getThreadPoolName(), this.getDtpDesc().getDtpQueue().getQueueTypeEnum());
            }
        }
    }

    private void setDtpDesc(String threadPoolName, int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, DtpQueue workQueue, RejectedExecutionHandler handler) {
        DtpDesc dtpDesc = new DtpDesc();
        dtpDesc.setCorePoolSize(corePoolSize);
        dtpDesc.setThreadPoolName(threadPoolName);
        dtpDesc.setKeepAliveTime(keepAliveTime);
        dtpDesc.setUnit(unit);
        dtpDesc.setMaximumPoolSize(maximumPoolSize);
        dtpDesc.setDtpQueue(workQueue);
        dtpDesc.setRejectedHandlerType(RejectHandler.getRejectedHandlerType(handler));
        this.dtpDesc = dtpDesc;
    }

    @Override
    public void destroy() throws Exception {

    }

    protected BeanDefinition toBeanDefinition() {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(DtpExecutor.class);
        beanDefinitionBuilder
                .addConstructorArgValue(this.dtpDesc.getThreadPoolName())
                .addConstructorArgValue(this.dtpDesc.getCorePoolSize())
                .addConstructorArgValue(this.dtpDesc.getMaximumPoolSize())
                .addConstructorArgValue(this.dtpDesc.getKeepAliveTime())
                .addConstructorArgValue(this.dtpDesc.getUnit())
                .addConstructorArgValue(this.dtpDesc.getDtpQueue())
                .addConstructorArgValue(RejectHandler.getProxy(this.dtpDesc.getRejectedHandlerType()));
        return beanDefinitionBuilder.getBeanDefinition();
    }


}
