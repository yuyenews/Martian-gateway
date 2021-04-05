package com.mars.gateway.core.timer;

import com.mars.gateway.core.notice.GateNotice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 定时任务
 */
public class GateTimer {

    private static Logger logger = LoggerFactory.getLogger(GateTimer.class);

    /**
     * 通知器
     */
    private static GateNotice gateNotice = new GateNotice();

    /**
     * 传染频率
     */
    private static final int period = 3000;

    /**
     * 清理垃圾的频率
     */
    private static final int clearLoop = 200;

    /**
     * 刷新本地缓存的微服务接口
     */
    public static void doNotice() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    gateNotice.notice();
                } catch (Exception e) {
                    logger.error("刷新本地服务缓存失败，10秒后将重试", e);
                }
            }
        }, new Date(), period);
    }
}
