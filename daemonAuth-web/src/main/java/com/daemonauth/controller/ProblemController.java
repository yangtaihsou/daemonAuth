package com.daemonauth.controller;

import com.daemonauth.dao.ResourcesMapper;
import com.daemonauth.dao.RolesResourcesMapper;
import com.daemonauth.domain.Resources;
import com.daemonauth.domain.RolesResources;
import com.daemonauth.domain.query.Query;
import com.daemonauth.service.common.AuthorityDBLoad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ProblemController {


    @Resource(name = "authorityDataConfig")
    private AuthorityDBLoad authorityDataConfig;

    @Autowired
    private ResourcesMapper resourcesMapper;

    @Autowired
    private RolesResourcesMapper rolesResourcesMapper;

    @ResponseBody
    @RequestMapping(value = "/insertSystemCode")
    public Map<String, String> insertSystemCode() {
        Map<String, String> map = new HashMap<String, String>();
     /*   int count = resourcesMapper.updateSystem();
        map.put("1","更新一级节点编码"+count);*/
        Resources resources = new Resources();
        Query<Resources> resourcesQuery = new Query<Resources>();
        resourcesQuery.setQuery(resources);
        List<Resources> resourcesesList = resourcesMapper.queryBySelective(resourcesQuery);
        int updateCount = 0;
        for (Resources resourcesResult : resourcesesList) {
            String resourceCode = resourcesResult.getResourceCode();
            String rootCode = authorityDataConfig.getSystemCodeByResourceCode(resourceCode);//即系统节点
            if (rootCode != null && !rootCode.equals("")) {
                Resources updateResources = new Resources();
                updateResources.setId(resourcesResult.getId());
                updateResources.setSystemCode(rootCode);
                int updateResult = resourcesMapper.updateByPrimaryKeySelective(updateResources);
                updateCount = updateCount + updateResult;
            }
        }
        map.put("1", "更新节点系统编码,查询数据条数：" + resourcesesList.size() + ",实际条数:" + updateCount);
        int updateRoleResourceCount = 0;
        Query<RolesResources> rolesResourcesQuery = new Query<RolesResources>();
        RolesResources rolesResources = new RolesResources();
        rolesResourcesQuery.setQuery(rolesResources);
        List<RolesResources> rolesResourcesList = rolesResourcesMapper.queryBySelective(rolesResourcesQuery);
        for (RolesResources rolesResourcesResult : rolesResourcesList) {
            String resourceCode = rolesResourcesResult.getResourceCode();
            Resources resourcesResult = resourcesMapper.queryByUniqueIndexresourceCode(resourceCode);
            if (resourcesResult != null && resourcesResult.getSystemCode() != null) {
                RolesResources updateRolesResources = new RolesResources();
                updateRolesResources.setId(rolesResourcesResult.getId());
                updateRolesResources.setSystemCode(resourcesResult.getSystemCode());
                int updateResult = rolesResourcesMapper.updateByPrimaryKeySelective(updateRolesResources);
                updateRoleResourceCount = updateRoleResourceCount + updateResult;
            }
        }

        map.put("2", "更新角色资源的系统编码,查询数据条数：" + rolesResourcesList.size() + ",实际条数:" + updateRoleResourceCount);

        return map;
    }
}