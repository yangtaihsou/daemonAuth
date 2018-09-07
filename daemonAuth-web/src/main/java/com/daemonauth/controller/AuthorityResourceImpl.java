package com.daemonauth.controller;


import com.daemonauth.domain.*;
import com.daemonauth.export.rest.AuthorityService;

import com.daemonauth.domain.enums.ResultInfoEnum;
import com.daemonauth.domain.query.Query;
import com.daemonauth.service.UsersRolesService;
import com.daemonauth.service.UsersService;
import com.daemonauth.service.common.AuthorityDBLoad;
import com.daemonauth.util.exception.ErpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.List;

/**
 * User:
 * Date: 15-1-28
 * Time: 下午4:52
 */
@Controller
public class AuthorityResourceImpl implements AuthorityService {

    @Autowired
    private UsersService usersService;

    @Autowired
    private UsersRolesService usersRolesService;
    @Resource(name = "authorityDataConfig")
    private AuthorityDBLoad authorityDataConfig;

    @Override
    public ListResult<Users> queryBySelective(Users users) {

        ListResult result = new ListResult();
        Query<Users> query = new Query<Users>();
        query.setQuery(users);
        try {
            List<Users> userses = usersService.queryBySelective(query);

            result.setList(userses);
            result.setInfo(ResultInfoEnum.SUCCESS);
        } catch (Exception e) {
            result.setInfo(ResultInfoEnum.UNKNOW_ERROR);
        }
        return result;
    }

    @Override
    public Result createUser(Users users) {
        Result result = new Result();
        try {
            Boolean t = usersService.save(users);
            result.setStatus(t);
            result.setReason(t == false ? "保存失败" : "");
        } catch (Exception e) {
            result.setStatus(Boolean.FALSE);
            if (e instanceof ErpException) {
                result.setReason(e.getMessage());
            } else {
                result.setReason("保存失败");
            }
        }
        return result;
    }

    @Override
    public Result createUsersRoles(UsersRoles users) {
        Result result = new Result();
        try {
            if (users.getSystemCode() == null || users.getSystemCode().equals("")) {
                users.setSystemCode("licaiErp");
            }
            Boolean t = usersRolesService.save(users);//需要添加systemcode ,默认licaiErp
            result.setStatus(t);
            result.setReason(t == false ? "保存失败" : "");
        } catch (Exception e) {
            result.setStatus(Boolean.FALSE);
            if (e instanceof ErpException) {
                result.setReason(e.getMessage());
            } else {
                result.setReason("保存失败");
            }
        }
        return result;
    }

    @Override
    public Result createUsersAndRoles(UsersAndRolesDto usersAndRolesDto) {
        Result result = new Result();
        try {
            Users authUsers = usersAndRolesDto.getUsers();
            UsersRoles usersRoles = usersAndRolesDto.getUsersRoles();
            usersRoles.setSystemCode(authUsers.getSysCodes());
            usersRoles.setUserPin(authUsers.getUserPin());
            usersRoles.setUserName(authUsers.getUserName());
            usersRoles.setRoleName(authorityDataConfig.roles_roleCodeMap.get(usersRoles.getRoleCode()).getRoleName());
            usersRoles.setEnable(true);
            Boolean t = usersService.saveUserAnUserRole(usersAndRolesDto.getUsers(), usersAndRolesDto.getUsersRoles());
            result.setStatus(t);
            result.setReason(t == false ? "保存失败" : "");
        } catch (Exception e) {
            result.setStatus(Boolean.FALSE);
            result.setReason(e.getMessage());
        }
        return result;
    }

    @Override
    public Result updateUsersAndRoles(UserDto userDto, String systemCode) {
        Result result = new Result();
        try {
            Boolean t = usersService.batchUpdateUserRole(userDto, systemCode);
            result.setStatus(t);
            result.setReason(t == false ? "保存失败" : "");
        } catch (Exception e) {
            result.setStatus(Boolean.FALSE);
            result.setReason(e.getMessage());
        }
        return result;
    }
}
