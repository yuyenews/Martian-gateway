package com.mars.gateway.api;

import com.mars.aio.server.MarsServerHandler;
import com.mars.aio.server.impl.MarsHttpExchange;
import com.mars.cloud.constant.MarsCloudConstant;
import com.mars.cloud.model.HttpResultModel;
import com.mars.cloud.util.SerializableCloudUtil;
import com.mars.common.util.JSONUtil;
import com.mars.gateway.api.filter.GateFactory;
import com.mars.gateway.common.filter.GateFilter;
import com.mars.gateway.api.model.RequestInfoModel;
import com.mars.gateway.api.util.RequestAndResultUtil;
import com.mars.gateway.request.RequestServer;
import com.mars.server.server.request.HttpMarsRequest;
import com.mars.server.server.request.HttpMarsResponse;
import com.mars.server.server.request.impl.HttpMarsDefaultRequest;
import com.mars.server.server.request.impl.HttpMarsDefaultResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

/**
 * 核心控制器，用来实现转发和响应
 */
public class GateWayDispatcher implements MarsServerHandler {

    private Logger log = LoggerFactory.getLogger(GateWayDispatcher.class);

    @Override
    public void request(MarsHttpExchange httpExchange) {
        HttpMarsRequest request = new HttpMarsDefaultRequest(httpExchange);
        HttpMarsResponse response = new HttpMarsDefaultResponse(httpExchange);

        try {

            /* 获取过滤器 */
            List<GateFilter> gateFilterList = GateFactory.getGateFilter();

            /* 执行过滤器 */
            Object result = execFilter(gateFilterList, request, response);
            if(result != null && !result.toString().equals(GateFilter.SUCCESS)){
                response.send(JSONUtil.toJSONString(result));
                return;
            }

            String requestUri = request.getUrl();
            RequestInfoModel requestInfoModel = RequestAndResultUtil.getServerNameAndMethodName(requestUri);

            HttpResultModel httpResultModel = RequestServer.doRequest(requestInfoModel, request);
            String fileName = getFileName(httpResultModel);

            Object resultData = null;
            if(fileName.equals(MarsCloudConstant.RESULT_FILE_NAME)){
                /* 如果返回的不是文件，则将数据反序列化出来 */
                resultData = SerializableCloudUtil.deSerialization(httpResultModel.getInputStream(), Object.class);
            }
            responseData(gateFilterList, request, response, resultData, httpResultModel.getInputStream(), fileName);
        } catch (Exception e) {
            log.error("处理请求失败!", e);
            RequestAndResultUtil.send(response,"处理请求发生错误"+e.getMessage());
        }
    }

    /**
     * 响应数据
     * @param gateFilterList
     * @param request
     * @param response
     * @param resultData
     * @param resultStream
     * @param fileName
     */
    private void responseData(List<GateFilter> gateFilterList, HttpMarsRequest request, HttpMarsResponse response, Object resultData, InputStream resultStream, String fileName){

        /* 执行过滤器的 响应方法 */
        Object filterResult = execFilterResult(gateFilterList, request, response, resultData, resultStream);
        if(filterResult != null && !filterResult.toString().equals(GateFilter.SUCCESS)){
            response.send(JSONUtil.toJSONString(filterResult));
            return;
        }

        /* 响应给客户端 */
        if(fileName.equals(MarsCloudConstant.RESULT_FILE_NAME)){
            response.send(JSONUtil.toJSONString(resultData));
        } else {
            response.downLoad(fileName, resultStream);
        }
    }

    /**
     * 执行过滤器
     * @param request
     * @param response
     * @return
     */
    private Object execFilter(List<GateFilter> gateFilterList, HttpMarsRequest request, HttpMarsResponse response){
        for(GateFilter gateFilter : gateFilterList){
            Object result = gateFilter.doFilter(request, response);
            if(result != null && !result.toString().equals(GateFilter.SUCCESS)){
                return result;
            }
        }
        return GateFilter.SUCCESS;
    }

    /**
     * 执行过滤器
     * @param request
     * @param response
     * @return
     */
    private Object execFilterResult(List<GateFilter> gateFilterList, HttpMarsRequest request, HttpMarsResponse response, Object resultData, InputStream resultStream){
        for(GateFilter gateFilter : gateFilterList){
            Object result = gateFilter.doResult(request, response, resultData, resultStream);
            if(result != null && !result.toString().equals(GateFilter.SUCCESS)){
                return result;
            }
        }
        return GateFilter.SUCCESS;
    }

    /**
     * 获取文件名称
     * @param httpResultModel
     * @return
     */
    private String getFileName(HttpResultModel httpResultModel){
        String fileName = httpResultModel.getFileName();
        if(fileName == null){
            fileName = UUID.randomUUID().toString();
        } else {
            String[] fileNames = fileName.split("=");
            if(fileNames == null || fileNames.length < 2){
                fileName = UUID.randomUUID().toString();
            } else {
                fileName = fileNames[1];
            }
        }
        return fileName;
    }
}
