package com.daemonauth.controller;

import com.google.common.collect.Lists;
import com.daemonauth.domain.RoleDto;
import com.daemonauth.domain.Roles;
import com.daemonauth.domain.RolesResources;
import com.daemonauth.domain.query.PageQuery;
import com.daemonauth.service.ResourcesService;
import com.daemonauth.service.RolesService;
import com.daemonauth.service.UsersRolesService;
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
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/roles")
public class RolesController {

    private static final Logger logger = LoggerFactory.getLogger(RolesController.class);

    @Autowired
    private RolesService rolesService;

    @Resource(name = "authorityDataConfig")
    private AuthorityDBLoad authorityDataConfig;


    @Autowired
    private ResourcesService resourcesService;
    @Autowired
    private UsersRolesService usersRolesService;

    /**
     * 获取列表
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @Pagination(pageName = "p", pageSize = Constant.pageSize)
    public String list(Model model, Integer p, PageQuery<Roles> pageQuery, Roles roles, HttpServletRequest request) {
        p = p == null ? 1 : p;
        pageQuery.setQuery(roles);
        pageQuery.setPageNo(p);
        pageQuery.setPageSize(Constant.pageSize);
        pageQuery.setStartRow((p - 1) * Constant.pageSize);
        List<Roles> rolesList = rolesService.queryBySelectiveForPagination(pageQuery);
        Long count = rolesService.queryCountBySelectiveForPagination(pageQuery);
        model.addAttribute("count", count == null ? 0 : count.intValue());
        model.addAttribute("rolesList", rolesList);


        model.addAttribute("rootCode", authorityDataConfig.getResources_rootCodeNameMap());
        Constant.loadRequestParameterMapToModel(model, request);
        return "authority/roles/list.jsp";
    }

    @RequestMapping(value = "/toAdd", method = RequestMethod.GET)
    public String toAdd(Model model) {
        model.addAttribute("rootCode", authorityDataConfig.getResources_rootCodeNameMap());
        return "authority/roles/add.jsp";
    }

    /**
     * 新增
     *
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    @CNotify
    public Result save(Roles roles) {
        Result result = new Result();
        try {
            Boolean t = rolesService.save(roles);
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
        Roles roles = rolesService.queryByPrimaryKey(id);
        model.addAttribute("roles", roles);
        model.addAttribute("rootCode", authorityDataConfig.getResources_rootCodeNameMap());
        return "authority/roles/edit.jsp";
    }

    /**
     * 更新一个
     *
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    @CNotify
    public Result edit(Roles roles) {
        Result result = new Result();
        try {
            Boolean t = rolesService.updateByPrimaryKeySelective(roles);
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
            Roles roles = rolesService.queryByPrimaryKey(id);
            Boolean t = rolesService.deleteByPrimaryKey(id);
            result.setStatus(t);
            result.setReason(t == false ? "删除角色失败" : "");
            t = usersRolesService.deleteByCommonIndexroleCode(roles.getRoleCode());//事物TODO
            result.setStatus(t);
            result.setReason(t == false ? "删除用户角色败" : "");
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
            Roles roles = new Roles();
            roles.setId(id);
            roles.setEnable(enable);
            Boolean t = rolesService.updateByPrimaryKeySelective(roles);
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
    public R pageQuery(PageQuery<Roles> pageQuery, Roles roles, Integer limit) {

        pageQuery.setQuery(roles);
        pageQuery.setPageSize(limit);
        pageQuery.setStartRow((pageQuery.getPageNo() - 1) * limit);
        if (roles.getSystemCode() == null || roles.getSystemCode().equals("")) {
            if (authorityDataConfig.systemCodesbyLoginUser() != null) {
                roles.setSystemCodeList(authorityDataConfig.systemCodesbyLoginUser());
            }
        }
        List<Roles> resourcesList = rolesService.queryBySelectiveForPagination(pageQuery);
        Long count = rolesService.queryCountBySelectiveForPagination(pageQuery);

        PageRes pageRes = new PageRes(resourcesList, count == null ? 0 : count.intValue(), pageQuery);

        return R.ok().put("page", pageRes).put("resourcesRootCode", authorityDataConfig.getResources_rootCodeNameMap());
    }


    /**
     * 信息
     */
    @ResponseBody
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id) {

        Roles roles = rolesService.queryByPrimaryKey(id);


        Collection<RolesResources> list = authorityDataConfig.rolesResources_roleCodeMultimap.get(roles.getRoleCode());
        List<String> resourceIds = Lists.newArrayList();
        for (RolesResources entity : list) {
            resourceIds.add(entity.getResourceCode() + ":" + entity.getResourceName());
        }

        return R.ok().put("roles", roles).put("resourceIds", resourceIds);
    }


    @ResponseBody
    @RequestMapping(value = "/resourceDic")
    public R resourceDic(HttpServletRequest request) {

        List<Map<String, Object>> list = authorityDataConfig.systemCodeList();

        return R.ok().put("resourcesRootCode", list);
    }


    /**
     * 创建角色批量保存对应资源
     *
     * @return
     */
    @RequestMapping(value = "/batchSave", method = RequestMethod.POST)
    @ResponseBody
    @CNotify
    public Result batchSave(@RequestBody RoleDto roleDto) {
        Result result = new Result();
        try {
            Boolean t = rolesService.saveAndBatchSaveRoleResource(roleDto);
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
    public Result batchUpdate(@RequestBody RoleDto roleDto) {
        Result result = new Result();
        try {
            Boolean t = rolesService.updateAndBatchSaveRoleResource(roleDto);
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