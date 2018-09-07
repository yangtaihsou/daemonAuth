package com.daemonauth.service.common;

import com.daemonauth.domain.*;
import com.daemonauth.export.NodeTypeEnum;
import com.daemonauth.util.interceptor.context.LoginContext;
import com.daemonauth.service.*;
import com.google.common.collect.*;

import com.daemonauth.domain.query.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * User:
 * Date: 15-1-9
 * Time: 下午4:03
 * TODO 需要优化的，应该添加一些容器变量根据客户端系统编码进行加载。且每张表都应该有系统编码属性。更好的，应该添加一张客户系统表，代替资源的根节点
 */
//@Service("authorityDataConfig")
public class AuthorityDBLoad  {
    private final static Logger log = LoggerFactory.getLogger(AuthorityDBLoad.class);
    public Multimap<String, RolesResources> rolesResources_resourceCodeMultimap = ArrayListMultimap.create();//角色资源表——根据资源码维度存放
    public Multimap<String, RolesResources> rolesResources_roleCodeMultimap = ArrayListMultimap.create();//角色资源表——根据角色码维度存放
    public Map<String, Resources> resources_resourceCodeMap = new HashMap<String, Resources>();
    public Multimap<String, Resources> resources_parentCodeMultimap = ArrayListMultimap.create();//
    public Multimap<String, Resources> resources_userIdMultimap = ArrayListMultimap.create();//用户维度的资源
    public Multimap<String, Resources> resourcesLeaf_userIdMultimap = ArrayListMultimap.create();//用户维度的叶子资源
    public Multimap<String, Resources> resourcesLeaf_rolecodeMultimap = ArrayListMultimap.create();//角色维度的叶子资源
    public Map<String, Roles> roles_roleCodeMap = new HashMap<String, Roles>();
    public Multimap<String, Roles> roles_systemCodeMultimap = ArrayListMultimap.create();//
    public Multimap<String, UsersRoles> usersRoles_roleCodeMultimap = ArrayListMultimap.create();
    public Multimap<String, UsersRoles> usersRoles_userPinMultimap = ArrayListMultimap.create();
    public Map<String, Users> users_userPinMap = new HashMap<String, Users>();//
    /**
     * ********以上代码自动生成，根据普通索引新建Multimap，根据唯一索引新建Map**********
     */


    public HashBasedTable<String, String, Users> users_hbase = HashBasedTable.create();//资源系统—用户id维度存放。用于客户端系统根据系统获取所有用户
    public Map<String, Resources> resources_rootMap = new HashMap<String, Resources>();//根节点
    public Map<String, String> resources_rootCodeNameMap = new HashMap<String, String>();//根节点的编码和名字。
    /**
     * 资源叶子节点
     */
    public Map<String, Tree> resourcesTreeLeafMap = new HashMap<String, Tree>();//这里的tree是resourcesTreeBranchListMap里叶子节点的tree，用于check设置
    public Map<String, List<Tree>> resoucesTreeMap = new HashMap<String, List<Tree>>();
    /**
     * 角色树
     */
    public List<Tree> roleTreeList = new ArrayList<Tree>();
    public Map<String, Tree> roleTreeMap = new HashMap<String, Tree>();//这里的tree是roleTree里的tree，用于check设置
    public HashBasedTable<String, String, ErpSysUser> erpSysUser_hbase = HashBasedTable.create();//用户—系统维度存放
    public Map<String, ErpSysUser> erpSysUserMap = new HashMap<String, ErpSysUser>();
    @Autowired
    private RolesResourcesService rolesResourcesService;
    @Autowired
    private ResourcesService resourcesService;
    @Autowired
    private RolesService rolesService;
    @Autowired
    private UsersRolesService usersRolesService;
    @Autowired
    private UsersService usersService;
    /**
     * 资源树枝节点
     */
    private Map<String, Tree> resourcesTreeBranchMap = new HashMap<String, Tree>();//这里的tree是resourcesTreeBranchListMap里的tree，用于check设置
    private Map<String, List<Tree>> resourcesTreeBranchListMap = new HashMap<String, List<Tree>>();
    /**
     * 本系统的用户
     */
    @Resource(name = "erpSysUserService")
    private ErpSysUserService erpSysUserService;

