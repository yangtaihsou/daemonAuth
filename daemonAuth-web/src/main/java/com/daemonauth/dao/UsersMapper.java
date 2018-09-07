package com.daemonauth.dao;

import com.daemonauth.domain.Users;
import com.daemonauth.domain.query.PageQuery;
import com.daemonauth.domain.query.Query;

import java.util.List;

public interface UsersMapper {

    public List<Users> findAll();

    public long findCount();

    public List<Users> queryBySelective(Query<Users> users);

    public long queryCountBySelective(Query<Users> users);

    public Users queryByPrimaryKey(Integer id);

    public int deleteByPrimaryKey(Integer id);

    public int updateByPrimaryKeySelective(Users users);

    public int updateByPin(Users users);

    public Integer save(Users users);

    public List<Users> queryBySelectiveForPagination(PageQuery<Users> users);

    public long queryCountBySelectiveForPagination(PageQuery<Users> users);

    public int deleteByUniqueIndexuserPin(Users users);


}