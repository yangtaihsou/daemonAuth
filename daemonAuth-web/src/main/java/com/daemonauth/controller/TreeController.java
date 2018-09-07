package com.daemonauth.controller;

import com.daemonauth.domain.*;
import com.daemonauth.export.NodeTypeEnum;
import com.daemonauth.util.interceptor.context.LoginContext;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

import com.daemonauth.service.common.AuthorityDBLoad;
import com.daemonauth.util.R;
import com.daemonauth.util.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.*;

/**
 * User:
 * Date: 14-11-17
 * Time: 下午3:50
 */
@RequestMapping("/tree")
@Controller
public class TreeController {
    @Resource(name = "authorityDataConfig")
    private AuthorityDBLoad authorityDataConfig;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ResponseBody
    public String test1() {
        Result result = new Result();
        String content = "[{\"id\":\"root\",\"text\":\"My Documents\",\"children\":[{\"id\":\"Photos\",\"text\":\"Photos\",\"state\":\"closed\",\"children\":[{\"id\":111,\"text\":\"Friend\"},{\"id\":112,\"text\":\"Wife\"},{\"id\":113,\"text\":\"Company\"}]},{\"id\":12,\"text\":\"Program Files\",\"children\":[{\"id\":121,\"text\":\"Intel\"},{\"id\":122,\"text\":\"Java\",\"attributes\":{\"p1\":\"Custom Attribute1\",\"p2\":\"Custom Attribute2\"}},{\"id\":123,\"text\":\"Microsoft Office\"},{\"id\":124,\"text\":\"Games\",\"checked\":true}]},{\"id\":13,\"text\":\"index.html\"},{\"id\":14,\"text\":\"about.html\"},{\"id\":15,\"text\":\"welcome.html\"}]}]";
        result.setContent(content);
        return content;
    }

    @RequestMapping(value = "/resources", method = RequestMethod.GET)
    @ResponseBody
    public List<Tree> resources(String roleCode) {
        clearChecked(authorityDataConfig.resourcesTreeLeafMap);
        if (roleCode != null && !roleCode.equals("")) {//设定已经挑选的
            Multimap<String, RolesResources> RolesResourcesListＭap = authorityDataConfig.rolesResources_roleCodeMultimap;
            List<RolesResources> rolesResourcesList = (List<RolesResources>) RolesResourcesListＭap.get(roleCode);
            for (RolesResources rolesResources : rolesResourcesList) {
                Tree tree = authorityDataConfig.resourcesTreeLeafMap.get(rolesResources.getResourceCode());
                if (tree != null) {
                    tree.setChecked("true");
                }
            }
        }
        Roles roles = authorityDataConfig.roles_roleCodeMap.get(roleCode);
        Map<String, List<Tree>> treeMap = authorityDataConfig.resoucesTreeMap;
        List<Tree> roleCode_systemCode_tree = treeMap.get(roles.getSystemCode());
      /*  List<Tree> treeList = new ArrayList<Tree>();
        Iterator<Map.Entry<String, List<Tree>>> iter = treeMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, List<Tree>> entry = iter.next();
            treeList.addAll(treeMap.get(entry.getKey()));
        }
        return treeList;*/
        return roleCode_systemCode_tree;
    }

