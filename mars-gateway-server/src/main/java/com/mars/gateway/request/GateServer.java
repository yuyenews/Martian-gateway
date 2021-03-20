package com.mars.gateway.request;

import com.mars.gateway.api.GateWayDispatcher;
import com.mars.iserver.server.MarsServer;
import com.mars.iserver.server.factory.MarsServerFactory;
import com.mars.iserver.server.factory.MarsServerHandlerFactory;

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
