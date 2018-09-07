/**
 * Copyright(c) 2004- www.360buy.com
 * RolesResources.java
 */
package com.daemonauth.domain;

import java.io.Serializable;

/**
 * @author
 * @date
 */
public class RolesResources implements Serializable {

    /**
     *
     */
    private Integer id;

    /**
     * 资源码
     */
    private String resourceCode;

    /**
     *
     */
    private String resourceName;

    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 角色名字
     */
    private String roleName;

    /**
     * 状态
     */
    private Boolean enable;

    /**
     *
     */
    private String createDate;

    /**
     *
     */
    private String updateTime;

    /**
     * system编码,即根节点
     */
    private String systemCode;
    /**
     * system编码列表
     */
    private String[] systemCodeList;

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

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
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

    public String[] getSystemCodeList() {
        return systemCodeList;
    }

    public void setSystemCodeList(String[] systemCodeList) {
        this.systemCodeList = systemCodeList;
    }
}