package com.daemonauth.service;

import com.daemonauth.service.common.BaseService;
import com.daemonauth.domain.Resources;

public interface ResourcesService extends BaseService<Resources> {

    public Boolean deleteByUniqueIndexresourceCode(String resourceCode);

    public Resources queryResourceByRscode(String resourceCode);
}