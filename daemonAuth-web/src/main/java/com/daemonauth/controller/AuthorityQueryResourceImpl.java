package com.daemonauth.controller;

import com.daemonauth.domain.ListResult;
import com.daemonauth.domain.UsersRoles;
import com.daemonauth.domain.enums.ResultInfoEnum;
import com.daemonauth.domain.query.Query;
import com.daemonauth.export.GsonUtils;
import com.daemonauth.export.rest.AuthorityQueryResource;
import com.daemonauth.service.UsersRolesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * 权限只读服务实现类
 *
 * @author renhong1
 * @create 2017-10-11 16:50
 */
@Controller
public class AuthorityQueryResourceImpl implements AuthorityQueryResource {

    private static final Logger logger = LoggerFactory.getLogger(AuthorityQueryResourceImpl.class);

    @Autowired
    private UsersRolesService usersRolesService;

    @Override
    public ListResult<UsersRoles> findUsersListByRole(Query<UsersRoles> query) {
        logger.info("查询权限组对应的用户,查询条件：{}", GsonUtils.toJson(query));
        ListResult<UsersRoles> resultList = new ListResult<UsersRoles>();
        try {
            List<UsersRoles> list = usersRolesService.queryBySelective(query);
            resultList.setList(list);
            resultList.setInfo(ResultInfoEnum.SUCCESS);
        } catch (Exception e) {
            resultList.setInfo(ResultInfoEnum.UNKNOW_ERROR);
            logger.info("查询权限组对应的用户异常:", e);
        }
        return resultList;
    }
}
