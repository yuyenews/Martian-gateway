package com.mars.gateway.api.filter;

import com.mars.gateway.common.filter.GateFilter;
import com.mars.gateway.config.MarsGateWayConfig;
import com.mars.gateway.core.util.GateWayConfigUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 过滤器工厂
 */
public class GateFactory {

    /**
     * 获取过滤器
     * @return
     */
    public static List<GateFilter> getGateFilter(){
        List<GateFilter> gateFilterList = getDefaultFilter();

        MarsGateWayConfig marsGateWayConfig = GateWayConfigUtil.getMarsGateWayConfig();
        if(marsGateWayConfig == null){
            return gateFilterList;
        }
        List<GateFilter> customFilter = marsGateWayConfig.getGateFilter();
        if(customFilter == null || customFilter.size() < 1){
            return gateFilterList;
        }
        for(GateFilter gateFilter : customFilter){
            gateFilterList.add(gateFilter);
        }

        return gateFilterList;
    }

    /**
     * 获取默认过滤器
     * @return
     */
    private static List<GateFilter> getDefaultFilter(){
        List<GateFilter> gateFilterList = new ArrayList<>();
        gateFilterList.add(new DefaultGateFilter());
        return gateFilterList;
    }
}
