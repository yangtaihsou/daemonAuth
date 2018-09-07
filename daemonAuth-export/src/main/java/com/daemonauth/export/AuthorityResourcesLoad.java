package com.daemonauth.export;

import com.daemonauth.domain.*;
import com.daemonauth.domain.query.Query;
import com.daemonauth.export.rpc.AuthorityResourceService;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Multimap;
import org.apache.http.conn.HttpHostConnectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 无法解决maven版本问题，建立这个类
 * User:
 * Date: 15-1-9
 * Time: 下午4:03
 * 本类将系统所有表按照不同维度加载缓存，提供给客户端使用
 * TODO 需要优化的，应该根据客户端系统编码进行加载。且每张表都应该有系统编码属性。更好的，应该添加一张客户系统表，代替资源的根节点
 */
/*@Service("authorityData")*/
public class AuthorityResourcesLoad implements ClientHanderService {
    private final static Logger log = LoggerFactory.getLogger(AuthorityResourcesLoad.class);
    public Multimap<String, RolesResources> rolesResources_resourceCodeMultimap = ArrayListMultimap.create();//角色资源表——根据资源码维度存放
    public Multimap<String, RolesResources> rolesResources_roleCodeMultimap = ArrayListMultimap.create();//角色资源表——根据角色码维度存放
    public Map<String, Resources> resources_resourceCodeMap = new HashMap<String, Resources>();
    public Multimap<String, Resources> resources_parentCodeMultimap = ArrayListMultimap.create();//
    public Map<String, Roles> roles_roleCodeMap = new HashMap<String, Roles>();
    public Multimap<String, Roles> roles_systemCodeMultimap = ArrayListMultimap.create();//
    public Multimap<String, UsersRoles> usersRoles_roleCodeMultimap = ArrayListMultimap.create();
    public Multimap<String, UsersRoles> usersRoles_userPinMultimap = ArrayListMultimap.create();
    //TODO 最好不用这个，因为客户端加载的是所有系统的用户。应该加载自己系统的用户
    @Deprecated
    public Map<String, Users> users_userPinMap = new HashMap<String, Users>();//
    /**
     * 根据用户id和资源url存储资源*
     */
    public HashBasedTable<String, String, Resources> resources_userIdAndUrlHBase = HashBasedTable.create();
    /**
     * 根据用户id和资源Code存储资源*
     */
    public HashBasedTable<String, String, Resources> resources_userIdAndSourceCodeHBase = HashBasedTable.create();
    /**
     * 根据用户id存储资源
     */
    public Multimap<String, Resources> resources_userIdMultimap = ArrayListMultimap.create();//
    public Map<String, Resources> resources_rootMap = new HashMap<String, Resources>();//根节点

    /**
     * ********以上代码自动生成，根据普通索引新建Multimap，根据唯一索引新建Map**********
     */
    public Map<String, String> resources_rootCodeNameMap = new HashMap<String, String>();//根节点的编码和名字。
    public Multimap<String, Resources> resourcesLeaf_userIdMultimap = ArrayListMultimap.create();//用户维度的叶子(带有url)资源
    public Multimap<String, Resources> resourcesButton_userIdMultimap = ArrayListMultimap.create();//用户维度的按钮(带有url)资源
    public Multimap<String, Resources> resourcesUrl_userIdMultimap = ArrayListMultimap.create();//用户维度的带着有url资源
    Long refreshTime = 60000l;//默认一分钟刷新，不能超过1分钟
    //你的系统编码
    private String systemCode = null;
    @Resource(name = "authorityResource")
    private AuthorityResourceService authorityResource;

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    @Override
    public void reload() {
        // this.init();
    }

    public Long getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(Long refreshTime) {
        if (refreshTime <= this.refreshTime) {
            this.refreshTime = refreshTime;
        }
    }

