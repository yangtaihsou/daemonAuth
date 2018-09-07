package com.daemonauth.domain;

import java.util.List;

/**
 * @Package .dto
 * @Description: TODO
 * @Author
 * @Date 2017/7/14
 * @Time 10:24
 * @Version V1.0
 */
public class RoleDto extends Roles {

    private List<String> resourceList;

    private Boolean isChanged;


    public List<String> getResourceList() {
        return resourceList;
    }

    public void setResourceList(List<String> resourceList) {
        this.resourceList = resourceList;
    }

    public Boolean getIsChanged() {
        return isChanged;
    }

    public void setIsChanged(Boolean changed) {
        isChanged = changed;
    }
}
