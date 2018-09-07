/**
 * Copyright(c) 2004- www.360buy.com
 * Resources.java
 */
package com.daemonauth.domain;

import java.io.Serializable;

/**
 * @author
 * @date
 */
public class Resources implements Serializable {

    /**
     *
     */
    private Integer id;

    /**
     * 资源码
     */
    private String resourceCode;

    /**
     * 资源名字
     */
    private String resourceName;

    /**
     * 节点类型
     */
    private Integer nodeType;

    /**
     * 上一级资源码
     */
    private String parentCode;

    /**
     * 权限所对应的url地址
     */
    private String resourceUrl;

    /**
     * 是否展示
     */
    private Boolean display;

    /**
     * 展示顺序
     */
    private Integer displayIndex;

    /**
     * 状态
     */
    private Boolean enable;

    /**
     * 创建时间
     */
    private String createDate;

    /**
     * 修改时间
     */
    private String updateTime;


    private String resourceIcon;//资源图标

    /**
     * system编码列表
     */
    private String[] systemCodeList;
    /**
     * system编码,即根节点
     */
    private String systemCode;

    /**
     * 资源唯一标示
     */
    private String resourceId;

    /**
     * 资源码
     */
    private String resourceCodeLike;

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getResourceCode() {
        return resourceCode;
    }

    public void setResourceCode(String resourceCode) {
        this.resourceCode = resourceCode;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public Integer getNodeType() {
        return nodeType;
    }

    public void setNodeType(Integer nodeType) {
        this.nodeType = nodeType;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public Boolean getDisplay() {
        return display;
    }

    public void setDisplay(Boolean display) {
        this.display = display;
    }

    public Integer getDisplayIndex() {
        return displayIndex;
    }

    public void setDisplayIndex(Integer displayIndex) {
        this.displayIndex = displayIndex;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getResourceIcon() {
        return resourceIcon;
    }

    public void setResourceIcon(String resourceIcon) {
        this.resourceIcon = resourceIcon;
    }

    public String[] getSystemCodeList() {
        return systemCodeList;
    }

    public void setSystemCodeList(String[] systemCodeList) {
        this.systemCodeList = systemCodeList;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceCodeLike() {
        return resourceCodeLike;
    }

    public void setResourceCodeLike(String resourceCodeLike) {
        this.resourceCodeLike = resourceCodeLike;
    }
}