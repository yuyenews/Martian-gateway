package com.mars.gateway.api.util;


public class DispatcherUtil {

    /**
     * 从uri中提取 请求连接的最末端
     * @param requestUri
     * @return
     */
    public static String getRequestPath(String requestUri) {
        /* 获取路径 */
        String uri = getUriName(requestUri);
        if(uri.startsWith("/") && !uri.equals("/")) {
            uri = uri.substring(1);
        }
        return uri;
    }

    /**
     * 从uri中提取最末端
     * @param uri 请求
     * @return string
     */
    public static String getUriName(String uri) {
        /* 获取路径 */
        if(uri.indexOf("?")>-1) {
            uri = uri.substring(0,uri.indexOf("?"));
        }
        return uri;
    }
}