    @PostConstruct
    public void init() {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                for (; ; ) {//添加循环目的:知道加载第三方资源成功，才关闭这个线程。因为如果在本系统启动时，第三方系统有问题，但本系统照样启动，
                    // 启动后仍然轮询第三方系统，知道加载资源成功。
                    try {

                        Long beginTime = System.currentTimeMillis();
                        //clearAll();
                        if (null != authorityResource) {
                            loadResources();
                            loadRolesResources();
                            loadRoles();
                            loadUsersRoles();
                            loadUsers();
                            /***********以上方法 及方法代码自动生成***********/
                        } else {
                            log.info("加载延迟中。。。。");
                            Thread.sleep(1000);
                        }

                        Long endTime = System.currentTimeMillis();
                       // log.info("加载资源权限数据用时" + (endTime - beginTime));
                        Thread.sleep(refreshTime);
                    } catch (Exception e) {
                        log.error("加载权限资源报错", e);
                        Throwable throwable = e.getCause();
                        if (throwable instanceof HttpHostConnectException) {
                            log.error("加载权限资源报错:网络连接不上");
                        } else {

                        }

                    } finally {
                       /* if (requestFlag) {
                            break;
                        }*/
                    }
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    private void clearAll() throws Exception {
        try {
            AuthorityResourcesLoad load = this;
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
            throw e;
        }
    }

    private void loadUsers() {
        Users queryUsers = new Users();
        queryUsers.setEnable(true);
        Query<Users> query = new Query<Users>();
        query.setQuery(queryUsers);
        List<Users> list = authorityResource.getUsersList(query);

        Map<String, Users> users_userPinMap1 = new HashMap<String, Users>();
        for (Users users : list) {
            users_userPinMap1.put(users.getUserPin(), users);
        }
        users_userPinMap = users_userPinMap1;
    }


    private void loadRolesResources() {
        RolesResources queryRolesResources = new RolesResources();
        queryRolesResources.setEnable(true);
        queryRolesResources.setSystemCode(systemCode);
        Query<RolesResources> query = new Query<RolesResources>();
        query.setQuery(queryRolesResources);
        List<RolesResources> list = authorityResource.getRolesResourcesList(query);
        Multimap<String, RolesResources> rolesResources_resourceCodeMultimap1 = ArrayListMultimap.create();
        Multimap<String, RolesResources> rolesResources_roleCodeMultimap1 = ArrayListMultimap.create();
        for (RolesResources rolesResources : list) {
            rolesResources_resourceCodeMultimap1.put(rolesResources.getResourceCode(), rolesResources);
            rolesResources_roleCodeMultimap1.put(rolesResources.getRoleCode(), rolesResources);
        }
        rolesResources_resourceCodeMultimap = rolesResources_resourceCodeMultimap1;
        rolesResources_roleCodeMultimap = rolesResources_roleCodeMultimap1;
    }

    private void loadResources() {

        Resources queryResources = new Resources();
        queryResources.setEnable(true);
        queryResources.setSystemCode(systemCode);
        Query<Resources> query = new Query<Resources>();
        query.setQuery(queryResources);
        List<Resources> list = authorityResource.getResourcesList(query);
        Map<String, Resources> resources_resourceCodeMap1 = new HashMap<String, Resources>();
        Multimap<String, Resources> resources_parentCodeMultimap1 = ArrayListMultimap.create();//
        Map<String, Resources> resources_rootMap1 = new HashMap<String, Resources>();

        Map<String, String> resources_rootCodeNameMap1 = new HashMap<String, String>();//
        for (int i = 0; i < list.size(); i++) {
            Resources resources = list.get(i);
            resources_resourceCodeMap1.put(resources.getResourceCode(), resources);
            resources_parentCodeMultimap1.put(resources.getParentCode(), resources);
            if (resources.getNodeType().intValue() == NodeTypeEnum.ROOT_NODE.getType()) {
                resources_rootMap1.put(resources.getResourceCode(), resources);
                resources_rootCodeNameMap1.put(resources.getResourceCode(), resources.getResourceName());
            }
        }
        resources_resourceCodeMap = resources_resourceCodeMap1;
        resources_parentCodeMultimap = resources_parentCodeMultimap1;
        resources_rootMap = resources_rootMap1;
        resources_rootCodeNameMap = resources_rootCodeNameMap1;
    }

    private void loadRoles() {
        Roles queryRoles = new Roles();
        queryRoles.setEnable(true);
        queryRoles.setSystemCode(systemCode);
        Query<Roles> query = new Query<Roles>();
        query.setQuery(queryRoles);
        Map<String, Roles> roles_roleCodeMap1 = new HashMap<String, Roles>();
        Multimap<String, Roles> roles_systemCodeMultimap1 = ArrayListMultimap.create();
        List<Roles> list = authorityResource.getRolesList(query);
        for (Roles roles : list) {
            roles_roleCodeMap1.put(roles.getRoleCode(), roles);
            roles_systemCodeMultimap1.put(roles.getSystemCode(), roles);
        }
        roles_roleCodeMap = roles_roleCodeMap1;
        roles_systemCodeMultimap = roles_systemCodeMultimap1;
    }

    private void loadUsersRoles() {
        UsersRoles queryUsersRoles = new UsersRoles();
        queryUsersRoles.setEnable(true);
        queryUsersRoles.setSystemCode(systemCode);
        Query<UsersRoles> query = new Query<UsersRoles>();
        query.setQuery(queryUsersRoles);
        List<UsersRoles> list = authorityResource.getUsersRolesList(query);

        Multimap<String, UsersRoles> usersRoles_roleCodeMultimap1 = ArrayListMultimap.create();
        Multimap<String, UsersRoles> usersRoles_userPinMultimap1 = ArrayListMultimap.create();
        for (UsersRoles usersRoles : list) {
            usersRoles_userPinMultimap1.put(usersRoles.getUserPin(), usersRoles);
            usersRoles_roleCodeMultimap1.put(usersRoles.getRoleCode(), usersRoles);
        }
        usersRoles_roleCodeMultimap = usersRoles_roleCodeMultimap1;
        usersRoles_userPinMultimap = usersRoles_userPinMultimap1;
        moreDetailResourceByRoles(list);
    }

    /**
     * 通过解析用户角色表，将用户和资源进一步详细关联起来
     *
     * @param usersRolesList
     */
    private void moreDetailResourceByRoles(List<UsersRoles> usersRolesList) {

        HashBasedTable<String, String, Resources> resources_userIdAndUrlHBase1 = HashBasedTable.create();
        HashBasedTable<String, String, Resources> resources_userIdAndSourceCodeHBase1 = HashBasedTable.create();
        Multimap<String, Resources> resources_userIdMultimap1 = ArrayListMultimap.create();
        Multimap<String, Resources> resourcesButton_userIdMultimapTemp = ArrayListMultimap.create();//用户维度的按钮(带有url)资源
        Multimap<String, Resources> resourcesUrl_userIdMultimapTemp = ArrayListMultimap.create();//用户维度的带着有url资源
        Multimap<String, Resources> resourcesLeaf_userIdMultimapTemp = ArrayListMultimap.create();//用户维度的叶子资源
        for (UsersRoles usersRoles : usersRolesList) {
            List<RolesResources> rolesResourcesList = (List<RolesResources>) rolesResources_roleCodeMultimap.get(usersRoles.getRoleCode());
            for (RolesResources rolesResources : rolesResourcesList) {
                String sourceCode = rolesResources.getResourceCode();
                Resources resources = resources_resourceCodeMap.get(sourceCode);
                if (resources != null) {
                    if (resources.getResourceUrl() != null) {
                        resources_userIdAndUrlHBase1.put(usersRoles.getUserPin(), resources.getResourceUrl(), resources);
                    }
                    if (resources.getResourceCode() != null) {
                        resources_userIdAndSourceCodeHBase1.put(usersRoles.getUserPin(), resources.getResourceCode(), resources);
                    }
                    if (resources.getNodeType() != null && resources.getNodeType() == NodeTypeEnum.LEAF_NODE.getType()) {
                        resourcesLeaf_userIdMultimapTemp.put(usersRoles.getUserPin(), resources);
                        resourcesUrl_userIdMultimapTemp.put(usersRoles.getUserPin(), resources);

                    }
                    if (resources.getNodeType() != null && resources.getNodeType() == NodeTypeEnum.RESOURCE_NODE.getType()) {
                        resourcesButton_userIdMultimapTemp.put(usersRoles.getUserPin(), resources);
                        resourcesUrl_userIdMultimapTemp.put(usersRoles.getUserPin(), resources);
                    }
                }
                resources_userIdMultimap1.put(usersRoles.getUserPin(), resources);
            }
        }
        resources_userIdAndUrlHBase = resources_userIdAndUrlHBase1;
        resources_userIdAndSourceCodeHBase = resources_userIdAndSourceCodeHBase1;
        resources_userIdMultimap = resources_userIdMultimap1;
        resourcesLeaf_userIdMultimap = resourcesLeaf_userIdMultimapTemp;
        resourcesButton_userIdMultimap = resourcesButton_userIdMultimapTemp;
        resourcesUrl_userIdMultimap = resourcesUrl_userIdMultimapTemp;
    }

    /**
     * 此方法返回可以展示menu的资源节点(目录节点和叶子节点)
     *
     * @param userId
     */
    public Multimap<String, Resources> loadFrontMenuResourceByUserId(String userId) {
        Multimap<String, Resources> resourceNoLeafByParentCodeMultimap = ArrayListMultimap.create();
        loadParentResourceFromDown(resourceNoLeafByParentCodeMultimap, (List<Resources>) resourcesLeaf_userIdMultimap.get(userId));
        return resourceNoLeafByParentCodeMultimap;
    }

    /**
     * 自下（叶子节点）而上反推所有父节点。某些父节点下只有按钮节点，这种方法可以去掉这些父节点放在menu页签中展示
     *
     * @param resourceParentCodeMultimap
     * @param downResourcesList          递归第一次，里面的资源都是叶子节点，不包括按钮节点
     */
    private void loadParentResourceFromDown(Multimap<String, Resources> resourceParentCodeMultimap, List<Resources> downResourcesList) {
        List<Resources> ownParentResourcesList = new ArrayList<Resources>();
        Map<String, Resources> resources_resourceCodeMap = this.resources_resourceCodeMap;
        for (Resources resources : downResourcesList) {
            if (resources != null) {
                if (resources.getResourceCode() != null && resources.getResourceCode().equals(this.getSystemCode())) {
                    continue;
                }
                if (resources.getParentCode() != null && !resources.getParentCode().equals("")) {
                    if (!resourceParentCodeMultimap.get(resources.getParentCode()).contains(resources)) {//防止重复放入
                        resourceParentCodeMultimap.put(resources.getParentCode(), resources);
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
            loadParentResourceFromDown(resourceParentCodeMultimap, ownParentResourcesList);
        }
    }
}
