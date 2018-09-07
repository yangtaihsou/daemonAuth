package com.daemonauth.export.rpc;

import com.daemonauth.domain.*;
import com.daemonauth.domain.query.Query;
import com.daemonauth.export.rpc.annotion.Controller;
import com.daemonauth.export.rpc.annotion.RequestMapping;
import com.daemonauth.export.rpc.annotion.RequestMethod;

import java.util.List;

@Controller
@RequestMapping(path = "/authority")
public interface AuthorityResourceService {

    @RequestMapping(path = "/resources", method = RequestMethod.POST)
    public List<Resources> getResourcesList(Query<Resources> resourcesQuery);

    @RequestMapping(path = "/roles", method = RequestMethod.POST)
    public List<Roles> getRolesList(Query<Roles> rolesQuery);

    @RequestMapping(path = "/users", method = RequestMethod.POST)
    public List<Users> getUsersList(Query<Users> usersQuery);

    @RequestMapping(path = "/usersroles", method = RequestMethod.POST)
    public List<UsersRoles> getUsersRolesList(Query<UsersRoles> usersRolesQuery);

    @RequestMapping(path = "/rolesresources", method = RequestMethod.POST)
    public List<RolesResources> getRolesResourcesList(Query<RolesResources> rolesResourcesQuery);

    @RequestMapping(path = "/createUser", method = RequestMethod.POST)
    public Result createUser(Users users);

    @RequestMapping(path = "/createUsersRoles", method = RequestMethod.POST)
    public Result createUsersRoles(UsersRoles users);
}