    public void reload() {
        this.init();
    }

    @PostConstruct
    public void init() {
        Long beginTime = System.currentTimeMillis();
        //clearAll();
        loadRolesResources();
        loadResources();
        loadRoles();
        loadUsers();
        loadUsersRoles();
        loadErpSysUser();
        /***********以上方法 及方法代码自动生成***********/
        loadResourceToTree();
        loadResourceToTreeBranch();
        loadRoleToTree();
        Long endTime = System.currentTimeMillis();
        //log.info("加载资源权限DB数据用时" + (endTime - beginTime));
    }

    private void loadErpSysUser() {
        ErpSysUser erpSysUser = new ErpSysUser();
        erpSysUser.setEnable(true);
        // erpSysUser.setUserType(1);
        Query<ErpSysUser> erpSysUserQuery = new Query<ErpSysUser>();
        erpSysUserQuery.setQuery(erpSysUser);
        List<ErpSysUser> erpSysUserList = erpSysUserService.queryBySelective(erpSysUserQuery);//仅仅加载有效用户
        HashBasedTable<String, String, ErpSysUser> erpSysUser_hbaseTemp = HashBasedTable.create();
        Map<String, ErpSysUser> erpSysUserMapTemp = new HashMap<String, ErpSysUser>();
        if (erpSysUserList != null && erpSysUserList.size() > 0) {
            for (ErpSysUser queryErpSysUser : erpSysUserList) {
                String erp = queryErpSysUser.getUserErp();
                erpSysUserMapTemp.put(erp, queryErpSysUser);
                String sysCodes = queryErpSysUser.getSysCodes();
                if (sysCodes != null && !sysCodes.equals("")) {
                    if (sysCodes.contains(",")) {
                        String[] sysCodeArr = sysCodes.split(",");
                        for (String sysCode : sysCodeArr) {
                            erpSysUser_hbaseTemp.put(erp, sysCode, queryErpSysUser);
                        }
                    } else {
                        erpSysUser_hbaseTemp.put(erp, sysCodes, queryErpSysUser);
                    }
                }
            }
        }
        erpSysUserMap = erpSysUserMapTemp;
        erpSysUser_hbase = erpSysUser_hbaseTemp;
    }

    /**
     * TODO 需要优化，使用copyonwrite方式替换容器变量。使用clearAll，在高并发时会带来问题：主要是null
     * map基本不用清理，list需要清理
     */
    @Deprecated
    private void clearAll() {
     /*   rolesResources_resourceCodeMultimap.clear();
        rolesResources_roleCodeMultimap.clear();
        resources_resourceCodeMap.clear();
        resources_parentCodeMultimap.clear();
        roles_roleCodeMap.clear();
        usersRoles_roleCodeMultimap.clear();
        usersRoles_userPinMultimap.clear();
        users_userPinMap.clear();
        resources_rootMap.clear();
        resources_rootCodeNameMap.clear();
        resourcesTreeLeafMap.clear();
        resoucesTreeMap.clear();
        resourcesTreeBranchMap.clear();
        resourcesTreeBranchListMap.clear();
        roleTreeList.clear();
        roleTreeMap.clear();*/
        try {
            AuthorityDBLoad load = this;
            Field[] fields = load.getClass().getFields();
            for (Field field : fields) {
                String fieldName = field.getName();
                //  if (!fieldName.equals("") && !fieldName.equals("")) {
                Object value = field.get(load);
                Class objectClass = value.getClass();
                Method method = objectClass.getMethod("clear", null);
                method.invoke(value, null);
                //  }
            }
        } catch (Exception e) {
            log.error("将属性清空出错", e);
        }
    }

