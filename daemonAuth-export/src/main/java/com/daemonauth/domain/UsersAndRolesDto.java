/**
 * Copyright(c) 2004- www.360buy.com
 * UsersRoles.java
 */
package com.daemonauth.domain;

import java.io.Serializable;

/**
 * @author
 * @date
 */
public class UsersAndRolesDto implements Serializable {
    private Users users;
    private UsersRoles usersRoles;

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public UsersRoles getUsersRoles() {
        return usersRoles;
    }

    public void setUsersRoles(UsersRoles usersRoles) {
        this.usersRoles = usersRoles;
    }
}