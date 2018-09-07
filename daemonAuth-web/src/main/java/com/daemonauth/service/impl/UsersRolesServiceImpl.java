package com.daemonauth.service.impl;

import com.daemonauth.domain.query.Query;
import com.daemonauth.service.UsersRolesService;
import com.daemonauth.dao.UsersRolesMapper;
import com.daemonauth.domain.UsersRoles;
import com.daemonauth.domain.query.PageQuery;
import com.daemonauth.util.exception.ErpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;

@Service("usersRolesService")
public class UsersRolesServiceImpl implements UsersRolesService {

    private static final Logger logger = LoggerFactory.getLogger(UsersRolesServiceImpl.class);
    @Autowired
    private UsersRolesMapper usersRolesMapper;
    @Autowired
    private PlatformTransactionManager transactionManager;

    public List<UsersRoles> findAll() {
        List<UsersRoles> usersRolesList = usersRolesMapper.findAll();
        return usersRolesList;
    }

    public Long findCount() {
        Long count = usersRolesMapper.findCount();
        return count;
    }

    public List<UsersRoles> queryBySelective(Query<UsersRoles> usersRoles) {
        List<UsersRoles> usersRolesList = usersRolesMapper.queryBySelective(usersRoles);
        return usersRolesList;
    }

    public Long queryCountBySelective(Query<UsersRoles> usersRoles) {
        Long count = usersRolesMapper.queryCountBySelective(usersRoles);
        return count;
    }

    public UsersRoles queryByPrimaryKey(Integer id) {
        UsersRoles usersRoles = usersRolesMapper.queryByPrimaryKey(id);
        return usersRoles;
    }

    public Boolean deleteByPrimaryKey(Integer id) {
        int result = usersRolesMapper.deleteByPrimaryKey(id);
        return result == 0 ? false : true;

    }

    public Boolean updateByPrimaryKeySelective(UsersRoles usersRoles) {
        int result = usersRolesMapper.updateByPrimaryKeySelective(usersRoles);
        return result == 0 ? false : true;
    }

    public Boolean save(UsersRoles usersRoles) throws Exception {
        try {
            Integer id = usersRolesMapper.save(usersRoles);
            return id == 0 ? false : true;
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException) {
                ErpException erpException = new ErpException("", "请检查是否唯一");
                e = erpException;
            }
            throw e;
        }
    }

    public List<UsersRoles> queryBySelectiveForPagination(PageQuery<UsersRoles> usersRoles) {
        List<UsersRoles> usersRolesList = usersRolesMapper.queryBySelectiveForPagination(usersRoles);
        return usersRolesList;
    }

    public Long queryCountBySelectiveForPagination(PageQuery<UsersRoles> usersRoles) {
        Long count = usersRolesMapper.queryCountBySelectiveForPagination(usersRoles);
        return count;
    }

    public Boolean deleteByUniqueIndexuserPinroleCode(String userPin, String roleCode) {
        UsersRoles usersRoles = new UsersRoles();
        usersRoles.setUserPin(userPin);
        usersRoles.setRoleCode(roleCode);
        int result = usersRolesMapper.deleteByUniqueIndexuserPinroleCode(usersRoles);
        return result == 0 ? false : true;
    }

    public Boolean deleteByCommonIndexroleCode(String roleCode) {
        UsersRoles usersRoles = new UsersRoles();
        usersRoles.setRoleCode(roleCode);
        int result = usersRolesMapper.deleteByCommonIndexroleCode(usersRoles);
        return result == 0 ? false : true;
    }

    public Boolean deleteByCommonIndexuserPin(String userPin) {
        UsersRoles usersRoles = new UsersRoles();
        usersRoles.setUserPin(userPin);
        int result = usersRolesMapper.deleteByCommonIndexuserPin(usersRoles);
        return result == 0 ? false : true;
    }

    protected TransactionStatus initTansactionStatus(
            PlatformTransactionManager transactionManager, int propagetion) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();// 事务定义类
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        return transactionManager.getTransaction(def);
    }


    @Override
    public Boolean batchSave(List<UsersRoles> usersRolesList) {
        // 事务控制
        TransactionStatus status = null;
        try {
            // 开始事务
            status = this.initTansactionStatus(transactionManager,
                    TransactionDefinition.PROPAGATION_REQUIRED);
            for (UsersRoles usersRoles : usersRolesList) {
                usersRolesMapper.save(usersRoles);
            }
            transactionManager.commit(status);
            return true;
        } catch (Exception e) {
            transactionManager.rollback(status);
            logger.error("批量保存失败", e);
            return false;
        }
    }

    @Override
    public Boolean batchDelete(List<UsersRoles> usersRolesList) {
        // 事务控制
        TransactionStatus status = null;
        try {
            // 开始事务
            status = this.initTansactionStatus(transactionManager,
                    TransactionDefinition.PROPAGATION_REQUIRED);
            for (UsersRoles usersRoles : usersRolesList) {
                usersRolesMapper.deleteByUniqueIndexuserPinroleCode(usersRoles);
            }
            transactionManager.commit(status);
            return true;
        } catch (Exception e) {
            transactionManager.rollback(status);
            logger.error("批量删除失败", e);
            return false;
        }
    }

    @Override
    public Boolean batchDeleteAndSave(List<UsersRoles> usersRolesList, List<UsersRoles> deleteUsersRolesList) {
        // 事务控制
        TransactionStatus status = null;
        try {
            // 开始事务
            status = this.initTansactionStatus(transactionManager,
                    TransactionDefinition.PROPAGATION_REQUIRED);
            for (UsersRoles usersRoles : deleteUsersRolesList) {
                usersRolesMapper.deleteByUniqueIndexuserPinroleCode(usersRoles);
            }
            if (usersRolesList.size() > 0) {
                for (UsersRoles usersRoles : usersRolesList) {
                    usersRolesMapper.save(usersRoles);
                }
            }
            transactionManager.commit(status);
            return true;
        } catch (Exception e) {
            transactionManager.rollback(status);
            logger.error("批量删除又保存失败", e);
            return false;
        }
    }
}