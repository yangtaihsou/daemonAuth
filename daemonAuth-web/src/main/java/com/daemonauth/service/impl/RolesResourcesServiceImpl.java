package com.daemonauth.service.impl;

import com.daemonauth.dao.RolesResourcesMapper;
import com.daemonauth.domain.RolesResources;
import com.daemonauth.domain.query.PageQuery;
import com.daemonauth.domain.query.Query;
import com.daemonauth.service.RolesResourcesService;
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

@Service("rolesResourcesService")
public class RolesResourcesServiceImpl implements RolesResourcesService {

    private static final Logger logger = LoggerFactory.getLogger(RolesResourcesServiceImpl.class);
    @Autowired
    private RolesResourcesMapper rolesResourcesMapper;
    @Autowired
    private PlatformTransactionManager transactionManager;

    public List<RolesResources> findAll() {
        List<RolesResources> rolesResourcesList = rolesResourcesMapper.findAll();
        return rolesResourcesList;
    }

    public Long findCount() {
        Long count = rolesResourcesMapper.findCount();
        return count;
    }

    public List<RolesResources> queryBySelective(Query<RolesResources> rolesResources) {
        List<RolesResources> rolesResourcesList = rolesResourcesMapper.queryBySelective(rolesResources);
        return rolesResourcesList;
    }

    public Long queryCountBySelective(Query<RolesResources> rolesResources) {
        Long count = rolesResourcesMapper.queryCountBySelective(rolesResources);
        return count;
    }

    public RolesResources queryByPrimaryKey(Integer id) {
        RolesResources rolesResources = rolesResourcesMapper.queryByPrimaryKey(id);
        return rolesResources;
    }

    public Boolean deleteByPrimaryKey(Integer id) {
        int result = rolesResourcesMapper.deleteByPrimaryKey(id);
        return result == 0 ? false : true;

    }

    public Boolean updateByPrimaryKeySelective(RolesResources rolesResources) {
        int result = rolesResourcesMapper.updateByPrimaryKeySelective(rolesResources);
        return result == 0 ? false : true;
    }

    public Boolean save(RolesResources rolesResources) throws Exception {
        try {
            Integer id = rolesResourcesMapper.save(rolesResources);
            return id == 0 ? false : true;
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException) {
                ErpException erpException = new ErpException("", "请检查是否唯一");
                e = erpException;
            }
            throw e;
        }
    }

    public List<RolesResources> queryBySelectiveForPagination(PageQuery<RolesResources> rolesResources) {
        List<RolesResources> rolesResourcesList = rolesResourcesMapper.queryBySelectiveForPagination(rolesResources);
        return rolesResourcesList;
    }

    public Long queryCountBySelectiveForPagination(PageQuery<RolesResources> rolesResources) {
        Long count = rolesResourcesMapper.queryCountBySelectiveForPagination(rolesResources);
        return count;
    }

    public Boolean deleteByUniqueIndexresourceCoderoleCode(String resourceCode, String roleCode) {
        RolesResources rolesResources = new RolesResources();
        rolesResources.setResourceCode(resourceCode);
        rolesResources.setRoleCode(roleCode);
        int result = rolesResourcesMapper.deleteByUniqueIndexresourceCoderoleCode(rolesResources);
        return result == 0 ? false : true;
    }

    public Boolean deleteByCommonIndexroleCode(String roleCode) {
        RolesResources rolesResources = new RolesResources();
        rolesResources.setRoleCode(roleCode);
        int result = rolesResourcesMapper.deleteByCommonIndexroleCode(rolesResources);
        return result == 0 ? false : true;
    }

    public Boolean deleteByCommonIndexresourceCode(String resourceCode) {
        RolesResources rolesResources = new RolesResources();
        rolesResources.setResourceCode(resourceCode);
        int result = rolesResourcesMapper.deleteByCommonIndexresourceCode(rolesResources);
        return result == 0 ? false : true;
    }

    protected TransactionStatus initTansactionStatus(
            PlatformTransactionManager transactionManager, int propagetion) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();// 事务定义类
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        return transactionManager.getTransaction(def);
    }


    @Override
    public Boolean batchSave(List<RolesResources> rolesResourcesList) {
        // 事务控制
        TransactionStatus status = null;
        try {
            // 开始事务
            status = this.initTansactionStatus(transactionManager,
                    TransactionDefinition.PROPAGATION_REQUIRED);
            for (RolesResources rolesResources : rolesResourcesList) {
                rolesResourcesMapper.save(rolesResources);
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
    public Boolean batchDelete(List<RolesResources> rolesResourcesList) {
        // 事务控制
        TransactionStatus status = null;
        try {
            // 开始事务
            status = this.initTansactionStatus(transactionManager,
                    TransactionDefinition.PROPAGATION_REQUIRED);
            for (RolesResources rolesResources : rolesResourcesList) {
                rolesResourcesMapper.deleteByUniqueIndexresourceCoderoleCode(rolesResources);
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
    public Boolean batchDeleteAndSave(List<RolesResources> rolesResourcesList, List<RolesResources> deleteRolesResourcesList) {
        // 事务控制
        TransactionStatus status = null;
        try {
            // 开始事务
            status = this.initTansactionStatus(transactionManager,
                    TransactionDefinition.PROPAGATION_REQUIRED);
            for (RolesResources rolesResources : deleteRolesResourcesList) {
                rolesResourcesMapper.deleteByUniqueIndexresourceCoderoleCode(rolesResources);
            }
            if (rolesResourcesList.size() > 0) {
                for (RolesResources rolesResources : rolesResourcesList) {
                    rolesResourcesMapper.save(rolesResources);
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