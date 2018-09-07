/**
 * Copyright(c) 2004- www.360buy.com
 * Roles.java
 */
package com.daemonauth.domain;

import java.io.Serializable;

/**
 * @author
 * @date
 */
public class Roles implements Serializable {

    /**
     *
     */
    private Integer id;

    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 角色名字
     */
    private String roleName;

    /**
     * system编码
     */
    private String systemCode;

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
     * system编码列表
     */
    private String[] systemCodeList;

    /**
     * 角色编码
     */
    private String roleCodeLike;

    public String[] getSystemCodeList() {
        return systemCodeList;
    }

    public void setSystemCodeList(String[] systemCodeList) {
        this.systemCodeList = systemCodeList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
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

    public String getRoleCodeLike() {
        return roleCodeLike;
    }

    public void setRoleCodeLike(String roleCodeLike) {
        this.roleCodeLike = roleCodeLike;
    }
}