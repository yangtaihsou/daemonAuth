package com.daemonauth.util.velocity;

import com.daemonauth.domain.enums.ResourceEnums;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @Package .velocity
 * @Description: TODO
 * @Author
 * @Date 2017/7/11
 * @Time 10:45
 * @Version V1.0
 */
public class DicUtils {


    private static final ConcurrentMap<String, Map> dicMap = new ConcurrentHashMap<String, Map>();
    private static final String formatterHtml = "<span class=\"label %s\">%s</span>";

    static {

        dicMap.put("nodeType", ResourceEnums.NodeType.asMap());

    }

    public static void regist(String key, Map value) {

        dicMap.putIfAbsent(key, value);

    }

    public static Map<Integer, Object> getDicMap(String enumName, boolean isFormat) {

        if (isFormat) {
            return dicMap.get(enumName + "_format");
        } else {
            return dicMap.get(enumName);
        }
    }

    public static Map<Integer, Object> getDicMap(String enumName) {

        return getDicMap(enumName, Boolean.FALSE);

    }
}
