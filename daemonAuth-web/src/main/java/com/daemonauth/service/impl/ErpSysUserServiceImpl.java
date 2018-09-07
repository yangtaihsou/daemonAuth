package com.daemonauth.service.impl;

import com.daemonauth.dao.ErpSysUserMapper;
import com.daemonauth.domain.ErpSysUser;
import com.daemonauth.domain.query.PageQuery;
import com.daemonauth.domain.query.Query;
import com.daemonauth.service.ErpSysUserService;
import com.daemonauth.util.exception.ErpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("erpSysUserService")
public class ErpSysUserServiceImpl implements ErpSysUserService {

    private static final Logger logger = LoggerFactory.getLogger(ErpSysUserServiceImpl.class);
    @Autowired
    private ErpSysUserMapper erpSysUserMapper;


    @Override
    public List<ErpSysUser> findAll() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Long findCount() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<ErpSysUser> queryBySelective(Query<ErpSysUser> erpSysUser) {
        List<ErpSysUser> erpSysUserList = erpSysUserMapper.queryBySelective(erpSysUser);
        return erpSysUserList;
    }

    public Long queryCountBySelective(Query<ErpSysUser> erpSysUser) {
        Long count = erpSysUserMapper.queryCountBySelective(erpSysUser.getQuery());
        return count;
    }

    public ErpSysUser queryByPrimaryKey(Integer id) {
        ErpSysUser erpSysUser = erpSysUserMapper.queryByPrimaryKey(id);
        return erpSysUser;
    }

    public Boolean deleteByPrimaryKey(Integer id) {
        return null;

    }

    public Boolean updateByPrimaryKeySelective(ErpSysUser erpSysUser) {
        int result = erpSysUserMapper.updateByPrimaryKey(erpSysUser);
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean save(ErpSysUser erpSysUser) throws Exception {
        try {
            Integer id = erpSysUserMapper.save(erpSysUser);
            return id == 0 ? false : true;
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException) {
                ErpException erpException = new ErpException("", "请检查是否唯一");
                e = erpException;
            }
            throw e;
        }
    }

    @Override
    public Boolean batchSave(List<ErpSysUser> erpSysUsers) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Boolean batchDelete(List<ErpSysUser> erpSysUsers) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Boolean batchDeleteAndSave(List<ErpSysUser> saveList, List<ErpSysUser> deleteList) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<ErpSysUser> queryBySelectiveForPagination(PageQuery<ErpSysUser> erpSysUser) {
        List<ErpSysUser> erpSysUserList = erpSysUserMapper.queryBySelectiveForPagination(erpSysUser);
        return erpSysUserList;
    }

    @Override
    public Long queryCountBySelectiveForPagination(PageQuery<ErpSysUser> query) {
        Long count = erpSysUserMapper.queryCountBySelective(query.getQuery());
        return count;
    }

    public Long queryCountBySelectiveForPagination(Query<ErpSysUser> erpSysUser) {
        Long count = erpSysUserMapper.queryCountBySelective(erpSysUser.getQuery());
        return count;
    }


}