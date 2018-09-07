package com.daemonauth.service.impl;

import com.daemonauth.domain.query.PageQuery;
import com.daemonauth.domain.query.Query;
import com.daemonauth.dao.ResourcesMapper;
import com.daemonauth.domain.Resources;
import com.daemonauth.service.ResourcesService;
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

@Service("resourcesService")
public class ResourcesServiceImpl implements ResourcesService {

    private static final Logger logger = LoggerFactory.getLogger(ResourcesServiceImpl.class);
    @Autowired
    private ResourcesMapper resourcesMapper;
    @Autowired
    private PlatformTransactionManager transactionManager;

    public List<Resources> findAll() {
        List<Resources> resourcesList = resourcesMapper.findAll();
        return resourcesList;
    }

    public Long findCount() {
        Long count = resourcesMapper.findCount();
        return count;
    }

    public List<Resources> queryBySelective(Query<Resources> resources) {
        List<Resources> resourcesList = resourcesMapper.queryBySelective(resources);
        return resourcesList;
    }

    public Long queryCountBySelective(Query<Resources> resources) {
        Long count = resourcesMapper.queryCountBySelective(resources);
        return count;
    }

    public Resources queryByPrimaryKey(Integer id) {
        Resources resources = resourcesMapper.queryByPrimaryKey(id);
        return resources;
    }

    public Boolean deleteByPrimaryKey(Integer id) {
        int result = resourcesMapper.deleteByPrimaryKey(id);
        return result == 0 ? false : true;

    }

    public Boolean updateByPrimaryKeySelective(Resources resources) {
        int result = resourcesMapper.updateByPrimaryKeySelective(resources);
        return result == 0 ? false : true;
    }

    public Boolean save(Resources resources) throws Exception {
        try {
            Integer id = resourcesMapper.save(resources);
            return id == 0 ? false : true;
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException) {
                ErpException erpException = new ErpException("", "请检查是否唯一");
                e = erpException;
            }
            throw e;
        }
    }

    public List<Resources> queryBySelectiveForPagination(PageQuery<Resources> resources) {
        List<Resources> resourcesList = resourcesMapper.queryBySelectiveForPagination(resources);
        return resourcesList;
    }

    public Long queryCountBySelectiveForPagination(PageQuery<Resources> resources) {
        Long count = resourcesMapper.queryCountBySelectiveForPagination(resources);
        return count;
    }

    public Boolean deleteByUniqueIndexresourceCode(String resourceCode) {
        Resources resources = new Resources();
        resources.setResourceCode(resourceCode);
        int result = resourcesMapper.deleteByUniqueIndexresourceCode(resources);
        return result == 0 ? false : true;
    }

    @Override
    public Resources queryResourceByRscode(String resourceCode) {
        return resourcesMapper.queryByUniqueIndexresourceCode(resourceCode);
    }

    protected TransactionStatus initTansactionStatus(
            PlatformTransactionManager transactionManager, int propagetion) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();// 事务定义类
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        return transactionManager.getTransaction(def);
    }


    @Override
    public Boolean batchSave(List<Resources> resourcesList) {
        // 事务控制
        TransactionStatus status = null;
        try {
            // 开始事务
            status = this.initTansactionStatus(transactionManager,
                    TransactionDefinition.PROPAGATION_REQUIRED);
            for (Resources resources : resourcesList) {
                resourcesMapper.save(resources);
            }
            transactionManager.commit(status);
            return true;
        } catch (Exception e) {
            transactionManager.rollback(status);
            logger.error("批量保存失败", e);
            throw new RuntimeException("批量保存失败,详情=" + e.getMessage());
        }
    }

    @Override
    public Boolean batchDelete(List<Resources> resourcesList) {
        // 事务控制
        TransactionStatus status = null;
        try {
            // 开始事务
            status = this.initTansactionStatus(transactionManager,
                    TransactionDefinition.PROPAGATION_REQUIRED);
            for (Resources resources : resourcesList) {
                resourcesMapper.deleteByUniqueIndexresourceCode(resources);
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
    public Boolean batchDeleteAndSave(List<Resources> resourcesList, List<Resources> deleteRsourcesList) {
        // 事务控制
        TransactionStatus status = null;
        try {
            // 开始事务
            status = this.initTansactionStatus(transactionManager,
                    TransactionDefinition.PROPAGATION_REQUIRED);
            for (Resources resources : resourcesList) {
                resourcesMapper.deleteByUniqueIndexresourceCode(resources);
                resourcesMapper.save(resources);
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