package com.daemonauth.controller;

import com.daemonauth.export.NodeTypeEnum;
import com.daemonauth.VoUtil;
import com.daemonauth.domain.Resources;
import com.daemonauth.domain.query.PageQuery;
import com.daemonauth.service.ResourcesService;
import com.daemonauth.service.RolesResourcesService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/resources")
public class ResourcesController {
    private static final Logger logger = LoggerFactory.getLogger(ResourcesController.class);
    @Resource(name = "resources_resourceCodeMap")
    Map<String, Resources> resources_resourceCodeMap;
    @Autowired
    private ResourcesService resourcesService;
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
    public String list(Model model, Integer p, PageQuery<Resources> pageQuery, Resources resources, HttpServletRequest request) {
        p = p == null ? 1 : p;
        pageQuery.setQuery(resources);
        pageQuery.setPageNo(p.intValue());
        pageQuery.setPageSize(Constant.pageSize);
        pageQuery.setStartRow((p - 1) * Constant.pageSize);
        List<Resources> resourcesList = resourcesService.queryBySelectiveForPagination(pageQuery);
        Long count = resourcesService.queryCountBySelectiveForPagination(pageQuery);
        model.addAttribute("count", count == null ? 0 : count.intValue());
        model.addAttribute("resourcesList", resourcesList);
        int displayCount = Constant.pageSize;
        if (resourcesList.size() < displayCount) {
            displayCount = resourcesList.size();
        }
        if (resources != null && resources.getParentCode() != null) {//是否设置顺序
            String resourcesParentCode = resources.getParentCode();
            Resources parentResources = resources_resourceCodeMap.get(resourcesParentCode);
            if (parentResources != null) {
                if (parentResources.getNodeType().intValue() == NodeTypeEnum.ROOT_NODE.getType()) {//资源父编码为根节点时
                    model.addAttribute("editDisplay", Boolean.TRUE);
                    model.addAttribute("displayCount", displayCount);
                }
                if (parentResources.getNodeType().intValue() == NodeTypeEnum.BRANCH_NODE.getType()) {
                    resources = resources_resourceCodeMap.get(resources.getResourceCode());
                /*    if(resources.getNodeType()!=null&&(resources.getNodeType().intValue()==NodeTypeEnum.LEAF_NODE.getType()||
                            resources.getNodeType().intValue()==NodeTypeEnum.BRANCH_NODE.getType())){//资源父编码为枝节点时，且查询的节点类型是叶子节或者枝节点。
*/
                    model.addAttribute("editDisplay", Boolean.TRUE);
                    model.addAttribute("displayCount", displayCount);
                    //    }
                }

            }
        }
        Constant.loadRequestParameterMapToModel(model, request);
        return "authority/resources/list.jsp";
    }

    @RequestMapping(value = "/toAdd", method = RequestMethod.GET)
    public String toAdd() {
        return "authority/resources/add.jsp";
    }

    /**
     * 新增
     *
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    @CNotify
    public Result save(Resources resources) {
        Result result = new Result();
        try {
            if (resources.getResourceCode().equals("")) {
                result.setStatus(false);
                result.setReason("请填写资源码");
                return result;
            }
            if (resources.getNodeType() == null) {
                result.setStatus(false);
                result.setReason("请选择资源类型");
                return result;
            }
            if (resources.getNodeType() == NodeTypeEnum.ROOT_NODE.getType()) {
                resources.setSystemCode(resources.getResourceCode());
            } else {
                String systemCode = resourcesService.queryResourceByRscode(resources.getParentCode()).getSystemCode();//authorityDataConfig.getSystemCodeByResourceCode(resources.getParentCode());
                resources.setSystemCode(systemCode);
            }
            Boolean t = resourcesService.save(resources);
            result.setStatus(t);
            result.setReason(t == false ? "保存失败" : "");
        } catch (Exception e) {
            logger.error("", e);
            result.setStatus(Boolean.FALSE);
            result.setReason("保存失败");
            if (e instanceof ErpException) {
                result.setReason(e.getMessage());
            } else {
                result.setReason("保存失败");
            }
        }
        return result;
    }

    @RequestMapping(value = "/batchExport", method = RequestMethod.POST)
    @ResponseBody
    public Result batchExport(Resources resources) {
        /*  格式  bigoneyy,bigoneyy_api_approval,审批流管理,/api/approval,2,bigoneyy
         * //父编码+资源编码+资源名字+资源url+按钮节点类型+系统编码*/
        Result result = new Result();
        result.setStatus(false);
        List<Resources> resourcesList = new ArrayList<Resources>();
        String urlContext = resources.getResourceUrl();
        try {
            if (urlContext != null && !urlContext.equals("")) {
                urlContext = urlContext.replace("\n", "").replace(" ", "");
                String[] sourceArr = urlContext.split(";");
                for (String sourceStr : sourceArr) {
                    String[] sourceDetailArr = sourceStr.split(",");//存放资源url和名字
                    String parentCode = sourceDetailArr[0];
                    String code = sourceDetailArr[1];
                    String name = sourceDetailArr[2];
                    String url = sourceDetailArr[3];
                    String nodetype = sourceDetailArr[4];
                    String systemCode = sourceDetailArr[5];
                    Resources resourcesNew = new Resources();
                    resourcesNew.setNodeType(Integer.parseInt(nodetype));
                    resourcesNew.setSystemCode(systemCode);
                    resourcesNew.setResourceCode(code);
                    resourcesNew.setResourceName(name);
                    resourcesNew.setParentCode(parentCode);
                    resourcesNew.setResourceUrl(url);
                    resourcesList.add(resourcesNew);
                }
                Boolean flag = resourcesService.batchSave(resourcesList);
                result.setStatus(flag);
            } else {
                result.setStatus(false);
                result.setReason("资源集合不能为空");
            }
        } catch (Exception e) {
            logger.error("导入失败", e);
            result.setStatus(false);
            result.setReason("错误." + e.getMessage());
        }

