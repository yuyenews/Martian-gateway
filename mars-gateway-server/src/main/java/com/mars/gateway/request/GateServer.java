package com.mars.gateway.request;

import com.mars.aio.server.MarsServer;
import com.mars.aio.server.factory.MarsServerFactory;
import com.mars.aio.server.factory.MarsServerHandlerFactory;
import com.mars.gateway.api.GateWayDispatcher;

/**
 * HTTP服务
 */
public class GateServer {
    /**
     * 启动一个Http服务
     * @param port
     * @throws Exception
     */
    public static void startServer(int port) throws Exception {

        MarsServerHandlerFactory.setMarsServerHandler(new GateWayDispatcher());
        MarsServer marsServer = MarsServerFactory.getMarsServer();
        marsServer.start(port);

    }
}
