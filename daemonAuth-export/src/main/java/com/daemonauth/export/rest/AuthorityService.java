package com.daemonauth.export.rest;

import com.daemonauth.domain.*;


import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/service/")
@Consumes({"application/json"})
@Produces({"application/json"})
public interface AuthorityService {

    @POST
    @Path("queryBySelective")
    public ListResult<Users> queryBySelective(Users users);


    @POST
    @Path("createUser")
    public Result createUser(Users users);

    @POST
    @Path("createUsersRoles")
    public Result createUsersRoles(UsersRoles users);


    /**
     * 创建用户，及绑定用户的角色
     *
     * @param usersAndRolesDto
     * @return
     */
    @POST
    @Path("createUsersAndRoles")
    public Result createUsersAndRoles(UsersAndRolesDto usersAndRolesDto);


    /**
     * 创建用户，及绑定用户的角色
     *
     * @param userDto
     * @return
     */
    @POST
    @Path("updateUsersAndRoles")
    public Result updateUsersAndRoles(UserDto userDto, String systemCode);
}
