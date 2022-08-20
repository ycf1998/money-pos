package com.money.oss.qiniu;

import com.qiniu.storage.Region;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 七牛云地区
 * @createTime : 2022-01-01 16:47:37
 */
@UtilityClass
public class QiniuRegion {

    Map<String, Region> regionMap = new HashMap<>(5);

    static {
        regionMap.put("huadong", Region.huadong());
        regionMap.put("huabei", Region.huabei());
        regionMap.put("huanan", Region.huanan());
        regionMap.put("beimei", Region.beimei());
        regionMap.put("xinjiapo", Region.xinjiapo());
    }
    
    Region getRegion(String region) {
        return regionMap.get(region);
    }
}