        return result;
    }

    @RequestMapping(value = "/toEdit", method = RequestMethod.GET)
    public String toEdit(Model model, Integer id) {
        Resources resources = resourcesService.queryByPrimaryKey(id);
        model.addAttribute("resources", resources);
        return "authority/resources/edit.jsp";
    }

    /**
     * 更新一个
     *
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    @CNotify
    public Result edit(Resources resources) {
        Result result = new Result();
        try {
            if (resources.getNodeType() == NodeTypeEnum.ROOT_NODE.getType()) {
                resources.setSystemCode(resources.getResourceCode());
            } else {
                String systemCode = authorityDataConfig.getSystemCodeByResourceCode(resources.getParentCode());//这里主要是对存量数据进行更新
                resources.setSystemCode(systemCode);
            }
            Boolean t = resourcesService.updateByPrimaryKeySelective(resources);
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

    @RequestMapping(value = "/updateDisplay", method = RequestMethod.POST)
    @ResponseBody
    @CNotify
    public Result updateDisplay(VoUtil voUtil) {
        Result result = new Result();
        result.setStatus(Boolean.TRUE);
        result.setReason("保存成功");
        try {
            if (voUtil.getResourceList() != null) {
                for (Resources resources : voUtil.getResourceList()) {
                    resourcesService.updateByPrimaryKeySelective(resources);//TODO 应该使用事物处理
                }
            }
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
            Resources resources = resourcesService.queryByPrimaryKey(id);
            Boolean t = resourcesService.deleteByPrimaryKey(id);
            result.setStatus(t);
            result.setReason(t == false ? "删除资源失败" : "");
            if (resources.getNodeType() == NodeTypeEnum.RESOURCE_NODE.getType() || resources.getNodeType() == NodeTypeEnum.LEAF_NODE.getType()) {
                t = rolesResourcesService.deleteByCommonIndexresourceCode(resources.getResourceCode());//事物TODO
                result.setStatus(t);
                result.setReason(t == false ? "删除角色资源失败" : "");
            }
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
            Resources resources = new Resources();
            resources.setId(id);
            resources.setEnable(enable);
            Boolean t = resourcesService.updateByPrimaryKeySelective(resources);
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
    public R pageQuery(PageQuery<Resources> pageQuery, Resources resources, Integer limit) {

        pageQuery.setQuery(resources);
        pageQuery.setPageSize(limit);
        pageQuery.setStartRow((pageQuery.getPageNo() - 1) * limit);
        if (resources.getSystemCode() == null || resources.getSystemCode().equals("")) {
            if (authorityDataConfig.systemCodesbyLoginUser() != null) {
                resources.setSystemCodeList(authorityDataConfig.systemCodesbyLoginUser());
            }
        }
        List<Resources> resourcesList = resourcesService.queryBySelectiveForPagination(pageQuery);
        Long count = resourcesService.queryCountBySelectiveForPagination(pageQuery);
/*        if (resources.getParentCode() == null || resources.getParentCode().equals("")) {//没有选择上级编码条件查询，默认返回为空
            resourcesList = null;
            count = null;
            String erp = LoginContext.getLoginContext().getPin();
            ErpSysUser erpSysUser = authorityDataConfig.erpSysUserMap.get(erp);
            if (erp.equals("bjyangkuan") || (erpSysUser!=null&& erpSysUser.getUserType() == 0)) {
                resources.setNodeType(NodeTypeEnum.ROOT_NODE.getType());
                resourcesList = resourcesService.queryBySelectiveForPagination(pageQuery);
                count = resourcesService.queryCountBySelectiveForPagination(pageQuery);
            }
        }*/
        PageRes pageRes = new PageRes(resourcesList, count == null ? 0 : count.intValue(), pageQuery);

        return R.ok().put("page", pageRes);
    }

    /**
     * 信息
     */
    @ResponseBody
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id) {
        Resources resources = resourcesService.queryByPrimaryKey(id);

        return R.ok().put("resources", resources);
    }


}