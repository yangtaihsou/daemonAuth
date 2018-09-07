package com.daemonauth.dao;

import com.daemonauth.domain.Resources;
import com.daemonauth.domain.query.PageQuery;
import com.daemonauth.domain.query.Query;

import java.util.List;

public interface ResourcesMapper {

    public List<Resources> findAll();

    public long findCount();

    public List<Resources> queryBySelective(Query<Resources> resources);

    public long queryCountBySelective(Query<Resources> resources);

    public Resources queryByPrimaryKey(Integer id);

    public int deleteByPrimaryKey(Integer id);

    public int updateByPrimaryKeySelective(Resources resources);

    public Integer save(Resources resources);

    public List<Resources> queryBySelectiveForPagination(PageQuery<Resources> resources);

    public long queryCountBySelectiveForPagination(PageQuery<Resources> resources);

    public int deleteByUniqueIndexresourceCode(Resources resources);

    public Resources queryByUniqueIndexresourceCode(String resourceCode);

    public int updateSystem();


}