    private void loadUsers() {
        List<Users> list = usersService.findAll();

        Map<String, Users> users_userPinMapTemp = new HashMap<String, Users>();//
        for (Users users : list) {
            users_userPinMapTemp.put(users.getUserPin(), users);
        }
        users_userPinMap = users_userPinMapTemp;
    }

    private void loadRoleToTree() {
        List<Tree> roleTreeListTemp = new ArrayList<Tree>();
        Map<String, Tree> roleTreeMapTemp = new HashMap<String, Tree>();//这里的tree是roleTree里的tree，用于check设置
        Iterator<String> rolesSystemCodeIter = roles_systemCodeMultimap.keySet().iterator();
        while (rolesSystemCodeIter.hasNext()) {
            String systemCode = rolesSystemCodeIter.next();
            Iterator<Map.Entry<String, Roles>> iter = roles_roleCodeMap.entrySet().iterator();
            Tree nodeTree = new Tree();
            nodeTree.setId(systemCode);
            nodeTree.setText(resources_rootCodeNameMap.get(systemCode));
            nodeTree.setState(null);
            nodeTree.setState("closed");

            nodeTree.setChecked(null);
            List<Tree> childrenList = new ArrayList<Tree>();
            while (iter.hasNext()) {
                Map.Entry<String, Roles> entry = iter.next();
                Roles roles = entry.getValue();
                if (systemCode.equals(entry.getValue().getSystemCode())) {
                    if (roles.getEnable() != null && roles.getEnable()) {
                        Tree leafTree = new Tree();
                        leafTree.setId(entry.getKey());
                        leafTree.setText(entry.getValue().getRoleName());
                        leafTree.setState(null);

                        leafTree.setChecked(null);
                        roleTreeMapTemp.put(leafTree.getId(), leafTree);
                        if (entry.getValue().getSystemCode().equals(systemCode)) {
                            childrenList.add(leafTree);
                        }
                    }
                }
            }
            nodeTree.setChildren(childrenList);
            roleTreeListTemp.add(nodeTree);
        }
        roleTreeMap = roleTreeMapTemp;
        roleTreeList = roleTreeListTemp;
    }

    private void loadResourceToTree() {
        Iterator<Map.Entry<String, Resources>> iter = resources_rootMap.entrySet().iterator();
        Map<String, List<Tree>> resoucesTreeMapTemp = new HashMap<String, List<Tree>>();
        while (iter.hasNext()) {
            Map.Entry<String, Resources> entry = iter.next();
            Tree nodeTree = new Tree();
            nodeTree.setId(entry.getKey());
            nodeTree.setText(entry.getValue().getResourceName());
            nodeTree.setChecked(null);
            Tree tree = recursionLoadResourceTree(nodeTree);
            List<Tree> treeList = new ArrayList<Tree>();
            treeList.add(tree);
            resoucesTreeMapTemp.put(entry.getKey(), treeList);
        }
        resoucesTreeMap = resoucesTreeMapTemp;
    }

    private void loadResourceToTreeBranch() {//todo
        Iterator<Map.Entry<String, Resources>> iter = resources_rootMap.entrySet().iterator();
        Map<String, Tree> resourcesTreeBranchMapTemp = new HashMap<String, Tree>();//这里的tree是resourcesTreeBranchListMap里的tree，用于check设置
        Map<String, List<Tree>> resourcesTreeBranchListMapTemp = new HashMap<String, List<Tree>>();

        while (iter.hasNext()) {
            Map.Entry<String, Resources> entry = iter.next();
            Tree nodeTree = new Tree();
            nodeTree.setId(entry.getKey());
            nodeTree.setText(entry.getValue().getResourceName());
            //nodeTree.setState(null);

            List<Resources> resourcesList = (List<Resources>) resources_parentCodeMultimap.get(nodeTree.getId());
            if (resourcesList != null && resourcesList.size() > 0) {//如果节点下没有子节点，也设置为closed，将展示为文件夹，然后点击文件夹死循环了
                nodeTree.setState("closed");
            }
            nodeTree.setChecked(null);
            Tree tree = recursionLoadResourceTreeBranch(nodeTree);
            resourcesTreeBranchMap.put(nodeTree.getId(), nodeTree);
            List<Tree> treeBranchList = new ArrayList<Tree>();
            treeBranchList.add(tree);
            resourcesTreeBranchListMapTemp.put(entry.getKey(), treeBranchList);
        }
        //resourcesTreeBranchMap = resourcesTreeBranchMapTemp;
        resourcesTreeBranchListMap = resourcesTreeBranchListMapTemp;
    }

