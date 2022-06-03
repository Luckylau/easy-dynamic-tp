package com.luckylau.easy.dynamic.tp.alert;

import com.luckylau.easy.dynamic.tp.common.em.NotifyType;
import com.luckylau.easy.dynamic.tp.common.model.DtpDesc;

import java.util.List;

/**
 * @Author luckylau
 * @Date 2022/5/29
 */
public interface Notifier {
    /**
     * Get the send platform name.
     *
     * @return platform
     */
    String platform();

    /**
     * Send change notify message.
     *
     * @param oldProp old properties
     * @param diffs   the changed keys
     */
    void sendChangeMsg(DtpDesc oldProp, List<String> diffs);

    /**
     * Send alarm message.
     *
     * @param typeEnum notify type
     */
    void sendAlarmMsg(NotifyType typeEnum);
}
