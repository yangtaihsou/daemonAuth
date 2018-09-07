package com.daemonauth.export.rest;

import com.daemonauth.domain.ListResult;
import com.daemonauth.domain.UsersRoles;
import com.daemonauth.domain.query.Query;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * 权限对外只读服务
 *
 * @author renhong1
 * @create 2017-10-11 16:37
 */
@Path("/service/")
@Consumes({"application/json"})
@Produces({"application/json"})
public interface AuthorityQueryResource {

    @POST
    @Path("createUser")
    ListResult<UsersRoles> findUsersListByRole(Query<UsersRoles> query);

}