    @RequestMapping(value = "/resourcesBranch", method = RequestMethod.GET)
    @ResponseBody
    public List<Tree> resourcesBranch(String parentCode) {

        clearChecked(authorityDataConfig.getResourcesTreeBranchMap());
        if (parentCode != null && !parentCode.equals("")) {
            authorityDataConfig.getResourcesTreeBranchMap().get(parentCode).setChecked("true");
        }
        Map<String, List<Tree>> treeMap = authorityDataConfig.getResourcesTreeBranchListMap();
        List<Tree> treeList = new ArrayList<Tree>();

        Iterator<Map.Entry<String, List<Tree>>> iter = treeMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, List<Tree>> entry = iter.next();
            treeList.addAll(treeMap.get(entry.getKey()));
        }
        return treeList;
    }

    @RequestMapping(value = "/roles", method = RequestMethod.GET)
    @ResponseBody
    public List<Tree> roles(String userPin) {
        clearChecked(authorityDataConfig.roleTreeMap);
        if (userPin != null && !userPin.equals("")) {
            Multimap<String, UsersRoles> usersRoles_pinMultimap = authorityDataConfig.usersRoles_userPinMultimap;
            List<UsersRoles> usersRolesList = (List<UsersRoles>) usersRoles_pinMultimap.get(userPin);
            for (UsersRoles usersRoles : usersRolesList) {
                Tree tree = authorityDataConfig.roleTreeMap.get(usersRoles.getRoleCode());
                if (tree != null) {
                    tree.setChecked("true");
                }
            }
        }
        List<Tree> roleTree = authorityDataConfig.roleTreeList;
        return roleTree;
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ModelAndView test() {
        ModelAndView mv = new ModelAndView("/tree/resources.jsp");
        return mv;
    }

    private void clearChecked(Map<String, Tree> treeMap) {
        Iterator<Map.Entry<String, Tree>> iter = treeMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, Tree> entry = iter.next();
            entry.getValue().setChecked(null);
        }
    }

    /**
     * 角色树
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/roleTree")
    public R roleTree() {
        List<Tree> roleTree = authorityDataConfig.roleTreeList;

        List<Map<String, Object>> roleList = Lists.newArrayList();
        for (Tree tree : roleTree) {
            if (!authorityDataConfig.isUserOfResource(tree.getId())) {
                continue;
            }
            roleList.add(buildNodeByTree(tree, ""));
            if (null != tree.getChildren()) {
                for (Tree role : tree.getChildren()) {
                    roleList.add(buildNodeByTree(role, tree.getId()));
                }
            }

        }

        return R.ok().put("roleList", roleList);
    }

    /**
     * 上级树
     *
     * @param parentCode
     * @return
     */
    @ResponseBody
    @RequestMapping("/parentTree")
    public R parentTree(String parentCode) {
        String erp = LoginContext.getLoginContext().getPin();
        //Collection<Resources> resources = authorityDataConfig.resources_userIdMultimap.get(erp);
        Collection<Resources> resources = authorityDataConfig.resources_resourceCodeMap.values();
        List<Map<String, Object>> list = Lists.newArrayList();
        for (Resources entity : resources) {
            /**去掉不属于自己系统的资源**/
            if (entity.getEnable() && (entity.getNodeType() == NodeTypeEnum.ROOT_NODE.getType())) {
                if (!authorityDataConfig.isUserOfResource(entity.getSystemCode())) {
                    continue;
                }
            }
            if (entity.getEnable() && (entity.getNodeType() == NodeTypeEnum.BRANCH_NODE.getType())) {
                if (!authorityDataConfig.isUserOfResource(entity.getSystemCode())) {//TODO 如果存在二级子目录，需要进一步优化判断
                    continue;
                }
            }
            /**去掉不属于自己系统的资源**/
            if (entity.getEnable() && (entity.getNodeType() == NodeTypeEnum.ROOT_NODE.getType() || entity.getNodeType() == NodeTypeEnum.BRANCH_NODE.getType())) {
                list.add(buildNode(entity));
            }
        }

        return R.ok().put("treeList", list);
    }


    /**
     * 上下级树
     *
     * @param sourceCode
     * @return
     */
    @ResponseBody
    @RequestMapping("/upAndDownTree")
    public R upAndDownTree(String sourceCode) {
        List<Map<String, Object>> list = Lists.newArrayList();
        Resources resources = authorityDataConfig.resources_resourceCodeMap.get(sourceCode);
        list.add(buildNode(resources));
        getParentResourceByResourceCode(sourceCode, list);//TODO 添加systemcode过滤
        getDownResourceByResourceCode(sourceCode, list);
        return R.ok().put("treeList", list);
    }

    /**
     * 用户管理列表里点击预览按钮,可以查看对应menu页签(即如果用户拥有某些系统编码，可以预览对应运营系统的menu)
     *
     * @param userId
     * @return
     */
    @ResponseBody
    @RequestMapping("/sysMenuTreeView")
    public R sysMenuTreeView(String userId) {
        List<Map<String, Object>> list = Lists.newArrayList();
        Multimap<String, Resources> resourceNoLeafByParentCodeMultimap = authorityDataConfig.loadFrontMenuResourceByUserId(userId);
        if (resourceNoLeafByParentCodeMultimap != null) {
            Iterator<String> parentCodeSet = resourceNoLeafByParentCodeMultimap.keySet().iterator();
            while (parentCodeSet.hasNext()) {
                String parentCode = parentCodeSet.next();
                List<Resources> resourcesList = (List<Resources>) resourceNoLeafByParentCodeMultimap.get(parentCode);
                if (resourcesList != null) {
                    for (Resources resources : resourcesList) {
                        list.add(buildNode(resources));
                    }
                }
            }
        }
        return R.ok().put("treeList", list);
    }

    /**
     * 角色管理列表里，点击预览按钮，可以查看对应menu页签(即如果用户拥有这角色，可以预览对应运营系统的menu)
     *
     * @param roleCode
     * @return
     */
    @ResponseBody
    @RequestMapping("/sysMenuTreeViewByRoleCode")
    public R sysMenuTreeViewByRoleCode(String roleCode) {
        List<Map<String, Object>> list = Lists.newArrayList();
        Multimap<String, Resources> resourceNoLeafByParentCodeMultimap = authorityDataConfig.loadFrontMenuResourceByRolecode(roleCode);
        if (resourceNoLeafByParentCodeMultimap != null) {
            Iterator<String> parentCodeSet = resourceNoLeafByParentCodeMultimap.keySet().iterator();
            while (parentCodeSet.hasNext()) {
                String parentCode = parentCodeSet.next();
                List<Resources> resourcesList = (List<Resources>) resourceNoLeafByParentCodeMultimap.get(parentCode);
                if (resourcesList != null) {
                    for (Resources resources : resourcesList) {
                        list.add(buildNode(resources));
                    }
                }
            }
        }
        return R.ok().put("treeList", list);
    }

    /**
     * 资源树
     *
     * @param parentCode
     * @return
     */
    @ResponseBody
    @RequestMapping("/resourceTree")
    public R resourceTree(String parentCode) {

        Map<String, List<Tree>> treeMap = authorityDataConfig.resoucesTreeMap;
        List<Tree> roleCode_systemCode_tree = treeMap.get(parentCode);

        List<Map<String, Object>> list = Lists.newArrayList();
        buildNodeByTreeList(roleCode_systemCode_tree, "", list);

        List<Map<String, Object>> resources = authorityDataConfig.systemCodeList();

        return R.ok().put("treeList", list).put("resourcesRootCode", resources);
    }


    private Map<String, Object> buildNodeByTree(Tree tree, String parentCode) {

        Map<String, Object> map = Maps.newHashMap();
        map.put("code", tree.getId());
        map.put("name", tree.getText());
        map.put("parentCode", parentCode);

        return map;
    }

    /**
     * 递归的方式迭代
     *
     * @param trees
     * @param parentCode
     * @param list
     */
    private void buildNodeByTreeList(List<Tree> trees, String parentCode, List<Map<String, Object>> list) {
        if (trees == null) {
            return;
        }
        for (Tree tree : trees) {
            list.add(buildNodeByTree(tree, parentCode));
            if (null != tree && null != tree.getChildren()) {
                buildNodeByTreeList(tree.getChildren(), tree.getId(), list);
            }
        }
    }

    private Map<String, Object> buildNode(Resources resources) {

        Map<String, Object> map = Maps.newHashMap();
        map.put("code", resources.getResourceCode());
        map.put("name", resources.getResourceName());
        map.put("parentCode", resources.getParentCode());
        map.put("nodeType", resources.getNodeType());

        return map;
    }

    /**
     * 根据资源节点递归得出节点的父节点，得到父节点树
     *
     * @param resourceCode
     * @param treeList
     */
    public void getParentResourceByResourceCode(String resourceCode, List<Map<String, Object>> treeList) {
        Resources resources = authorityDataConfig.resources_resourceCodeMap.get(resourceCode);
        if (resources != null) {
            if (resources.getNodeType() == NodeTypeEnum.ROOT_NODE.getType()) {//根节点递归结束
                return;
            }
            String parentCode = resources.getParentCode();
            resources = authorityDataConfig.resources_resourceCodeMap.get(parentCode);
            if (resources != null) {
                treeList.add(buildNode(resources));
                getParentResourceByResourceCode(parentCode, treeList);
            }
        }
    }

    /**
     * 根据资源节点递归得出节点的子孙节点，得到子孙点树
     *
     * @param resourceCode
     * @param treeList
     */
    public void getDownResourceByResourceCode(String resourceCode, List<Map<String, Object>> treeList) {
        Resources resources = authorityDataConfig.resources_resourceCodeMap.get(resourceCode);
        if (resources != null) {
            if (resources.getNodeType() == NodeTypeEnum.LEAF_NODE.getType() || resources.getNodeType() == NodeTypeEnum.RESOURCE_NODE.getType()) {//叶子节点或者资源节点返回
                return;
            }
            List<Resources> sonResourcesesList = (List<Resources>) authorityDataConfig.resources_parentCodeMultimap.get(resourceCode);
            if (sonResourcesesList != null && sonResourcesesList.size() > 0) {//子节点
                for (Resources sonResource : sonResourcesesList) {
                    treeList.add(buildNode(sonResource));
                    getDownResourceByResourceCode(sonResource.getResourceCode(), treeList);
                }
            }
        }

/*        Map<String, List<Tree>> treeMap = authorityDataConfig.resoucesTreeMap;
        List<Tree> roleCode_systemCode_tree = treeMap.get(resourceCode);
        buildNodeByTreeList(roleCode_systemCode_tree, "", treeList);*/
    }
}
