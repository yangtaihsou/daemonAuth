/**
 * Copyright(c) 2004- www.360buy.com
 * Users.java
 */
package com.daemonauth.domain;


import java.io.Serializable;


/**
 * @author
 * @date
 */

public class Users implements Serializable {

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

    private String sysCodes;

    private String email;
    private String tel;
    private String operateErp;


    /**
     * 用户pin
     */
    private String userPinLike;

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

    public String getSysCodes() {
        return sysCodes;
    }

    public void setSysCodes(String sysCodes) {
        this.sysCodes = sysCodes;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getOperateErp() {
        return operateErp;
    }

    public void setOperateErp(String operateErp) {
        this.operateErp = operateErp;
    }

    public String getUserPinLike() {
        return userPinLike;
    }

    public void setUserPinLike(String userPinLike) {
        this.userPinLike = userPinLike;
    }
}