    /**
     * 递归
     *
     * @param nodeTree
     * @return
     */
    private Tree recursionLoadResourceTree(Tree nodeTree) {
        List<Resources> resourcesList = (List<Resources>) resources_parentCodeMultimap.get(nodeTree.getId());
        //Map<String,Tree> resourcesTreeLeafMapTem = new HashMap<String,Tree>();//这里的tree是resourcesTreeBranchListMap里叶子节点的tree，用于check设置
        if (resourcesList == null || resourcesList.size() == 0) {//虽然是节点，但节点下还没有资源
            nodeTree.setChildren(null);
            nodeTree.setState(null);

            resourcesTreeLeafMap.put(nodeTree.getId(), nodeTree);//作为一个最底一个层次的树放入，如果前台是否勾选
        } else {
            List<Tree> treeList1 = new ArrayList<Tree>();
            for (Resources resources : resourcesList) {
                Tree tree1 = new Tree();
                tree1.setId(resources.getResourceCode());
                tree1.setText(resources.getResourceName());
                if (resources.getNodeType() == NodeTypeEnum.BRANCH_NODE.getType() || resources.getNodeType() == NodeTypeEnum.ROOT_NODE.getType()) {
                    //  tree1.setState("closed");
                    tree1.setState(null);
                } else {
                    tree1.setState(null);
                }
                if (resources.getNodeType().intValue() == NodeTypeEnum.ROOT_NODE.getType() || resources.getNodeType().intValue() == NodeTypeEnum.BRANCH_NODE.getType()) {//枝节点、根结点
                    recursionLoadResourceTree(tree1);
                    //  }else if(resources.getNodeType().intValue()==NodeTypeEnum.LEAF_NODE.getType()){
                } else {
                    resourcesTreeLeafMap.put(tree1.getId(), tree1);

                }
                treeList1.add(tree1);
            }
            nodeTree.setChildren(treeList1);
        }
        return nodeTree;
    }

    /**
     * 递归
     *
     * @param nodeTree
     * @return
     */
    private Tree recursionLoadResourceTreeBranch(Tree nodeTree) {
        List<Resources> resourcesList = (List<Resources>) resources_parentCodeMultimap.get(nodeTree.getId());
        List<Tree> treeList1 = new ArrayList<Tree>();
        for (Resources resources : resourcesList) {
            Tree tree1 = new Tree();
            tree1.setId(resources.getResourceCode());
            tree1.setText(resources.getResourceName());
            if (resources.getNodeType().intValue() == NodeTypeEnum.ROOT_NODE.getType() || resources.getNodeType().intValue() == NodeTypeEnum.BRANCH_NODE.getType()) {//枝节点、根结点
                //tree1.setState("closed");
                recursionLoadResourceTreeBranch(tree1);
                resourcesTreeBranchMap.put(tree1.getId(), tree1);
                treeList1.add(tree1);
            }
        }
        nodeTree.setChildren(treeList1);
        return nodeTree;
    }

