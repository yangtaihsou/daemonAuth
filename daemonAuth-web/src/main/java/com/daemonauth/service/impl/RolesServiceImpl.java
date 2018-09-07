package com.daemonauth.service.impl;

import com.daemonauth.service.common.AuthorityDBLoad;
import com.daemonauth.dao.RolesMapper;
import com.daemonauth.dao.RolesResourcesMapper;
import com.daemonauth.domain.RoleDto;
import com.daemonauth.domain.Roles;
import com.daemonauth.domain.RolesResources;
import com.daemonauth.domain.query.PageQuery;
import com.daemonauth.domain.query.Query;
import com.daemonauth.service.RolesService;
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

import javax.annotation.Resource;
import java.util.List;

@Service("rolesService")
public class RolesServiceImpl implements RolesService {

    private static final Logger logger = LoggerFactory.getLogger(RolesServiceImpl.class);
    @Autowired
    private RolesMapper rolesMapper;

    @Autowired
    private RolesResourcesMapper rolesResourcesMapper;
    @Autowired
    private PlatformTransactionManager transactionManager;
    @Resource(name = "authorityDataConfig")
    private AuthorityDBLoad authorityDataConfig;

    public List<Roles> findAll() {
        List<Roles> rolesList = rolesMapper.findAll();
        return rolesList;
    }

    public Long findCount() {
        Long count = rolesMapper.findCount();
        return count;
    }

    public List<Roles> queryBySelective(Query<Roles> roles) {
        List<Roles> rolesList = rolesMapper.queryBySelective(roles);
        return rolesList;
    }

    public Long queryCountBySelective(Query<Roles> roles) {
        Long count = rolesMapper.queryCountBySelective(roles);
        return count;
    }

    public Roles queryByPrimaryKey(Integer id) {
        Roles roles = rolesMapper.queryByPrimaryKey(id);
        return roles;
    }

    public Boolean deleteByPrimaryKey(Integer id) {
        int result = rolesMapper.deleteByPrimaryKey(id);
        return result == 0 ? false : true;

    }

    public Boolean updateByPrimaryKeySelective(Roles roles) {
        int result = rolesMapper.updateByPrimaryKeySelective(roles);
        return result == 0 ? false : true;
    }

    public Boolean save(Roles roles) throws Exception {
        try {
            Integer id = rolesMapper.save(roles);
            return id == 0 ? false : true;
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException) {
                ErpException erpException = new ErpException("", "请检查是否唯一");
                e = erpException;
            }
            throw e;
        }
    }

    public List<Roles> queryBySelectiveForPagination(PageQuery<Roles> roles) {
        List<Roles> rolesList = rolesMapper.queryBySelectiveForPagination(roles);
        return rolesList;
    }

    public Long queryCountBySelectiveForPagination(PageQuery<Roles> roles) {
        Long count = rolesMapper.queryCountBySelectiveForPagination(roles);
        return count;
    }

    public Boolean deleteByUniqueIndexroleCode(String roleCode) {
        Roles roles = new Roles();
        roles.setRoleCode(roleCode);
        int result = rolesMapper.deleteByUniqueIndexroleCode(roles);
        return result == 0 ? false : true;
    }

    public Boolean deleteByCommonIndexsystemCode(String systemCode) {
        Roles roles = new Roles();
        roles.setSystemCode(systemCode);
        int result = rolesMapper.deleteByCommonIndexsystemCode(roles);
        return result == 0 ? false : true;
    }

    protected TransactionStatus initTansactionStatus(
            PlatformTransactionManager transactionManager, int propagetion) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();// 事务定义类
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        return transactionManager.getTransaction(def);
    }

    @Override
    public Boolean batchSave(List<Roles> rolesList) {
        // 事务控制
        TransactionStatus status = null;
        try {
            // 开始事务
            status = this.initTansactionStatus(transactionManager,
                    TransactionDefinition.PROPAGATION_REQUIRED);
            for (Roles roles : rolesList) {
                rolesMapper.save(roles);
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
    public Boolean batchDelete(List<Roles> rolesList) {
        // 事务控制
        TransactionStatus status = null;
        try {
            // 开始事务
            status = this.initTansactionStatus(transactionManager,
                    TransactionDefinition.PROPAGATION_REQUIRED);
            for (Roles roles : rolesList) {
                rolesMapper.deleteByUniqueIndexroleCode(roles);
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
    public Boolean batchDeleteAndSave(List<Roles> rolesList, List<Roles> deleteRolesList) {
        // 事务控制
        TransactionStatus status = null;
        try {
            // 开始事务
            status = this.initTansactionStatus(transactionManager,
                    TransactionDefinition.PROPAGATION_REQUIRED);
            for (Roles roles : rolesList) {
                rolesMapper.deleteByUniqueIndexroleCode(roles);
                rolesMapper.save(roles);
            }
            transactionManager.commit(status);
            return true;
        } catch (Exception e) {
            transactionManager.rollback(status);
            logger.error("批量删除又保存失败", e);
            return false;
        }
    }

    @Override
    public Boolean saveAndBatchSaveRoleResource(RoleDto roleDto) {
        // 事务控制
        TransactionStatus status = null;
        try {
            // 开始事务
            status = this.initTansactionStatus(transactionManager,
                    TransactionDefinition.PROPAGATION_REQUIRED);

            rolesMapper.save(roleDto);
            batchSaveRoleResources(roleDto);

            transactionManager.commit(status);
            return true;
        } catch (Exception e) {
            transactionManager.rollback(status);
            logger.error("批量删除又保存失败", e);
            return false;
        }

    }

    /**
     * 批量保存角色权限关联表
     *
     * @param roleDto
     */
    private void batchSaveRoleResources(RoleDto roleDto) {

        if (null != roleDto.getResourceList() && roleDto.getResourceList().size() > 0) {

            for (String codeName : roleDto.getResourceList()) {

                String[] codeNameArray = codeName.split(":");
                RolesResources rolesResources = new RolesResources();
                rolesResources.setResourceCode(codeNameArray[0]);
                rolesResources.setResourceName(codeNameArray[1]);
                rolesResources.setRoleCode(roleDto.getRoleCode());
                rolesResources.setRoleName(roleDto.getRoleName());
                String systemCode = authorityDataConfig.getSystemCodeByResourceCode(rolesResources.getResourceCode());
                rolesResources.setSystemCode(systemCode);
                rolesResourcesMapper.save(rolesResources);
            }
        }
    }

    @Override
    public Boolean updateAndBatchSaveRoleResource(RoleDto roleDto) {
        // 事务控制
        TransactionStatus status = null;
        try {
            // 开始事务
            status = this.initTansactionStatus(transactionManager,
                    TransactionDefinition.PROPAGATION_REQUIRED);

            rolesMapper.updateByPrimaryKeySelective(roleDto);


            if (roleDto.getIsChanged()) {
                RolesResources delVo = new RolesResources();
                delVo.setRoleCode(roleDto.getRoleCode());
                rolesResourcesMapper.deleteByCommonIndexroleCode(delVo);
                batchSaveRoleResources(roleDto);
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