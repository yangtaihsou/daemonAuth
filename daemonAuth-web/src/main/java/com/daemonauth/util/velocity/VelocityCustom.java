package com.daemonauth.util.velocity;

import com.daemonauth.export.GsonUtils;

import java.util.Map;

/**
 * @Package .velocity
 * @Description: TODO
 * @Author
 * @Date 2017/7/11
 * @Time 10:43
 * @Version V1.0
 */
public class VelocityCustom {

    public String getDicJson(String dicName) {

        return GsonUtils.toJson(DicUtils.getDicMap(dicName));


    }

    public String options(String dicName) {

        String optStr = "<option value=\"%s\">%s</option>\n";
        StringBuffer sb = new StringBuffer();

        Map<Integer, Object> map = DicUtils.getDicMap(dicName);

        for (Map.Entry entry : map.entrySet()) {
            sb.append(String.format(optStr, entry.getKey(), entry.getValue()));
        }
        return sb.toString();
    }
}
