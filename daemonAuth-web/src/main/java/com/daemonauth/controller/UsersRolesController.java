package com.daemonauth.controller;


import com.daemonauth.domain.Roles;
import com.daemonauth.domain.Users;
import com.daemonauth.domain.UsersRoles;
import com.daemonauth.domain.query.PageQuery;
import com.daemonauth.domain.query.Query;
import com.daemonauth.service.UsersRolesService;
import com.daemonauth.service.common.AuthorityDBLoad;
import com.daemonauth.util.Constant;
import com.daemonauth.util.Result;
import com.daemonauth.util.annotion.CNotify;
import com.daemonauth.util.annotion.Pagination;
import com.daemonauth.util.exception.ErpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/usersRoles")
public class UsersRolesController {

    private static final Logger logger = LoggerFactory.getLogger(UsersRolesController.class);

    @Autowired
    private UsersRolesService usersRolesService;
    @Resource(name = "authorityDataConfig")
    private AuthorityDBLoad authorityDataConfig;

    /**
     * 获取列表
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @Pagination(pageName = "p", pageSize = Constant.pageSize)
    public String list(Model model, Integer p, PageQuery<UsersRoles> pageQuery, UsersRoles usersRoles, HttpServletRequest request) {
        p = p == null ? 1 : p;
        pageQuery.setQuery(usersRoles);
        pageQuery.setPageNo(p);
        pageQuery.setPageSize(Constant.pageSize);
        pageQuery.setStartRow((p - 1) * Constant.pageSize);
        List<UsersRoles> usersRolesList = usersRolesService.queryBySelectiveForPagination(pageQuery);
        Long count = usersRolesService.queryCountBySelectiveForPagination(pageQuery);
        model.addAttribute("count", count == null ? 0 : count.intValue());
        model.addAttribute("usersRolesList", usersRolesList);
        Constant.loadRequestParameterMapToModel(model, request);
        return "authority/usersRoles/list.jsp";
    }

    @RequestMapping(value = "/toAdd", method = RequestMethod.GET)
    public String toAdd() {
        return "authority/usersRoles/add.jsp";
    }

    /**
     * 新增
     *
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    @CNotify
    public Result save(UsersRoles usersRoles) {
        Result result = new Result();
        try {
            Boolean t = usersRolesService.save(usersRoles);
            result.setStatus(t);
            result.setReason(t == false ? "保存失败" : "");
        } catch (Exception e) {
            logger.error("", e);
            result.setStatus(Boolean.FALSE);
            if (e instanceof ErpException) {
                result.setReason(e.getMessage());
            } else {
                result.setReason("保存失败");
            }
        }
        return result;
    }

    @RequestMapping(value = "/toEdit", method = RequestMethod.GET)
    public String toEdit(Model model, Integer id) {
        UsersRoles usersRoles = usersRolesService.queryByPrimaryKey(id);
        model.addAttribute("usersRoles", usersRoles);
        return "authority/usersRoles/edit.jsp";
    }

    /**
     * 更新一个
     *
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    @CNotify
    public Result edit(UsersRoles usersRoles) {
        Result result = new Result();
        try {
            Boolean t = usersRolesService.updateByPrimaryKeySelective(usersRoles);
            result.setStatus(t);
            result.setReason(t == false ? "更新失败" : "");
        } catch (Exception e) {
            logger.error("", e);
            result.setStatus(Boolean.FALSE);
            if (e instanceof ErpException) {
                result.setReason(e.getMessage());
            } else {
                result.setReason("更新失败");
            }
        }
        return result;
    }

    /**
     * 删除一个
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    @ResponseBody
    @CNotify
    public Result delete(Integer id) {
        Result result = new Result();
        try {
            Boolean t = usersRolesService.deleteByPrimaryKey(id);
            result.setStatus(t);
            result.setReason(t == false ? "删除失败" : "");
        } catch (Exception e) {
            logger.error("", e);
            result.setStatus(Boolean.FALSE);
            if (e instanceof ErpException) {
                result.setReason(e.getMessage());
            } else {
                result.setReason("删除失败");
            }
        }
        return result;
    }

    @RequestMapping(value = "/saveBatch", method = RequestMethod.POST)
    @ResponseBody
    @CNotify
    public Result saveBatch(UsersRoles usersRoles) {
        Result result = new Result();
        try {
            String roleCodes = usersRoles.getRoleCode();
            String[] roleCodeArr = roleCodes.split(",");

            List<UsersRoles> usersRolesList = new ArrayList<UsersRoles>();
            Map<String, Roles> roles_roleCodeMap = authorityDataConfig.roles_roleCodeMap;
            Map<String, Users> users_userPinMap = authorityDataConfig.users_userPinMap;
            for (String roleCode : roleCodeArr) {
                if (roleCode != null && !roleCode.equals("")) {
                    UsersRoles usersRoles1 = new UsersRoles();
                    usersRoles1.setUserPin(usersRoles.getUserPin());
                    usersRoles1.setUserName(users_userPinMap.get(usersRoles.getUserPin()).getUserName());
                    usersRoles1.setRoleCode(roleCode);
                    String roleName = roles_roleCodeMap.get(roleCode).getRoleName();
                    usersRoles1.setRoleName(roleName);
                    usersRolesList.add(usersRoles1);
                }
            }

            String userPin = usersRoles.getUserPin();
            Query query = new Query();
            UsersRoles queryUsersRoles = new UsersRoles();
            queryUsersRoles.setUserPin(userPin);
            query.setQuery(queryUsersRoles);
            List<UsersRoles> deleteRolesResourcesList = usersRolesService.queryBySelective(query);
            Boolean t = usersRolesService.batchDeleteAndSave(usersRolesList, deleteRolesResourcesList);
            result.setStatus(t);
            result.setReason(t == false ? "保存失败" : "");
        } catch (Exception e) {
            logger.error("", e);
            result.setStatus(Boolean.FALSE);
            if (e instanceof ErpException) {
                result.setReason(e.getMessage());
            } else {
                result.setReason("保存失败");
            }
        }
        return result;
    }

    /**
     * 更新状态
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "updateEnable", method = RequestMethod.PUT)
    @ResponseBody
    @CNotify
    public Result updateEnable(Integer id, Boolean enable) {
        Result result = new Result();
        try {
            UsersRoles usersRoles = new UsersRoles();
            usersRoles.setId(id);
            usersRoles.setEnable(enable);
            Boolean t = usersRolesService.updateByPrimaryKeySelective(usersRoles);
            result.setStatus(t);
            result.setReason(t == false ? "更新状态失败" : "");
        } catch (Exception e) {
            logger.error("", e);
            result.setStatus(Boolean.FALSE);
            if (e instanceof ErpException) {
                result.setReason(e.getMessage());
            } else {
                result.setReason("更新状态失败");
            }
        }
        return result;
    }

}