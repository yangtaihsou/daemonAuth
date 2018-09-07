package com.daemonauth.controller;

import com.daemonauth.domain.*;
import com.daemonauth.service.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.daemonauth.domain.query.Query;
import com.daemonauth.export.GsonUtils;

import com.daemonauth.util.exception.ErpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * User:
 * Date: 15-1-28
 * Time: 下午4:52
 * TODO 以下涉及到查询的地方，都应该添加systemcode进行查询。减轻查询压力、传输压力和客户端缓存压力
 */
@Controller
@RequestMapping(value = "/authority")
public class AuthorityResourceController {

    private final static Logger log = LoggerFactory.getLogger(AuthorityController.class);
    @Autowired
    ResourcesController resourcesController;
    @Autowired
    private ResourcesService resourcesService;
    @Autowired
    private RolesService rolesService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private UsersRolesService usersRolesService;
    @Autowired
    private RolesResourcesService rolesResourcesService;

    @RequestMapping(value = "/resources", method = RequestMethod.POST)
    @ResponseBody
    public List<Resources> getResourcesList(Query<Resources> resourcesQuery, HttpServletRequest request) throws IOException {
        if (resourcesQuery == null || resourcesQuery.getQuery() == null)
            resourcesQuery = (Query<Resources>) parseJsonToObject(new TypeToken<Query<Resources>>() {
            }.getType(), request);
        if (resourcesQuery.getQuery() == null) {
            Resources resources = new Resources();
            resourcesQuery.setQuery(resources);
        }
        resourcesQuery.getQuery().setEnable(Boolean.TRUE);
        log.info("resourcesQuery=" + GsonUtils.toJson(resourcesQuery.getQuery()));
        return resourcesService.queryBySelective(resourcesQuery);
    }

