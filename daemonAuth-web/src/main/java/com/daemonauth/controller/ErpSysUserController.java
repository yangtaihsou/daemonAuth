package com.daemonauth.controller;

import com.daemonauth.util.interceptor.context.LoginContext;
import com.daemonauth.domain.ErpSysUser;
import com.daemonauth.domain.query.PageQuery;
import com.daemonauth.service.ErpSysUserService;
import com.daemonauth.util.Constant;
import com.daemonauth.util.PageRes;
import com.daemonauth.util.R;
import com.daemonauth.util.Result;
import com.daemonauth.util.annotion.CNotify;
import com.daemonauth.util.annotion.Pagination;
import com.daemonauth.util.exception.ErpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * ErpSysUser()的controller
 * User:
 */
@Controller
@RequestMapping("/erpSysUser")
public class ErpSysUserController {

    private static final Logger logger = LoggerFactory.getLogger(ErpSysUserController.class);


    @Resource(name = "erpSysUserService")
    private ErpSysUserService erpSysUserService;

    /**
     * 获取列表
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @Pagination(pageName = "p", pageSize = Constant.pageSize)
    public String list(Model model, Integer p, PageQuery<ErpSysUser> pageQuery, ErpSysUser erpSysUser, HttpServletRequest request) {
        p = p == null ? 1 : p;
        pageQuery.setQuery(erpSysUser);
        pageQuery.setPageNo(p.intValue());
        pageQuery.setPageSize(Constant.pageSize);
        pageQuery.setStartRow((p - 1) * Constant.pageSize);
        List<ErpSysUser> erpSysUserList = erpSysUserService.queryBySelectiveForPagination(pageQuery);
        Long count = erpSysUserService.queryCountBySelectiveForPagination(pageQuery);
        model.addAttribute("count", count == null ? 0 : count.intValue());
        model.addAttribute("erpSysUserList", erpSysUserList);

        Constant.loadRequestParameterMapToModel(model, request);
        return "authority/erpSysUser/list.jsp";
    }


    @RequestMapping(value = "/toAdd", method = RequestMethod.GET)
    public String toAdd() {
        return "authority/erpSysUser/add.jsp";
    }

    /**
     * 新增
     *
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    @CNotify
    public Result save(ErpSysUser erpSysUser) {
        Result result = new Result();
        try {
            /*String sysCodes = String.join("",erpSysUser.getSysCodesList());
            erpSysUser.setSysCodes(sysCodes);*/
            erpSysUser.setOperateErp(LoginContext.getLoginContext().getPin());
            if (erpSysUser.getUserType() == 0) {
                erpSysUser.setSysCodes("");
            } else {
                if (erpSysUser.getSysCodes() == null || erpSysUser.getSysCodes().equals("")) {
                    result.setStatus(false);
                    result.setReason("请选择系统");
                    return result;
                }
            }
            Boolean t = erpSysUserService.save(erpSysUser);
            result.setStatus(t);
            result.setReason(t == false ? "保存失败" : "");
        } catch (Exception e) {
            logger.error("保存运营系统的用户异常:", e);
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


    @RequestMapping(value = "/toEdit", method = RequestMethod.GET)
    public String toEdit(Model model, Integer id) {
        ErpSysUser erpSysUser = erpSysUserService.queryByPrimaryKey(id);
        model.addAttribute("erpSysUser", erpSysUser);
        return "authority/erpSysUser/edit.jsp";
    }

    /**
     * 更新一个
     *
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    @CNotify
    public Result edit(ErpSysUser erpSysUser) {
        Result result = new Result();
        try {

            erpSysUser.setOperateErp(LoginContext.getLoginContext().getPin());
            Boolean t = erpSysUserService.updateByPrimaryKeySelective(erpSysUser);
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
            ErpSysUser erpSysUser = new ErpSysUser();
            erpSysUser.setId(id);
            erpSysUser.setEnable(enable);
            Boolean t = erpSysUserService.updateByPrimaryKeySelective(erpSysUser);
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
    public R pageQuery(PageQuery<ErpSysUser> pageQuery, ErpSysUser erpSysUser, Integer limit) {

        pageQuery.setQuery(erpSysUser);
        pageQuery.setPageSize(limit);
        pageQuery.setStartRow((pageQuery.getPageNo() - 1) * limit);

        List<ErpSysUser> erpSysUserList = erpSysUserService.queryBySelectiveForPagination(pageQuery);
        Long count = erpSysUserService.queryCountBySelectiveForPagination(pageQuery);

        PageRes pageRes = new PageRes(erpSysUserList, count == null ? 0 : count.intValue(), pageQuery);

        return R.ok().put("page", pageRes);
    }

    /**
     * 信息
     */
    @ResponseBody
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id) {
        ErpSysUser erpSysUser = erpSysUserService.queryByPrimaryKey(id);

        return R.ok().put("erpSysUser", erpSysUser);
    }

}