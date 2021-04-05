package com.mars.gateway.starter;

import com.mars.gateway.config.MarsGateWayConfig;
import com.mars.gateway.request.GateServer;
import com.mars.gateway.core.timer.GateTimer;
import com.mars.gateway.core.util.GateWayConfigUtil;
import com.mars.gateway.onload.OnLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 启动网关服务
 */
public class StartGateWay {

    private static Logger logger = LoggerFactory.getLogger(StartGateWay.class);

    /**
     * 启动网关服务
     * @param cls
     * @param marsGateWayConfig
     */
    public static void start(Class cls, MarsGateWayConfig marsGateWayConfig){
        try {
            logger.info("网关启动中......");

            // 加载配置文件
            GateWayConfigUtil.setConfig(marsGateWayConfig);

            // 初始化
            OnLoader.onLoad();

            // 启动定时任务
            GateTimer.doNotice();

            // 开启HTTP服务
            GateServer.startServer(marsGateWayConfig.port());
        } catch (Exception e){
            logger.error("网关启动失败", e);
        }
    }
}