    private void loadRolesResources() {
        Multimap<String, RolesResources> rolesResources_resourceCodeMultimapTemp = ArrayListMultimap.create();//角色资源表——根据资源码维度存放
        Multimap<String, RolesResources> rolesResources_roleCodeMultimapTemp = ArrayListMultimap.create();//角色资源表——根据角色码维度存放
        List<RolesResources> list = rolesResourcesService.findAll();
        for (RolesResources rolesResources : list) {
            rolesResources_resourceCodeMultimapTemp.put(rolesResources.getResourceCode(), rolesResources);
            rolesResources_roleCodeMultimapTemp.put(rolesResources.getRoleCode(), rolesResources);
        }
        rolesResources_resourceCodeMultimap = rolesResources_resourceCodeMultimapTemp;
        rolesResources_roleCodeMultimap = rolesResources_roleCodeMultimapTemp;
    }

    private void loadResources() {
        List<Resources> list = resourcesService.findAll();
        if (null != list) {
            Map<String, Resources> resources_resourceCodeMapTemp = new HashMap<String, Resources>();
            Multimap<String, Resources> resources_parentCodeMultimapTemp = ArrayListMultimap.create();//
            Map<String, Resources> resources_rootMapTemp = new HashMap<String, Resources>();//根节点
            Map<String, String> resources_rootCodeNameMapTemp = new HashMap<String, String>();//根节点的编码和名字。
            for (int i = 0; i < list.size(); i++) {
                Resources resources = list.get(i);
                resources_resourceCodeMapTemp.put(resources.getResourceCode(), resources);
                resources_parentCodeMultimapTemp.put(resources.getParentCode(), resources);
                if (resources.getNodeType().intValue() == NodeTypeEnum.ROOT_NODE.getType()) {
                    resources_rootMapTemp.put(resources.getResourceCode(), resources);
                    resources_rootCodeNameMapTemp.put(resources.getResourceCode(), resources.getResourceName());
                }
            }
            resources_resourceCodeMap = resources_resourceCodeMapTemp;
            resources_parentCodeMultimap = resources_parentCodeMultimapTemp;
            resources_rootMap = resources_rootMapTemp;
            resources_rootCodeNameMap = resources_rootCodeNameMapTemp;
        }

    }

    private void loadRoles() {
        List<Roles> list = rolesService.findAll();
        Map<String, Roles> roles_roleCodeMapTemp = new HashMap<String, Roles>();
        Multimap<String, Roles> roles_systemCodeMultimapTemp = ArrayListMultimap.create();//
        for (Roles roles : list) {
            roles_roleCodeMapTemp.put(roles.getRoleCode(), roles);
            roles_systemCodeMultimapTemp.put(roles.getSystemCode(), roles);
        }
        roles_roleCodeMap = roles_roleCodeMapTemp;
        roles_systemCodeMultimap = roles_systemCodeMultimapTemp;
    }

    private void loadUsersRoles() {
        List<UsersRoles> list = usersRolesService.findAll();
        HashBasedTable<String, String, Users> users_hbaseTemp = HashBasedTable.create();
        Multimap<String, UsersRoles> usersRoles_roleCodeMultimapTemp = ArrayListMultimap.create();
        Multimap<String, UsersRoles> usersRoles_userPinMultimapTemp = ArrayListMultimap.create();


        Multimap<String, Resources> resources_userIdMultimap1 = ArrayListMultimap.create();//用户维度的资源
        Multimap<String, Resources> resourcesLeaf_userIdMultimap1 = ArrayListMultimap.create();//用户维度的叶子资源
        Multimap<String, Resources> resourcesLeaf_rolecodeMultimapTemp = ArrayListMultimap.create();//角色维度的叶子资源
        for (UsersRoles usersRoles : list) {
            usersRoles_userPinMultimapTemp.put(usersRoles.getUserPin(), usersRoles);
            usersRoles_roleCodeMultimapTemp.put(usersRoles.getRoleCode(), usersRoles);
            Users users = users_userPinMap.get(usersRoles.getUserPin());
            if (users != null && usersRoles.getSystemCode() != null) {
                users_hbaseTemp.put(usersRoles.getSystemCode(), usersRoles.getUserPin(), users);
            }


            List<RolesResources> rolesResourcesList = (List<RolesResources>) rolesResources_roleCodeMultimap.get(usersRoles.getRoleCode());

            for (RolesResources rolesResources : rolesResourcesList) {
                String sourceCode = rolesResources.getResourceCode();
                Resources resources = resources_resourceCodeMap.get(sourceCode);
                if (resources != null) {
                    resources_userIdMultimap1.put(usersRoles.getUserPin(), resources);
                    if (resources.getNodeType() != null && resources.getNodeType() == NodeTypeEnum.LEAF_NODE.getType()) {
                        resourcesLeaf_userIdMultimap1.put(usersRoles.getUserPin(), resources);
                        resourcesLeaf_rolecodeMultimapTemp.put(rolesResources.getRoleCode(), resources);
                    }
                }
            }
        }
        users_hbase = users_hbaseTemp;
        usersRoles_roleCodeMultimap = usersRoles_roleCodeMultimapTemp;
        usersRoles_userPinMultimap = usersRoles_userPinMultimapTemp;

        resources_userIdMultimap = resources_userIdMultimap1;
        resourcesLeaf_userIdMultimap = resourcesLeaf_userIdMultimap1;
        resourcesLeaf_rolecodeMultimap = resourcesLeaf_rolecodeMultimapTemp;
    }


