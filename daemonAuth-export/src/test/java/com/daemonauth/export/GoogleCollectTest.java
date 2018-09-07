package com.daemonauth.export;

import com.google.common.collect.HashBasedTable;

import java.util.HashMap;
import java.util.Map;

/**
 * User:
 * Date: 17-10-12
 * Time: 下午4:47
 */
public class GoogleCollectTest {

    public static void main(String[] args) {
        HashBasedTable<String, String, String> resources_userIdAndUrlHBase = HashBasedTable.create();
        resources_userIdAndUrlHBase.put("234", null, "");
        Map<String, String> resourceUrlMap = new HashMap<String, String>();
        resourceUrlMap.put(null, "qwewqe");
    }

}
