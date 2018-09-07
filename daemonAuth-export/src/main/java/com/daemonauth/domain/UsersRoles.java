/**
 * Copyright(c) 2004- www.360buy.com
 * UsersRoles.java
 */
package com.daemonauth.domain;

import java.io.Serializable;

/**
 * @author
 * @date
 */
public class UsersRoles implements Serializable {

    /**
     *
     */
    private Integer id;

    /**
     * 用户pin
     */
    private String userPin;

    /**
     * 用户名字
     */
    private String userName;

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
     * 角色所属系统
     */
    private String systemCode;

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

    public String getUserPin() {
        return userPin;
    }

    public void setUserPin(String userPin) {
        this.userPin = userPin;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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


}