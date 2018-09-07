package com.daemonauth.dao;

import com.daemonauth.domain.RolesResources;
import com.daemonauth.domain.query.PageQuery;
import com.daemonauth.domain.query.Query;

import java.util.List;

public interface RolesResourcesMapper {

    public List<RolesResources> findAll();

    public long findCount();

    public List<RolesResources> queryBySelective(Query<RolesResources> rolesResources);

    public long queryCountBySelective(Query<RolesResources> rolesResources);

    public RolesResources queryByPrimaryKey(Integer id);

    public int deleteByPrimaryKey(Integer id);

    public int updateByPrimaryKeySelective(RolesResources rolesResources);

    public Integer save(RolesResources rolesResources);

    public List<RolesResources> queryBySelectiveForPagination(PageQuery<RolesResources> rolesResources);

    public long queryCountBySelectiveForPagination(PageQuery<RolesResources> rolesResources);

    public int deleteByUniqueIndexresourceCoderoleCode(RolesResources rolesResources);


    public int deleteByCommonIndexroleCode(RolesResources rolesResources);

    public int deleteByCommonIndexresourceCode(RolesResources rolesResources);
}