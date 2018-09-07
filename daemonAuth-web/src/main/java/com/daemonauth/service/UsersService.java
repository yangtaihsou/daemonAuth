package com.daemonauth.service;

import com.daemonauth.domain.Users;
import com.daemonauth.service.common.BaseService;
import com.daemonauth.domain.UserDto;
import com.daemonauth.domain.UsersRoles;

public interface UsersService extends BaseService<Users> {

    public Boolean deleteByUniqueIndexuserPin(String userPin);


    Boolean saveAndBatchSaveUserRole(UserDto userDto) throws Exception;

    Boolean updateAndBatchSaveUserRole(UserDto userDto) throws Exception;

    public Boolean batchUpdateUserRole(UserDto userDto, String systemCode) throws Exception;

    Boolean saveUserAnUserRole(Users users, UsersRoles usersRoles) throws Exception;
}