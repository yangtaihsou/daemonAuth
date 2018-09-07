package com.daemonauth.service.common;

import com.daemonauth.domain.query.PageQuery;
import com.daemonauth.domain.query.Query;

import java.util.List;

/**
 * User:
 * Date: 15-1-7
 * Time: 下午5:01
 */
public interface BaseService<T> {
    public List<T> findAll();

    public Long findCount();

    public List<T> queryBySelective(Query<T> Query);

    public Long queryCountBySelective(Query<T> query);


    public T queryByPrimaryKey(Integer id);

    public Boolean deleteByPrimaryKey(Integer id);

    public Boolean updateByPrimaryKeySelective(T t);

    public Boolean save(T t) throws Exception;

    public Boolean batchSave(List<T> tList);

    public Boolean batchDelete(List<T> tList);


    // public Boolean batchDeleteAndSave(List<T> tList);

    public Boolean batchDeleteAndSave(List<T> saveList, List<T> deleteList);

    public List<T> queryBySelectiveForPagination(PageQuery<T> pageQuery);

    public Long queryCountBySelectiveForPagination(PageQuery<T> query);

}
