package com.daemonauth.service.impl;

import com.daemonauth.domain.*;
import com.daemonauth.util.interceptor.context.LoginContext;
import com.daemonauth.service.common.AuthorityDBLoad;
import com.google.common.base.Strings;
import com.daemonauth.dao.UsersMapper;
import com.daemonauth.dao.UsersRolesMapper;

import com.daemonauth.domain.query.PageQuery;
import com.daemonauth.domain.query.Query;
import com.daemonauth.service.UsersService;
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

@Service("usersService")
public class UsersServiceImpl implements UsersService {

    private static final Logger logger = LoggerFactory.getLogger(UsersServiceImpl.class);
    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private UsersRolesMapper usersRolesMapper;
    @Autowired
    private PlatformTransactionManager transactionManager;
    @Resource(name = "authorityDataConfig")
    private AuthorityDBLoad authorityDataConfig;

    public List<Users> findAll() {
        List<Users> usersList = usersMapper.findAll();
        return usersList;
    }

    public Long findCount() {
        Long count = usersMapper.findCount();
        return count;
    }

    public List<Users> queryBySelective(Query<Users> users) {
        List<Users> usersList = usersMapper.queryBySelective(users);
        return usersList;
    }

    public Long queryCountBySelective(Query<Users> users) {
        Long count = usersMapper.queryCountBySelective(users);
        return count;
    }

    public Users queryByPrimaryKey(Integer id) {
        Users users = usersMapper.queryByPrimaryKey(id);
        return users;
    }

    public Boolean deleteByPrimaryKey(Integer id) {
        int result = usersMapper.deleteByPrimaryKey(id);
        return result == 0 ? false : true;

    }

    public Boolean updateByPrimaryKeySelective(Users users) {
        int result = usersMapper.updateByPrimaryKeySelective(users);
        return result == 0 ? false : true;
    }

