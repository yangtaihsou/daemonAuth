package com.daemonauth.dao;

import com.daemonauth.domain.ErpSysUser;
import com.daemonauth.domain.query.PageQuery;
import com.daemonauth.domain.query.Query;

import java.util.List;

public interface ErpSysUserMapper {


    public List<ErpSysUser> queryBySelective(Query<ErpSysUser> erpSysUser);

    public long queryCountBySelective(ErpSysUser erpSysUser);

    public ErpSysUser queryByPrimaryKey(Integer id);


    public Integer save(ErpSysUser erpSysUser);

    public List<ErpSysUser> queryBySelectiveForPagination(PageQuery<ErpSysUser> erpSysUser);

    public int updateByPrimaryKey(ErpSysUser erpSysUser);

    /**
     * 根据唯一索引查询
     **/
    public ErpSysUser queryByUserErp(String userErp);

    /**
     * 根据唯一索引更新
     **/
    public int updateByUserErp(ErpSysUser erpSysUser);


}