    /**
     * 此方法返回可以展示menu的资源节点
     *
     * @param userId
     */
    public Multimap<String, Resources> loadFrontMenuResourceByUserId(String userId) {
        Multimap<String, Resources> resourceNoLeafByParentCodeMultimap = ArrayListMultimap.create();
        loadParentResourceFromDown(resourceNoLeafByParentCodeMultimap, (List<Resources>) resourcesLeaf_userIdMultimap.get(userId));
        return resourceNoLeafByParentCodeMultimap;
    }

    /**
     * 此方法返回可以展示menu的资源节点
     *
     * @param roleCode
     */
    public Multimap<String, Resources> loadFrontMenuResourceByRolecode(String roleCode) {
        Multimap<String, Resources> resourceNoLeafByParentCodeMultimap = ArrayListMultimap.create();
        loadParentResourceFromDown(resourceNoLeafByParentCodeMultimap, (List<Resources>) resourcesLeaf_rolecodeMultimap.get(roleCode));
        return resourceNoLeafByParentCodeMultimap;
    }


    /**
     * 自下（叶子节点）而上反推所有父节点。某些父节点下只有按钮节点，这种方法可以去掉这些父节点放在menu页签中展示
     *
     * @param resourceNoLeafByParentCodeMultimap
     * @param downResourcesList
     */
    private void loadParentResourceFromDown(Multimap<String, Resources> resourceNoLeafByParentCodeMultimap, List<Resources> downResourcesList) {
        if (downResourcesList == null || downResourcesList.size() == 0) {
            return;
        }
        List<Resources> ownParentResourcesList = new ArrayList<Resources>();
        Map<String, Resources> resources_resourceCodeMap = this.resources_resourceCodeMap;

        Map<String, Resources> parentResourcesMapTemp = new HashMap<String, Resources>();
        for (Resources resources : downResourcesList) {
            if (resources != null) {
                if (resources.getResourceCode() != null) {
                    if (resources.getNodeType() == NodeTypeEnum.ROOT_NODE.getType()) {
                        if (!resourceNoLeafByParentCodeMultimap.get("root").contains(resources)) {
                            resourceNoLeafByParentCodeMultimap.put("root", resources);
                        }
                        continue;
                    }
                    if (!isUserOfResource(resources.getSystemCode())) {//去掉不属于当前登录用户的系统，即不显示给当前登录用户看此系统的预览
                        continue;
                    }

                }
                if (resources.getParentCode() != null && !resources.getParentCode().equals("")) {
                    if (!resourceNoLeafByParentCodeMultimap.get(resources.getParentCode()).contains(resources)) {//防止重复放入
                        resourceNoLeafByParentCodeMultimap.put(resources.getParentCode(), resources);
                        Resources parentResources = resources_resourceCodeMap.get(resources.getParentCode());
                        if (parentResources != null) {
                            if (!ownParentResourcesList.contains(parentResources)) {//TODO o(n)循环判断，耗时。可以使用parentResourcesMapTemp临时变量存放，在n比较大时，使用空间换取时间优化
                                ownParentResourcesList.add(parentResources);
                            }
                        }
                    }

                }
            }
        }
        if (ownParentResourcesList.size() > 0) {
            loadParentResourceFromDown(resourceNoLeafByParentCodeMultimap, ownParentResourcesList);
        }
    }

