package com.daemonauth.controller;

import com.daemonauth.util.interceptor.context.LoginContext;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.daemonauth.domain.UserDto;
import com.daemonauth.domain.Users;
import com.daemonauth.domain.UsersRoles;
import com.daemonauth.domain.query.PageQuery;
import com.daemonauth.service.UsersService;
import com.daemonauth.service.common.AuthorityDBLoad;
import com.daemonauth.util.Constant;
import com.daemonauth.util.PageRes;
import com.daemonauth.util.R;
import com.daemonauth.util.Result;
import com.daemonauth.util.annotion.CNotify;
import com.daemonauth.util.annotion.Pagination;
import com.daemonauth.util.exception.ErpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UsersController {

    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

    @Autowired
    private UsersService usersService;


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
    public String list(Model model, Integer p, PageQuery<Users> pageQuery, Users users, HttpServletRequest request) {
        p = p == null ? 1 : p;
        pageQuery.setQuery(users);
        pageQuery.setPageNo(p);
        pageQuery.setPageSize(Constant.pageSize);
        pageQuery.setStartRow((p - 1) * Constant.pageSize);
        List<Users> usersList = usersService.queryBySelectiveForPagination(pageQuery);
        Long count = usersService.queryCountBySelectiveForPagination(pageQuery);
        model.addAttribute("count", count == null ? 0 : count.intValue());
        model.addAttribute("usersList", usersList);
        Constant.loadRequestParameterMapToModel(model, request);
        return "authority/users/list.jsp";
    }

    @RequestMapping(value = "/toAdd", method = RequestMethod.GET)
    public String toAdd() {
        return "authority/users/add.jsp";
    }


    /**
     * 新增
     *
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    @CNotify
    public Result save(Users users) {
        Result result = new Result();
        try {
            Boolean t = usersService.save(users);
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
        Users users = usersService.queryByPrimaryKey(id);
        model.addAttribute("users", users);
        return "authority/users/edit.jsp";
    }


    /**
     * 更新一个
     *
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    @CNotify
    public Result edit(Users users) {
        Result result = new Result();
        try {
            Boolean t = usersService.updateByPrimaryKeySelective(users);
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
            Boolean t = usersService.deleteByPrimaryKey(id);
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
            Users users = new Users();
            users.setId(id);
            users.setEnable(enable);
            users.setOperateErp(LoginContext.getLoginContext().getPin());
            Boolean t = usersService.updateByPrimaryKeySelective(users);
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

    /**
     * 分页查询
     *
     * @param pageQuery
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/pageQuery")
    public R pageQuery(PageQuery<Users> pageQuery, Users users, Integer limit) {

        pageQuery.setQuery(users);
        pageQuery.setPageSize(limit);
        pageQuery.setStartRow((pageQuery.getPageNo() - 1) * limit);

        List<Users> resourcesList = usersService.queryBySelectiveForPagination(pageQuery);
        Long count = usersService.queryCountBySelectiveForPagination(pageQuery);

        PageRes pageRes = new PageRes(resourcesList, count == null ? 0 : count.intValue(), pageQuery);

        return R.ok().put("page", pageRes);
    }


    /**
     * 信息
     */
    @ResponseBody
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id) {

        Users users = usersService.queryByPrimaryKey(id);

        Multimap<String, UsersRoles> usersRoles_pinMultimap = authorityDataConfig.usersRoles_userPinMultimap;
        List<UsersRoles> usersRolesList = (List<UsersRoles>) usersRoles_pinMultimap.get(users.getUserPin());

        List<String> roleIds = Lists.newArrayList();

        for (UsersRoles usersRoles : usersRolesList) {
            roleIds.add(usersRoles.getRoleCode() + ":" + usersRoles.getRoleName());
        }

        return R.ok().put("user", users).put("roleIds", roleIds);
    }


    /**
     * 创建角色批量保存对应资源
     *
     * @return
     */
    @RequestMapping(value = "/batchSave", method = RequestMethod.POST)
    @ResponseBody
    @CNotify
    public Result batchSave(@RequestBody UserDto userDto) {
        Result result = new Result();
        if (userDto == null || userDto.getUserPin() == null || userDto.getUserName() == null ||
                userDto.getUserPin().equals("") || userDto.getUserName().equals("")) {
            result.setStatus(false);
            result.setReason("用户id和名字不能为空");
            return result;
        }
        try {
            userDto.setOperateErp(LoginContext.getLoginContext().getPin());

            Boolean t = usersService.saveAndBatchSaveUserRole(userDto);
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
     * 更新角色批量保存对应资源
     *
     * @return
     */
    @RequestMapping(value = "/batchUpdate", method = RequestMethod.POST)
    @ResponseBody
    @CNotify
    public Result batchUpdate(@RequestBody UserDto userDto) {
        Result result = new Result();
        try {
            Boolean t = usersService.updateAndBatchSaveUserRole(userDto);
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
}