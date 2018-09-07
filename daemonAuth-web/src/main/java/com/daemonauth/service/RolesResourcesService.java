package com.daemonauth.service;

import com.daemonauth.service.common.BaseService;
import com.daemonauth.domain.RolesResources;

public interface RolesResourcesService extends BaseService<RolesResources> {

    public Boolean deleteByUniqueIndexresourceCoderoleCode(String resourceCode, String roleCode);


    public Boolean deleteByCommonIndexroleCode(String roleCode);

    public Boolean deleteByCommonIndexresourceCode(String resourceCode);

}