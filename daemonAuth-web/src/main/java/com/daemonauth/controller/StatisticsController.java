package com.daemonauth.controller;

import com.google.common.collect.Maps;
import com.daemonauth.domain.Resources;
import com.daemonauth.domain.enums.ResourceEnums;
import com.daemonauth.domain.query.Query;
import com.daemonauth.service.ResourcesService;
import com.daemonauth.service.RolesService;
import com.daemonauth.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @Package .controller
 * @Description: 统计入口
 * @Author
 * @Date 2017/7/27
 * @Time 18:35
 * @Version V1.0
 */
@Controller
@RequestMapping("/statistics")
public class StatisticsController {


    @Autowired
    private ResourcesService resourcesService;

    @Autowired
    private RolesService rolesService;

    @Autowired
    private UsersService usersService;


    @RequestMapping(value = "/count", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> count() {

        Long resourcesCount = resourcesService.findCount();

        Resources resources = new Resources();
        resources.setNodeType(ResourceEnums.NodeType.SYS.getCode());
        Query<Resources> query = new Query<Resources>();
        query.setQuery(resources);

        long systemCount = resourcesService.queryCountBySelective(query);
        Long rolesCount = rolesService.findCount();
        Long usersCount = usersService.findCount();

        Map<String, Object> result = Maps.newHashMap();
        result.put("userCount", usersCount);
        result.put("roleCount", rolesCount);
        result.put("resourceCount", resourcesCount);
        result.put("systemCount", systemCount);

        return result;
    }
}
