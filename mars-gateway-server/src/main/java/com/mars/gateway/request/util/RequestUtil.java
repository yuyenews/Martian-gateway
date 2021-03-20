package com.mars.gateway.request.util;

import com.mars.cloud.annotation.enums.ContentType;
import com.mars.gateway.request.param.ParamTypeConstant;
import com.mars.server.server.request.HttpMarsRequest;

public class RequestUtil {

    /**
     * 获取内容类型
     * @param request
     * @return
     */
    public static ContentType getContentType(HttpMarsRequest request){
        if(request.getMethod().toUpperCase().equals("GET")){
            return ContentType.FORM;
        } else {
            String contentStr = request.getContentType();
            if(ParamTypeConstant.isUrlEncoded(contentStr)){
                return ContentType.FORM;
            } else if(ParamTypeConstant.isFormData(contentStr)){
                return ContentType.FORM_DATA;
            } else if(ParamTypeConstant.isJSON(contentStr)){
                return ContentType.JSON;
            }
        }
        return ContentType.FORM;
    }
}
