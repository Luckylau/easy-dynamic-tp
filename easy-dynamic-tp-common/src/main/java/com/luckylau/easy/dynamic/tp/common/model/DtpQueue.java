package com.luckylau.easy.dynamic.tp.common.model;

import com.luckylau.easy.dynamic.tp.common.em.QueueTypeEnum;
import lombok.Builder;
import lombok.Data;

/**
 * @Author luckylau
 * @Date 2022/5/2
 */
@Data
@Builder
public class DtpQueue {
    private QueueTypeEnum queueTypeEnum;
    private int capacity;
    private boolean fair;
}
