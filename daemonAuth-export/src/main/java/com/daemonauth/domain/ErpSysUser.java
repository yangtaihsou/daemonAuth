/**
 * Copyright(c) 2004- www.360buy.com
 * ErpSysUser.java
 */
package com.daemonauth.domain;

import java.io.Serializable;

/**
 * @author
 * @date
 */
public class ErpSysUser implements Serializable {


    /**
     *
     */

    private Integer id;


    /**
     * 用户pin
     */

    private String userErp;


    /**
     * 用户名字
     */

    private String username;


    /**
     * 0超级管理员,1业务运营管理员(一般一个系统默认一个管理员)
     */

    private Integer userType;


    /**
     * 用户的系统编码(可以拥有多个系统，超级管理员默认所有)
     */

    private String sysCodes;


    /**
     * 添加此条记录的erp
     */

    private String operateErp;


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


    private String userErpLike;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getUserErp() {
        return userErp;
    }

    public void setUserErp(String userErp) {
        this.userErp = userErp;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }


    public String getSysCodes() {
        return sysCodes;
    }

    public void setSysCodes(String sysCodes) {
        this.sysCodes = sysCodes;
    }


    public String getOperateErp() {
        return operateErp;
    }

    public void setOperateErp(String operateErp) {
        this.operateErp = operateErp;
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

    public String getUserErpLike() {
        return userErpLike;
    }

    public void setUserErpLike(String userErpLike) {
        this.userErpLike = userErpLike;
    }
}