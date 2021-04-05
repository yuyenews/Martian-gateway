package com.mars.gateway.core.notice;

import com.mars.cloud.constant.MarsCloudConstant;
import com.mars.cloud.core.blanced.PollingIndexManager;
import com.mars.cloud.core.cache.ServerApiCache;
import com.mars.cloud.core.cache.ServerApiCacheManager;
import com.mars.cloud.core.util.NoticeUtil;
import com.mars.cloud.model.RestApiCacheModel;
import com.mars.cloud.util.MarsCloudConfigUtil;
import com.mars.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * 通知器
 */
public class GateNotice {

    private Logger marsLogger = LoggerFactory.getLogger(GateNotice.class);

    /**
     * 本地缓存
     */
    private ServerApiCache serverApiCache = new ServerApiCache();

    /**
     * 广播接口
     */
    public void notice() throws Exception {
        try {
            marsLogger.info("感染接口中.......");

            /* 获取传染渠道 */
            String contagions = MarsCloudConfigUtil.getMarsCloudConfig().getCloudConfig().getContagions();
            if(StringUtil.isNull(contagions)){
                throw new Exception("传染渠道不可以为空, 否则本服务将被孤立");
            }

            /* 从传染渠道获取所有服务接口 */
            String[] contagionList = contagions.split(",");
            boolean isSuccess = getApis(contagionList);
            if(isSuccess){
                marsLogger.info("感染接口成功.......");
            }

            /* 初始化轮询下标 */
            PollingIndexManager.initPollingMap();
        } catch (Exception e){
            throw new Exception("接口传染失败", e);
        }
    }

    /**
     * 获取所有服务接口
     * @param contagionList
     * @throws Exception
     */
    private boolean getApis(String[] contagionList) throws Exception {
        List<String> cacheServerList = ServerApiCacheManager.getAllServerList(true);
        if (cacheServerList != null && cacheServerList.size() > 0) {
            /* 优先从自己缓存的服务上获取接口 */
            String url = NoticeUtil.getRandomUrl(cacheServerList);
            for (int i = 0; i < cacheServerList.size(); i++) {
                String getApisUrl = url + "/" + MarsCloudConstant.GET_APIS;
                boolean isSuccess = getRemoteApis(getApisUrl);
                if (isSuccess) {
                    /* 从任意服务器上拉取成功，就停止 */
                    return true;
                }
                url = NoticeUtil.getRandomUrl(cacheServerList);
            }
        }

        /* 如果从自己缓存的服务上没有获取到接口，则从配置的服务商拉取 */
        String contagion = NoticeUtil.getRandomUrl(contagionList);
        for (int i = 0; i < contagionList.length; i++) {
            String getApisUrl = contagion + "/" + MarsCloudConstant.GET_APIS;
            boolean isSuccess = getRemoteApis(getApisUrl);
            if (isSuccess) {
                /* 从任意服务器上拉取成功，就停止 */
                return true;
            }
            contagion = NoticeUtil.getRandomUrl(contagionList);
        }
        return false;
    }

    /**
     * 从其他服务器拉取接口
     * @param getApisUrl
     * @return
     */
    private boolean getRemoteApis(String getApisUrl) {
        try {
            Map<String, List<RestApiCacheModel>> remoteCacheModelMap = NoticeUtil.getApis(getApisUrl);
            if(remoteCacheModelMap == null || remoteCacheModelMap.size() < 1){
                return false;
            }
            serverApiCache.saveRestApiCacheModelMap(remoteCacheModelMap);
            return true;
        } catch (Exception e) {
            marsLogger.warn("拉取接口异常");
            return false;
        }
    }
}
