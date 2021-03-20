package com.mars.gateway.common.filter;

import com.mars.server.server.request.HttpMarsRequest;
import com.mars.server.server.request.HttpMarsResponse;

import java.io.InputStream;

/**
 * 网关过滤器
 */
public interface GateFilter {

    String SUCCESS = "success";

    /**
     * 开始执行过滤器
     * @param request
     * @param response
     * @return
     */
    Object doFilter(HttpMarsRequest request, HttpMarsResponse response);

    /**
     * 请求响应过滤
     * @param request
     * @param response
     * @param resultData
     * @param resultStream
     * @return
     */
    Object doResult(HttpMarsRequest request, HttpMarsResponse response, Object resultData, InputStream resultStream);
}
