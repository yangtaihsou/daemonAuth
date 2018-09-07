package com.daemonauth.domain;

import java.util.List;

/**
 * @Package .domain
 * @Description: TODO
 * @Author
 * @Date 2017/7/14
 * @Time 11:00
 * @Version V1.0
 */
public class UserDto extends Users {

    private List<String> roleList;

    private Boolean isChanged;

    public Boolean getIsChanged() {
        return isChanged;
    }

    public void setIsChanged(Boolean changed) {
        isChanged = changed;
    }

    public List<String> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<String> roleList) {
        this.roleList = roleList;
    }
}
