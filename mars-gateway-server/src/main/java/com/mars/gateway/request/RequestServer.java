package com.mars.gateway.request;

import com.mars.cloud.model.HttpResultModel;
import com.mars.cloud.request.rest.request.MarsRestTemplate;
import com.mars.cloud.request.util.model.MarsHeader;
import com.mars.gateway.api.model.RequestInfoModel;
import com.mars.gateway.request.param.ParamConversionToModel;
import com.mars.gateway.request.util.RequestUtil;
import com.mars.aio.server.impl.MarsHttpExchange;
import com.mars.aio.server.model.HttpHeaders;
import com.mars.server.server.request.HttpMarsRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * 转发给对应的微服务
 */
public class RequestServer {

    /**
     * 发起请求
     *
     * @param requestInfoModel
     * @param request
     * @return
     * @throws Exception
     */
    public static HttpResultModel doRequest(RequestInfoModel requestInfoModel, HttpMarsRequest request) throws Exception {
        Object params = ParamConversionToModel.paramConversionToMap(request);

        MarsHeader header = getHeader(request);

        return MarsRestTemplate.request(requestInfoModel.getServerName(),
                requestInfoModel.getMethodName(),
                new Object[]{params},
                HttpResultModel.class,
                RequestUtil.getContentType(request), header);
    }

    /**
     * 获取请求头
     *
     * @param request
     * @return
     */
    private static MarsHeader getHeader(HttpMarsRequest request) {
        HttpHeaders headers = request.getNativeRequest(MarsHttpExchange.class).getRequestHeaders();
        if (headers == null || headers.size() < 1) {
            return null;
        }
        MarsHeader marsHeader = new MarsHeader();
        for (String name : headers.keySet()) {
            String value = headers.get(name);
            if (value == null) {
                continue;
            }
            List<String> values = new ArrayList<>();
            values.add(value);
            marsHeader.put(name, values);
        }
        return marsHeader;
    }
}
