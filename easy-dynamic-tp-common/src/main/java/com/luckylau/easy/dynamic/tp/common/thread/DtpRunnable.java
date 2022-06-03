package com.luckylau.easy.dynamic.tp.common.thread;

/**
 * @Author luckylau
 * @Date 2022/5/29
 */
public class DtpRunnable implements Runnable {
    private final Runnable runnable;

    private final Long submitTime;

    private Long startTime;

    public DtpRunnable(Runnable runnable) {
        this.runnable = runnable;
        submitTime = System.currentTimeMillis();
    }

    @Override
    public void run() {
        runnable.run();
    }

    public Long getSubmitTime() {
        return submitTime;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }
}
