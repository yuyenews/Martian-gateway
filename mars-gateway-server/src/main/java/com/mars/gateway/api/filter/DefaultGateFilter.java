package com.mars.gateway.api.filter;

import com.mars.common.constant.MarsConstant;
import com.mars.gateway.api.util.DispatcherUtil;
import com.mars.gateway.common.filter.GateFilter;
import com.mars.gateway.core.constant.GateWayConstant;
import com.mars.iserver.execute.access.PathAccess;
import com.mars.server.server.request.HttpMarsRequest;
import com.mars.server.server.request.HttpMarsResponse;

import java.io.InputStream;

/**
 * 默认拦截器
 */
public class DefaultGateFilter implements GateFilter {

    /**
     * 过滤非法请求
     * @param request
     * @param response
     * @return
     */
    @Override
    public Object doFilter(HttpMarsRequest request, HttpMarsResponse response) {
        String requestUri = request.getUrl();

        String uri = DispatcherUtil.getRequestPath(requestUri).toUpperCase();
        if(uri.equals("/")){
            /* 如果请求的是根目录，则返回欢迎语 */
            return GateWayConstant.WELCOME;
        }

        boolean isFail = filterUri(requestUri, request);
        if(isFail){
            /* 如果请求的路径不合法，则直接响应一个ok */
            return GateWayConstant.OK;
        }

        return SUCCESS;
    }

    /**
     * 过滤要响应的参数
     * @param request
     * @param response
     * @param resultData
     * @param resultStream
     * @return
     */
    @Override
    public Object doResult(HttpMarsRequest request, HttpMarsResponse response, Object resultData, InputStream resultStream) {
        return SUCCESS;
    }

    /**
     * 过滤掉非法请求
     * @param requestUri
     * @param request
     * @return
     */
    private static boolean filterUri(String requestUri, HttpMarsRequest request){
        String method = request.getMethod().toUpperCase();
        /* 如果请求方式是options，那就说明这是一个试探性的请求，直接响应即可 */
        if(method.equals(MarsConstant.OPTIONS.toLowerCase())){
            return true;
        }

        /* 如果请求的路径不合法，则直接响应 */
        String uri = DispatcherUtil.getUriName(requestUri);
        if(PathAccess.hasAccess(uri)){
            return true;
        }
        return false;
    }
}
