package com.daemonauth.service;

import com.daemonauth.service.common.BaseService;
import com.daemonauth.domain.RoleDto;
import com.daemonauth.domain.Roles;

public interface RolesService extends BaseService<Roles> {

    public Boolean deleteByUniqueIndexroleCode(String roleCode);


    public Boolean deleteByCommonIndexsystemCode(String systemCode);

    public Boolean saveAndBatchSaveRoleResource(RoleDto roleDto);

    public Boolean updateAndBatchSaveRoleResource(RoleDto roleDto);

}