    public Boolean save(Users users) throws Exception {
        try {
            Integer id = usersMapper.save(users);
            return id == 0 ? false : true;
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException) {
                ErpException erpException = new ErpException("", "请检查是否唯一");
                e = erpException;
            }
            throw e;
        }
    }

    public List<Users> queryBySelectiveForPagination(PageQuery<Users> users) {
        List<Users> usersList = usersMapper.queryBySelectiveForPagination(users);
        return usersList;
    }

    public Long queryCountBySelectiveForPagination(PageQuery<Users> users) {
        Long count = usersMapper.queryCountBySelectiveForPagination(users);
        return count;
    }

    public Boolean deleteByUniqueIndexuserPin(String userPin) {
        Users users = new Users();
        users.setUserPin(userPin);
        int result = usersMapper.deleteByUniqueIndexuserPin(users);
        return result == 0 ? false : true;
    }

    protected TransactionStatus initTansactionStatus(
            PlatformTransactionManager transactionManager, int propagetion) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();// 事务定义类
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        return transactionManager.getTransaction(def);
    }

    @Override
    public Boolean batchSave(List<Users> usersList) {
        // 事务控制
        TransactionStatus status = null;
        try {
            // 开始事务
            status = this.initTansactionStatus(transactionManager,
                    TransactionDefinition.PROPAGATION_REQUIRED);
            for (Users users : usersList) {
                usersMapper.save(users);
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
    public Boolean batchDelete(List<Users> usersList) {
        // 事务控制
        TransactionStatus status = null;
        try {
            // 开始事务
            status = this.initTansactionStatus(transactionManager,
                    TransactionDefinition.PROPAGATION_REQUIRED);
            for (Users users : usersList) {
                usersMapper.deleteByUniqueIndexuserPin(users);
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
    public Boolean batchDeleteAndSave(List<Users> usersList, List<Users> deleteUsersList) {
        // 事务控制
        TransactionStatus status = null;
        try {
            // 开始事务
            status = this.initTansactionStatus(transactionManager,
                    TransactionDefinition.PROPAGATION_REQUIRED);
            for (Users users : usersList) {
                usersMapper.deleteByUniqueIndexuserPin(users);
                usersMapper.save(users);
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
    public Boolean saveAndBatchSaveUserRole(UserDto userDto) throws Exception {
        // 事务控制
        TransactionStatus status = null;
        try {
            // 开始事务
            status = this.initTansactionStatus(transactionManager,
                    TransactionDefinition.PROPAGATION_REQUIRED);
            //userDto.setSysCodes();//TODO
            usersMapper.save(userDto);
            batchSaveUserRole(userDto);

            transactionManager.commit(status);
            return true;
        } catch (Exception e) {
            transactionManager.rollback(status);
            logger.error("批量删除又保存失败", e);
            if (e instanceof DuplicateKeyException) {
                ErpException erpException = new ErpException("", "请检查是否唯一");
                e = erpException;
            }
            throw e;
        }
    }

    @Override
    public Boolean batchUpdateUserRole(UserDto userDto, String systemCode) throws Exception {
        // 事务控制
        TransactionStatus status = null;
        try {
            // 开始事务
            status = this.initTansactionStatus(transactionManager,
                    TransactionDefinition.PROPAGATION_REQUIRED);

            if (Strings.isNullOrEmpty(userDto.getOperateErp())) {
                userDto.setOperateErp(LoginContext.getLoginContext().getPin());
            }
            usersMapper.updateByPin(userDto);


            if (userDto.getIsChanged()) {
                UsersRoles delVo = new UsersRoles();
                delVo.setUserPin(userDto.getUserPin());
                delVo.setSystemCode(systemCode);
                usersRolesMapper.deleteByUserPinAndSysCode(delVo);
                batchSaveUserRole(userDto);
            }

            transactionManager.commit(status);
            return true;
        } catch (Exception e) {
            transactionManager.rollback(status);
            logger.error("批量删除又保存失败", e);
            if (e instanceof DuplicateKeyException) {
                ErpException erpException = new ErpException("", "请检查是否唯一");
                e = erpException;
            }
            throw e;
        }
    }

    @Override
    public Boolean updateAndBatchSaveUserRole(UserDto userDto) throws Exception {
        // 事务控制
        TransactionStatus status = null;
        try {
            // 开始事务
            status = this.initTansactionStatus(transactionManager,
                    TransactionDefinition.PROPAGATION_REQUIRED);

            if (Strings.isNullOrEmpty(userDto.getOperateErp())) {
                userDto.setOperateErp(LoginContext.getLoginContext().getPin());
            }
            usersMapper.updateByPrimaryKeySelective(userDto);


            if (userDto.getIsChanged()) {
                batchDeleteUserRole(userDto);//先删除
                batchSaveUserRole(userDto);
            }

            transactionManager.commit(status);
            return true;
        } catch (Exception e) {
            transactionManager.rollback(status);
            logger.error("批量删除又保存失败", e);
            if (e instanceof DuplicateKeyException) {
                ErpException erpException = new ErpException("", "请检查是否唯一");
                e = erpException;
            }
            throw e;
        }
    }

    /**
     * 删除某个pin下、某个系统下的角色（超级用户默认全删除）
     *
     * @param userDto
     */
    private void batchDeleteUserRole(UserDto userDto) {
        ErpSysUser erpSysUser = authorityDataConfig.erpSysUserMap.get(LoginContext.getLoginContext().getPin());
        if (erpSysUser == null) {
            throw new RuntimeException("登录用户非法");
        }
        UsersRoles delVo = new UsersRoles();
        delVo.setUserPin(userDto.getUserPin());
        if (erpSysUser.getUserErp().equals("bjyangkuan") || erpSysUser.getUserType() == 0) {
            int result = usersRolesMapper.deleteByCommonIndexuserPin(delVo);//超级用户全删除
            return;
        }
        String[] sysCodeArr = erpSysUser.getSysCodes().split(",");
        for (String codeName : sysCodeArr) {
            delVo.setUserPin(userDto.getUserPin());
            delVo.setSystemCode(codeName);
            int result = usersRolesMapper.deleteByUserPinAndSysCode(delVo);
            System.out.println(result);
        }
    }

    /**
     * 批量保存角色权限关联表
     *
     * @param userDto
     */
    private void batchSaveUserRole(UserDto userDto) {
        if (null != userDto.getRoleList() && userDto.getRoleList().size() > 0) {
            for (String codeName : userDto.getRoleList()) {
                String[] codeNameArray = codeName.split(":");
                UsersRoles usersRoles = new UsersRoles();
                Roles roles = authorityDataConfig.roles_roleCodeMap.get(codeNameArray[0]);
                if (roles != null) {
                    usersRoles.setSystemCode(roles.getSystemCode());
                } else {
                    usersRoles.setSystemCode(codeNameArray[0]);
                    System.out.println(codeNameArray[0] + "所属角色为空");
                }
                usersRoles.setUserPin(userDto.getUserPin());
                usersRoles.setUserName(userDto.getUserName());
                usersRoles.setRoleCode(codeNameArray[0]);
                usersRoles.setRoleName(codeNameArray[1]);
                usersRolesMapper.save(usersRoles);
            }
        }
    }

    @Override
    public Boolean saveUserAnUserRole(Users users, UsersRoles usersRoles) throws Exception {
        // 事务控制
        TransactionStatus status = null;
        try {
            // 开始事务
            status = this.initTansactionStatus(transactionManager,
                    TransactionDefinition.PROPAGATION_REQUIRED);
            usersMapper.save(users);
            usersRolesMapper.save(usersRoles);
            transactionManager.commit(status);
            return true;
        } catch (Exception e) {
            transactionManager.rollback(status);
            String error = "保存用户及对应角色错误:" + e.getMessage();
            logger.info(error);
            throw new RuntimeException(error);
        }
    }

}