package com.luckylau.easy.dynamic.tp.core.refresh;

/**
 * @Author luckylau
 * @Date 2022/5/2
 */
public interface Refresher {
    /**
     * content = dtpConfig.json
     *
     * @param content
     * @throws Exception
     */
    void refresh(String content) throws Exception;
}
