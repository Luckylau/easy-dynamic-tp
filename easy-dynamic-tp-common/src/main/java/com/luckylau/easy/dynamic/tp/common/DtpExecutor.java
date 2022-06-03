package com.luckylau.easy.dynamic.tp.common;

import com.luckylau.easy.dynamic.tp.common.alert.DtpAlertIndicator;
import com.luckylau.easy.dynamic.tp.common.model.DtpDesc;
import com.luckylau.easy.dynamic.tp.common.model.DtpQueue;
import com.luckylau.easy.dynamic.tp.common.thread.DtpRunnable;
import com.luckylau.easy.dynamic.tp.common.thread.queue.QueueHandler;
import com.luckylau.easy.dynamic.tp.common.thread.reject.RejectHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;

import java.util.Objects;
import java.util.concurrent.*;

import static com.luckylau.easy.dynamic.tp.common.em.QueueType.VARIABLE_LINKED_BLOCKING_QUEUE;

/**
 * @Author luckylau
 * @Date 2022/5/2
 */
@Slf4j
public class DtpExecutor extends ThreadPoolExecutor implements DisposableBean {

    protected DtpDesc dtpDesc;

    protected DtpAlertIndicator dtpAlertIndicator;

    public DtpExecutor(String threadPoolName, int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, DtpQueue dtpQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, QueueHandler.buildBlockingQueue(dtpQueue));
        setDtpDesc(threadPoolName, corePoolSize, maximumPoolSize, keepAliveTime, unit, dtpQueue, this.getRejectedExecutionHandler());
        setDtpAlertIndicator(threadPoolName);
        DtpRegistry.registerDtp(this);
    }

    public DtpExecutor(String threadPoolName, int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, DtpQueue dtpQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, QueueHandler.buildBlockingQueue(dtpQueue), handler);
        setDtpDesc(threadPoolName, corePoolSize, maximumPoolSize, keepAliveTime, unit, dtpQueue, this.getRejectedExecutionHandler());
        setDtpAlertIndicator(threadPoolName);
        DtpRegistry.registerDtp(this);
    }

    public void setWaitForTasksToCompleteOnShutdown(boolean waitForTasksToCompleteOnShutdown) {
        this.dtpDesc.setWaitForTasksToCompleteOnShutdown(waitForTasksToCompleteOnShutdown);
    }

    public void setAwaitTerminationSeconds(int awaitTerminationSeconds) {
        this.dtpDesc.setAwaitTerminationSeconds(awaitTerminationSeconds);
    }

    protected String getThreadPoolName() {
        return dtpDesc.getThreadPoolName();
    }

    protected void doRefresh(DtpDesc dtpDesc) {
        if (!Objects.equals(this.getCorePoolSize(), dtpDesc.getCorePoolSize())) {
            this.setCorePoolSize(dtpDesc.getCorePoolSize());
            this.dtpDesc.setCorePoolSize(dtpDesc.getCorePoolSize());
        }

        if (!Objects.equals(this.getMaximumPoolSize(), dtpDesc.getMaxPoolSize())) {
            this.setMaximumPoolSize(dtpDesc.getMaxPoolSize());
            this.dtpDesc.setMaxPoolSize(dtpDesc.getMaxPoolSize());
        }

        if (!Objects.equals(this.getKeepAliveTime(this.dtpDesc.getUnit()), dtpDesc.getKeepAliveTime(this.dtpDesc.getUnit()))) {
            this.setKeepAliveTime(dtpDesc.getKeepAliveTime(), dtpDesc.getUnit());
            this.dtpDesc.setKeepAliveTime(dtpDesc.getKeepAliveTime());
            this.dtpDesc.setUnit(dtpDesc.getUnit());
        }

        // update reject handler
        if (!Objects.equals(this.dtpDesc.getRejectedHandlerType(), dtpDesc.getRejectedHandlerType())) {
            this.setRejectedExecutionHandler(RejectHandler.getProxy(dtpDesc.getRejectedHandlerType()));
            this.dtpDesc.setRejectedHandlerType(dtpDesc.getRejectedHandlerType());
        }

        // update work queue capacity
        DtpQueue oldQueue = this.dtpDesc.getDtpQueue();
        DtpQueue newQueue = dtpDesc.getDtpQueue();
        if (!Objects.equals(oldQueue.getCapacity(), newQueue.getCapacity()) &&
                oldQueue.getQueueType() == VARIABLE_LINKED_BLOCKING_QUEUE) {
            BlockingQueue<Runnable> blockingQueue = this.getQueue();
            if (blockingQueue instanceof VariableLinkedBlockingQueue) {
                ((VariableLinkedBlockingQueue<Runnable>) blockingQueue).setCapacity(dtpDesc.getDtpQueue().getCapacity());
                this.dtpDesc.getDtpQueue().setCapacity(dtpDesc.getDtpQueue().getCapacity());
            } else {
                log.error("DynamicTp refresh, the blockingqueue capacity cannot be reset, dtpName: {}, queueType {}",
                        this.getThreadPoolName(), this.dtpDesc.getDtpQueue().getQueueType());
            }
        }
        this.dtpDesc.setWaitForTasksToCompleteOnShutdown(dtpDesc.isWaitForTasksToCompleteOnShutdown());
        this.dtpDesc.setAwaitTerminationSeconds(dtpDesc.getAwaitTerminationSeconds());
    }

    private void setDtpDesc(String threadPoolName, int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, DtpQueue workQueue, RejectedExecutionHandler handler) {
        DtpDesc dtpDesc = new DtpDesc();
        dtpDesc.setCorePoolSize(corePoolSize);
        dtpDesc.setThreadPoolName(threadPoolName);
        dtpDesc.setKeepAliveTime(keepAliveTime);
        dtpDesc.setUnit(unit);
        dtpDesc.setMaxPoolSize(maximumPoolSize);
        dtpDesc.setDtpQueue(workQueue);
        dtpDesc.setRejectedHandlerType(RejectHandler.getRejectedHandlerType(handler));
        this.dtpDesc = dtpDesc;
    }

    protected BeanDefinition toBeanDefinition() {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(DtpExecutor.class);
        beanDefinitionBuilder
                .addConstructorArgValue(this.dtpDesc.getThreadPoolName())
                .addConstructorArgValue(this.dtpDesc.getCorePoolSize())
                .addConstructorArgValue(this.dtpDesc.getMaxPoolSize())
                .addConstructorArgValue(this.dtpDesc.getKeepAliveTime())
                .addConstructorArgValue(this.dtpDesc.getUnit())
                .addConstructorArgValue(this.dtpDesc.getDtpQueue())
                .addConstructorArgValue(RejectHandler.getProxy(this.dtpDesc.getRejectedHandlerType()));
        return beanDefinitionBuilder.getBeanDefinition();
    }

    @Override
    public void destroy() throws Exception {
        internalShutdown();
    }

    public DtpDesc getDtpDesc() {
        return dtpDesc;
    }

    private void internalShutdown() {
        log.info("Shutting down ExecutorService, poolName: {}", this.getThreadPoolName());
        if (this.dtpDesc.isWaitForTasksToCompleteOnShutdown()) {
            this.shutdown();
        } else {
            for (Runnable remainingTask : this.shutdownNow()) {
                cancelRemainingTask(remainingTask);
            }
        }
        awaitTerminationIfNecessary();

    }

    public DtpAlertIndicator getDtpAlertIndicator() {
        return dtpAlertIndicator;
    }

    private void setDtpAlertIndicator(String threadPoolName) {
        this.dtpAlertIndicator = new DtpAlertIndicator(threadPoolName);
    }

    private void cancelRemainingTask(Runnable task) {
        if (task instanceof Future) {
            ((Future<?>) task).cancel(true);
        }
    }

    private void awaitTerminationIfNecessary() {
        int awaitTerminationSeconds = this.dtpDesc.getAwaitTerminationSeconds();
        if (awaitTerminationSeconds <= 0) {
            return;
        }
        try {
            if (!awaitTermination(awaitTerminationSeconds, TimeUnit.SECONDS)) {
                log.warn("Timed out while waiting for executor {} to terminate", this.getThreadPoolName());
            }
        } catch (InterruptedException ex) {
            if (log.isWarnEnabled()) {
                log.warn("Interrupted while waiting for executor {} to terminate", this.getThreadPoolName());
            }
            Thread.currentThread().interrupt();
        }
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        long currTime = System.currentTimeMillis();
        DtpRunnable runnable = (DtpRunnable) r;
        runnable.setStartTime(currTime);

        long waitTime = currTime - runnable.getSubmitTime();
        long queueTimeout = this.dtpDesc.getQueueTimeout();
        if (queueTimeout > 0 && queueTimeout < waitTime) {
            this.dtpAlertIndicator.getQueueTimeoutCount().incrementAndGet();
        }
        super.beforeExecute(t, r);
    }

    @Override
    public void execute(Runnable command) {
        command = new DtpRunnable(command);
        super.execute(command);
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
    }
}
