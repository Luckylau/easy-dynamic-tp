package com.luckylau.easy.dynamic.tp.core.thread.queue;

import com.luckylau.easy.dynamic.tp.common.VariableLinkedBlockingQueue;
import com.luckylau.easy.dynamic.tp.common.em.QueueTypeEnum;
import com.luckylau.easy.dynamic.tp.common.ex.DtpException;
import com.luckylau.easy.dynamic.tp.common.model.DtpQueue;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

import static com.luckylau.easy.dynamic.tp.common.em.QueueTypeEnum.*;

/**
 * @Author luckylau
 * @Date 2022/5/2
 */
@Slf4j
public class QueueHandler {

    public static BlockingQueue<Runnable> buildBlockingQueue(DtpQueue queue) {
        QueueTypeEnum name = queue.getQueueTypeEnum();
        int capacity = queue.getCapacity();
        boolean fair = queue.isFair();
        BlockingQueue<Runnable> blockingQueue = null;
        if (name == ARRAY_BLOCKING_QUEUE) {
            blockingQueue = new ArrayBlockingQueue<>(capacity);
        } else if (name == LINKED_BLOCKING_QUEUE) {
            blockingQueue = new LinkedBlockingQueue<>(capacity);
        } else if (name == PRIORITY_BLOCKING_QUEUE) {
            blockingQueue = new PriorityBlockingQueue<>(capacity);
        } else if (name == DELAY_QUEUE) {
            blockingQueue = new DelayQueue();
        } else if (name == SYNCHRONOUS_QUEUE) {
            blockingQueue = new SynchronousQueue<>(fair);
        } else if (name == LINKED_TRANSFER_QUEUE) {
            blockingQueue = new LinkedTransferQueue<>();
        } else if (name == LINKED_BLOCKING_DEQUE) {
            blockingQueue = new LinkedBlockingDeque<>(capacity);
        } else if (name == VARIABLE_LINKED_BLOCKING_QUEUE) {
            blockingQueue = new VariableLinkedBlockingQueue<>(capacity);
        }
        if (blockingQueue != null) {
            return blockingQueue;
        }

        log.error("Cannot find specified BlockingQueue {}", name);
        throw new DtpException("Cannot find specified BlockingQueue " + name);
    }
}
