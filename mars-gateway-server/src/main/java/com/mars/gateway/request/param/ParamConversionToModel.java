package com.mars.gateway.request.param;

import com.mars.cloud.annotation.enums.ContentType;
import com.mars.common.util.JSONUtil;
import com.mars.gateway.request.util.RequestUtil;
import com.mars.aio.par.factory.InitRequestFactory;
import com.mars.server.server.request.HttpMarsRequest;
import com.mars.server.server.request.model.MarsFileUpLoad;

import java.util.HashMap;
import java.util.Map;

/**
 * 参数转对象
 */
public class ParamConversionToModel {


    /**
     * 从请求中获取参数，并转化成restTemplate可以识别的对象
     * @param request
     * @return
     * @throws Exception
     */
    public static Object paramConversionToMap(HttpMarsRequest request) throws Exception {
        request = InitRequestFactory.getInitRequest().getHttpMarsRequest(request);

        ContentType contentType = RequestUtil.getContentType(request);
        switch (contentType){
            case FORM:
                return request.getParameters();
            case JSON:
                return getJSON(request);
            case FORM_DATA:
                return getFormData(request);
        }
        return null;
    }

    /**
     * 获取JSON的参数
     * @param request
     * @return
     */
    private static Map<String, Object> getJSON(HttpMarsRequest request){
        String jsonObject = request.getJsonParam();
        if(jsonObject == null){
            return null;
        }
        return JSONUtil.toMap(jsonObject);
    }

    /**
     * 获取formData的传参
     * @param request
     * @return
     */
    private static Map<String, Object> getFormData(HttpMarsRequest request) throws Exception {
        Map<String, Object> params = request.getParameters();
        Map<String, MarsFileUpLoad> marsFileUpLoadMap = request.getFiles();

        Map<String, Object> formDataMap = new HashMap<>();
        for(String name : params.keySet()){
            Object value = params.get(name);
            if(value == null){
                continue;
            }
            formDataMap.put(name,value);
        }

        for(String name : marsFileUpLoadMap.keySet()){
            MarsFileUpLoad marsFileUpLoad = marsFileUpLoadMap.get(name);
            if(marsFileUpLoad == null){
                continue;
            }
            formDataMap.put(name,marsFileUpLoad);
        }
        return formDataMap;
    }
}