    public Map<String, List<Tree>> getResourcesTreeBranchListMap() {
        return resourcesTreeBranchListMap;
    }

    public void setResourcesTreeBranchListMap(Map<String, List<Tree>> resourcesTreeBranchListMap) {
        this.resourcesTreeBranchListMap = resourcesTreeBranchListMap;
    }


    public Map<String, Tree> getResourcesTreeBranchMap() {
        return resourcesTreeBranchMap;
    }

    public void setResourcesTreeBranchMap(Map<String, Tree> resourcesTreeBranchMap) {
        this.resourcesTreeBranchMap = resourcesTreeBranchMap;
    }


    public List<Map<String, Object>> systemCodeList() {

        List<Map<String, Object>> list = Lists.newArrayList();
        for (Map.Entry entry : getResources_rootCodeNameMap().entrySet()) {
            Map<String, Object> map = Maps.newHashMap();
            if (!isUserOfResource((String) entry.getKey())) {
                continue;
            }
            map.put("resourceCode", entry.getKey());
            map.put("resourceName", entry.getValue());
            list.add(map);
        }
        return list;
    }

    public String[] systemCodesbyLoginUser() {
        String erp = LoginContext.getLoginContext().getPin();
        if (erp.equals("bjyangkuan")) {
            return null;
        }
        ErpSysUser erpSysUser = erpSysUserMap.get(erp);
        if (erpSysUser.getUserType() == 0) {
            return null;
        }
        return erpSysUser.getSysCodes().split(",");
    }

    /**
     * 判断具体的登录用户是否属于某个系统的运营用户
     *
     * @param resourceRootCode
     * @return
     */
    public Boolean isUserOfResource(String resourceRootCode) {
        String erp = LoginContext.getLoginContext().getPin();
        if (!erp.equals("bjyangkuan")) {
            ErpSysUser erpSysUser = erpSysUserMap.get(erp);
            if (erpSysUser == null) {
                return false;
            }
            if (erpSysUser.getUserType() == 0) {
                return true;
            }
            erpSysUser = erpSysUser_hbase.get(erp, resourceRootCode);//普通用户

            if (erpSysUser == null) {
                return false;//如果登录用户没有操作某个根角色的权限，则不展示
            } else

                return true;
        }
        return true;
    }

    /**
     * 通过资源编码得到本资源所在的系统编码(即根节点)
     *
     * @param resourceCode
     * @return
     */
    public String getSystemCodeByResourceCode(String resourceCode) {
        Resources resources = resources_resourceCodeMap.get(resourceCode);
        String systemCode = null;
        if (resources == null) {
            log.info(resourceCode + "获取的资源节点为空");
            return null;
        }
        if (resources.getNodeType() == NodeTypeEnum.ROOT_NODE.getType()) {
            return systemCode = resources.getResourceCode();
        } else {
            return getSystemCodeByResourceCode(resources.getParentCode());//递归
        }

    }


    @Bean(name = "resourcesRootCodeNameMap")
    public Map<String, String> getResources_rootCodeNameMap() {
        return resources_rootCodeNameMap;
    }

    @Bean(name = "resources_resourceCodeMap")
    public Map<String, Resources> getResources_resourceCodeMap() {
        return resources_resourceCodeMap;
    }

}
