package com.daemonauth.dao;

import com.daemonauth.domain.UsersRoles;
import com.daemonauth.domain.query.PageQuery;
import com.daemonauth.domain.query.Query;

import java.util.List;

public interface UsersRolesMapper {

    public List<UsersRoles> findAll();

    public long findCount();

    public List<UsersRoles> queryBySelective(Query<UsersRoles> usersRoles);

    public long queryCountBySelective(Query<UsersRoles> usersRoles);

    public UsersRoles queryByPrimaryKey(Integer id);

    public int deleteByPrimaryKey(Integer id);

    public int updateByPrimaryKeySelective(UsersRoles usersRoles);

    public Integer save(UsersRoles usersRoles);

    public List<UsersRoles> queryBySelectiveForPagination(PageQuery<UsersRoles> usersRoles);

    public long queryCountBySelectiveForPagination(PageQuery<UsersRoles> usersRoles);

    public int deleteByUniqueIndexuserPinroleCode(UsersRoles usersRoles);

    public int deleteByUserPinAndSysCode(UsersRoles usersRoles);


    public int deleteByCommonIndexroleCode(UsersRoles usersRoles);

    public int deleteByCommonIndexuserPin(UsersRoles usersRoles);
}