package com.mars.gateway.api.util;

import com.mars.common.util.MesUtil;
import com.mars.common.util.StringUtil;
import com.mars.gateway.api.model.RequestInfoModel;
import com.mars.server.server.request.HttpMarsResponse;


/**
 * 请求和响应工具类
 */
public class RequestAndResultUtil {

    /**
     * 从请求中解析出要转发的服务名和方法名
     * @param url
     * @return
     * @throws Exception
     */
    public static RequestInfoModel getServerNameAndMethodName(String url) throws Exception {
        url = DispatcherUtil.getUriName(url);

        if (url.startsWith("/")) {
            url = url.substring(1);
        }
        if (StringUtil.isNull(url)) {
            throw new Exception("请求路径有误");
        }

        String[] urls = url.split("/");
        if (urls == null || urls.length < 2) {
            throw new Exception("请求路径有误");
        }

        RequestInfoModel requestInfoModel = new RequestInfoModel();
        requestInfoModel.setServerName(urls[0]);
        requestInfoModel.setMethodName(urls[1]);
        return requestInfoModel;
    }

    /**
     * 响应数据
     *
     * @param context 消息
     */
    public static void send(HttpMarsResponse response, String context) {
        response.send(MesUtil.getMes(500,context));
    }
}
