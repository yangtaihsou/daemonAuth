package com.daemonauth.dao;

import com.daemonauth.domain.Roles;
import com.daemonauth.domain.query.PageQuery;
import com.daemonauth.domain.query.Query;

import java.util.List;

public interface RolesMapper {

    public List<Roles> findAll();

    public long findCount();

    public List<Roles> queryBySelective(Query<Roles> roles);

    public long queryCountBySelective(Query<Roles> roles);

    public Roles queryByPrimaryKey(Integer id);

    public int deleteByPrimaryKey(Integer id);

    public int updateByPrimaryKeySelective(Roles roles);

    public Integer save(Roles roles);

    public List<Roles> queryBySelectiveForPagination(PageQuery<Roles> roles);

    public long queryCountBySelectiveForPagination(PageQuery<Roles> roles);

    public int deleteByUniqueIndexroleCode(Roles roles);


    public int deleteByCommonIndexsystemCode(Roles roles);
}