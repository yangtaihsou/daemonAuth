package com.daemonauth.service;

import com.daemonauth.service.common.BaseService;
import com.daemonauth.domain.UsersRoles;

public interface UsersRolesService extends BaseService<UsersRoles> {

    public Boolean deleteByUniqueIndexuserPinroleCode(String userPin, String roleCode);


    public Boolean deleteByCommonIndexroleCode(String roleCode);

    public Boolean deleteByCommonIndexuserPin(String userPin);

}