    @RequestMapping(value = "/roles", method = RequestMethod.POST)
    @ResponseBody
    public List<Roles> getRolesList(Query<Roles> rolesQuery, HttpServletRequest request) throws IOException {
        if (rolesQuery == null || rolesQuery.getQuery() == null)
            rolesQuery = (Query<Roles>) parseJsonToObject(new TypeToken<Query<Roles>>() {
            }.getType(), request);
        if (rolesQuery.getQuery() == null) {
            Roles resources = new Roles();
            rolesQuery.setQuery(resources);
        }
        rolesQuery.getQuery().setEnable(Boolean.TRUE);
        log.info("rolesQuery=" + GsonUtils.toJson(rolesQuery.getQuery()));
        List<Roles> rolesList = rolesService.queryBySelective(rolesQuery);
        return rolesList;
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    @ResponseBody
    public List<Users> getUsersList(Query<Users> usersQuery, HttpServletRequest request) throws IOException {
        if (usersQuery == null || usersQuery.getQuery() == null)
            usersQuery = (Query<Users>) parseJsonToObject(new TypeToken<Query<Users>>() {
            }.getType(), request);
        if (usersQuery.getQuery() == null) {
            Users resources = new Users();
            usersQuery.setQuery(resources);
        }
        usersQuery.getQuery().setEnable(Boolean.TRUE);
        log.info("usersQuery=" + GsonUtils.toJson(usersQuery.getQuery()));
        return usersService.queryBySelective(usersQuery);
    }

    @RequestMapping(value = "/usersroles", method = RequestMethod.POST)
    @ResponseBody
    public List<UsersRoles> getUsersRolesList(Query<UsersRoles> usersRolesQuery, HttpServletRequest request) throws IOException {
        if (usersRolesQuery == null || usersRolesQuery.getQuery() == null)
            usersRolesQuery = (Query<UsersRoles>) parseJsonToObject(new TypeToken<Query<UsersRoles>>() {
            }.getType(), request);
        if (usersRolesQuery.getQuery() == null) {
            UsersRoles resources = new UsersRoles();
            usersRolesQuery.setQuery(resources);
        }
        usersRolesQuery.getQuery().setEnable(Boolean.TRUE);
        log.info("usersRolesQuery=" + GsonUtils.toJson(usersRolesQuery.getQuery()));
        //TODO 去掉新版adminlte改版，多添加的系统节点记录
        List<UsersRoles> usersRolesList = usersRolesService.queryBySelective(usersRolesQuery);
        List<UsersRoles> filterUsersRolesList = new ArrayList<UsersRoles>();
        if (usersRolesList != null && usersRolesList.size() > 0) {
            for (UsersRoles usersRoles : usersRolesList) {
                String roleCode = usersRoles.getRoleCode();
                String systemCode = usersRoles.getSystemCode();
                if (roleCode == null || roleCode.equals("licaiErp")) {
                    System.out.println("角色编码是系统编码" + roleCode);
                }
                if (systemCode == null) {
                    filterUsersRolesList.add(usersRoles);
                } else if (!roleCode.equals(systemCode)) {
                    filterUsersRolesList.add(usersRoles);
                }
            }
        }
        return filterUsersRolesList;
    }

    @RequestMapping(value = "/rolesresources", method = RequestMethod.POST)
    @ResponseBody
    public List<RolesResources> getRolesResourcesList(Query<RolesResources> rolesResourcesQuery, HttpServletRequest request) throws IOException {
        if (rolesResourcesQuery == null || rolesResourcesQuery.getQuery() == null)
            rolesResourcesQuery = (Query<RolesResources>) parseJsonToObject(new TypeToken<Query<RolesResources>>() {
            }.getType(), request);
        if (rolesResourcesQuery.getQuery() == null) {
            RolesResources resources = new RolesResources();
            rolesResourcesQuery.setQuery(resources);
        }
        rolesResourcesQuery.getQuery().setEnable(Boolean.TRUE);
        log.info("rolesResourcesQuery=" + GsonUtils.toJson(rolesResourcesQuery.getQuery()));
        //TODO 去掉新版adminlte改版，多添加的系统节点记录

        List<RolesResources> rolesResourcesList = rolesResourcesService.queryBySelective(rolesResourcesQuery);
        List<RolesResources> filterRolesResourcesList = new ArrayList<RolesResources>();
        if (rolesResourcesList != null && rolesResourcesList.size() > 0) {
            for (RolesResources rolesResources : rolesResourcesList) {
                String resourceCode = rolesResources.getResourceCode();
                String systemCode = rolesResources.getSystemCode();
                if (systemCode == null) {
                    filterRolesResourcesList.add(rolesResources);
                } else if (!resourceCode.equals(systemCode)) {
                    filterRolesResourcesList.add(rolesResources);
                }
            }
        }
        return filterRolesResourcesList;
    }

    @RequestMapping(value = "/createUser", method = RequestMethod.POST)
    @ResponseBody
    public Result createUser(Users users, HttpServletRequest request) throws IOException {
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

    @RequestMapping(value = "/createUsersRoles", method = RequestMethod.POST)
    @ResponseBody
    public Result createUsersRoles(UsersRoles usersRoles, HttpServletRequest request) throws IOException {
        Result result = new Result();
        try {
            if (usersRoles.getSystemCode() == null || usersRoles.getSystemCode().equals("")) {//需要添加systemcode ,默认licaiErp
                usersRoles.setSystemCode("licaiErp");
            }
            Boolean t = usersRolesService.save(usersRoles);
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

    @RequestMapping(value = "/createResources", method = RequestMethod.POST)
    @ResponseBody
    public Result createResources(Resources resources, HttpServletRequest request) throws IOException {
        if (resources == null)
            resources = (Resources) parseJsonToObject(new TypeToken<Query<Resources>>() {
            }.getType(), request);
        com.daemonauth.util.Result result = resourcesController.save(resources);
        Result result1 = new Result();
        result1.setStatus(result.getStatus());
        result1.setContent(result.getReason());
        return result1;
    }

    private Object parseJsonToObject(Type type, HttpServletRequest request) throws IOException {
        InputStream inputStream = request.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        StringBuilder builder = new StringBuilder();
        String line = "";
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        String json = builder.toString();
        Gson gson = new Gson();
        Object e = gson.fromJson(json, type);
        return e;
    }
}
