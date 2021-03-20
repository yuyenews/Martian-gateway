package com.mars.gateway.onload;

import com.mars.gateway.core.notice.GateNotice;

/**
 * 启动时事件
 */
public class OnLoader {

    /**
     * 传染服务接口
     * @throws Exception
     */
    public static void onLoad() throws Exception {
        /* 传染服务接口 */
        GateNotice gateNotice = new GateNotice();
        gateNotice.notice();
    }
}
