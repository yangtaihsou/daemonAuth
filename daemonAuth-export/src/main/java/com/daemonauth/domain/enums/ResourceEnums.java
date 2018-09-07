package com.daemonauth.domain.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @Package .domain.enums
 * @Description: TODO
 * @Author
 * @Date 2017/7/11
 * @Time 16:55
 * @Version V1.0
 */
public class ResourceEnums {

    public enum NodeType {
/*        ROOT_NODE(1,"根结点"),
        BRANCH_NODE(2,"枝节点"),
        LEAF_NODE(3,"树叶"),
        RESOURCE_NODE(4,"资源节点");*/

        SYS(1, "系统节点"),
        PATH(2, "目录节点(枝节点)"),
        RES(3, "叶子节点(menu最后一级)"),
        FUNC(4, "功能按钮(在页面里显示)");
        private Integer code;
        private String name;

        NodeType(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        public static Map<Integer, String> asMap() {

            Map<Integer, String> map = new HashMap<Integer, String>();
            for (ResourceEnums.NodeType enumObj : ResourceEnums.NodeType.values()) {
                map.put(enumObj.getCode(), enumObj.getName());
            }

            return map;
        }

        public Integer getCode() {
            return this.code;
        }

        public String getName() {
            return name;
        }
    }
}
