package com.daemonauth.controller;

import com.daemonauth.domain.Resources;
import com.daemonauth.domain.Roles;
import com.daemonauth.domain.RolesResources;
import com.daemonauth.domain.query.PageQuery;
import com.daemonauth.domain.query.Query;
import com.daemonauth.service.RolesResourcesService;
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
@RequestMapping("/rolesResources")
public class RolesResourcesController {

    private static final Logger logger = LoggerFactory.getLogger(RolesResourcesController.class);

    @Autowired
    private RolesResourcesService rolesResourcesService;
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
    public String list(Model model, Integer p, PageQuery<RolesResources> pageQuery, RolesResources rolesResources, HttpServletRequest request) {
        p = p == null ? 1 : p;
        pageQuery.setQuery(rolesResources);
        pageQuery.setPageNo(p);
        pageQuery.setPageSize(Constant.pageSize);
        pageQuery.setStartRow((p - 1) * Constant.pageSize);
        List<RolesResources> rolesResourcesList = rolesResourcesService.queryBySelectiveForPagination(pageQuery);
        Long count = rolesResourcesService.queryCountBySelectiveForPagination(pageQuery);
        model.addAttribute("count", count == null ? 0 : count.intValue());
        model.addAttribute("rolesResourcesList", rolesResourcesList);
        Constant.loadRequestParameterMapToModel(model, request);
        return "authority/rolesResources/list.jsp";
    }

    @RequestMapping(value = "/toAdd", method = RequestMethod.GET)
    public String toAdd() {
        return "authority/rolesResources/add.jsp";
    }

    /**
     * 新增
     *
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    @CNotify
    public Result save(RolesResources rolesResources) {
        Result result = new Result();
        try {
            String systemCode = authorityDataConfig.getSystemCodeByResourceCode(rolesResources.getResourceCode());
            rolesResources.setSystemCode(systemCode);
            Boolean t = rolesResourcesService.save(rolesResources);
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
     * 新增
     *
     * @return
     */
    @RequestMapping(value = "/saveBatch", method = RequestMethod.POST)
    @ResponseBody
    @CNotify
    public Result saveBatch(RolesResources rolesResources) {
        Result result = new Result();
        try {
            String resourceCodes = rolesResources.getResourceCode();
            String[] resourceCodeArr = resourceCodes.split(",");
            List<RolesResources> rolesResourcesList = new ArrayList<RolesResources>();
            Map<String, Resources> resources_rourceCodetimap = authorityDataConfig.resources_resourceCodeMap;
            Map<String, Roles> roles_roleCodeMap = authorityDataConfig.roles_roleCodeMap;
            String roleName = roles_roleCodeMap.get(rolesResources.getRoleCode()).getRoleName();
            for (String resourceCode : resourceCodeArr) {
                if (resourceCode != null && !resourceCode.equals("")) {
                    RolesResources rolesResources1 = new RolesResources();
                    rolesResources1.setResourceCode(resourceCode);
                    rolesResources1.setResourceName(resources_rourceCodetimap.get(resourceCode).getResourceName());
                    rolesResources1.setRoleCode(rolesResources.getRoleCode());
                    rolesResources1.setRoleName(roleName);
                    rolesResourcesList.add(rolesResources1);
                }
            }
            String roleCode = rolesResources.getRoleCode();
            Query query = new Query();
            RolesResources queryRolesResources = new RolesResources();
            queryRolesResources.setRoleCode(roleCode);
            query.setQuery(queryRolesResources);
            List<RolesResources> deleteRolesResourcesList = rolesResourcesService.queryBySelective(query);
            Boolean t = rolesResourcesService.batchDeleteAndSave(rolesResourcesList, deleteRolesResourcesList);
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
        RolesResources rolesResources = rolesResourcesService.queryByPrimaryKey(id);
        model.addAttribute("rolesResources", rolesResources);
        return "authority/rolesResources/edit";
    }


    /**
     * 更新一个
     *
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    @CNotify
    public Result edit(RolesResources rolesResources) {
        Result result = new Result();
        try {
            String systemCode = authorityDataConfig.getSystemCodeByResourceCode(rolesResources.getResourceCode());//这里主要是对存量数据进行更新
            rolesResources.setSystemCode(systemCode);
            Boolean t = rolesResourcesService.updateByPrimaryKeySelective(rolesResources);
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
            Boolean t = rolesResourcesService.deleteByPrimaryKey(id);
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
            RolesResources rolesResources = new RolesResources();
            rolesResources.setId(id);
            rolesResources.setEnable(enable);
            Boolean t = rolesResourcesService.updateByPrimaryKeySelective(rolesResources);
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