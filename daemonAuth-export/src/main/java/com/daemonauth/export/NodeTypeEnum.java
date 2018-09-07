package com.daemonauth.export;

/**
 * User:
 * Date: 15-1-12
 * Time: 下午3:44
 */
public enum NodeTypeEnum {
    ROOT_NODE(1, "根结点"),
    BRANCH_NODE(2, "枝节点(menu目录)"),
    LEAF_NODE(3, "树叶节点(menu最后一级)"),
    RESOURCE_NODE(4, "功能按钮(在页面里显示)");

    private final int type;
    private final String typeName;

    private NodeTypeEnum(int type, String typeName) {
        this.type = type;
        this.typeName = typeName;
    }

    public int getType() {
        return type;
    }

    public String getTypeName() {
        return typeName;
    }
}
