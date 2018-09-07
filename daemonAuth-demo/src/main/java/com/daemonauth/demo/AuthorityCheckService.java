package com.daemonauth.demo;

import ch.lambdaj.Lambda;
import com.daemonauth.export.AuthorityResourcesLoad;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Multimap;
import com.daemonauth.domain.Resources;
import com.daemonauth.export.NodeTypeEnum;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * User:
 * Date: 15-4-23
 * Time: 下午1:58
 */
public class AuthorityCheckService {
    private final static Log log = LogFactory.getLog(AuthorityCheckService.class);
    @Resource(name = "authorityData")
    private AuthorityResourcesLoad authorityData;

    private String profiles;

    /**
     * 通过url和pin查询是否存在资源
     * @param pin
     * @param url
     * @return
     */
    public String loadResourceUrl(String pin, String url) {
        if (profiles.equals("dev")) {
            return "ok";
        }
        if (profiles.equals("test")) {
            return "ok";
        }
        HashBasedTable<String, String, Resources> resources_userIdAndUrlHBase = authorityData.resources_userIdAndUrlHBase;
        Resources resources = resources_userIdAndUrlHBase.get(pin, url);
        //Resources resources=loadResourceAsUrl(pin).get(url);
        if (resources == null) {
            return null;
        }
        return "ok";
    }

    /**
     * 组装当前用户的menu
     * @param pin
     * @return
     */
    public String setUpMenuHtml(String pin) {
        if (profiles != null && (profiles.equals("dev") || profiles.equals("test"))) {
            String html = "";//默认menu的html
            return html;
        } else {
            StringBuffer menuHtmlSBuffer = new StringBuffer();
            menuHtmlSBuffer.append("<ul class=\"sidebar-menu\">");
            menuHtmlSBuffer.append("<li class=\"header\">导航菜单</li>");
            Multimap<String, Resources> resourceParentCodeMultimap = authorityData.loadFrontMenuResourceByUserId(pin);//可以展示menu的资源节点(目录节点和叶子节点)。key都是目录节点，会有多级目录节点
            recursionMenuHtmlByResourceCodeFromDownTop(authorityData.getSystemCode(), menuHtmlSBuffer,resourceParentCodeMultimap);
            menuHtmlSBuffer.append("</ul>");
            return menuHtmlSBuffer.toString();
        }
    }


    /**
     * 通过pin得到此用户的按钮资源(带有url)集合
     * @param pin
     * @return
     */
    public List<Resources> loadButtonResourceList(String pin) {
        Multimap<String, Resources> resourcesButton_userIdMultimap = authorityData.resourcesButton_userIdMultimap;
        List<Resources> resourcesList = (List<Resources>) resourcesButton_userIdMultimap.get(pin);
        return resourcesList;
    }
    /**
     * 通过pin得到此用户的带有url资源集合
     * @param pin
     * @return
     */
    public List<String> loadUrlResourceList(String pin) {
        Multimap<String, Resources> resourcesButton_userIdMultimap = authorityData.resourcesUrl_userIdMultimap;
        List<Resources> resourcesList = (List<Resources>) resourcesButton_userIdMultimap.get(pin);
        List<String> resourcesUrlList = new ArrayList<String>();
        if(resourcesList!=null&&resourcesList.size()>0){
            for(Resources resources:resourcesList){
                resourcesUrlList.add(resources.getResourceUrl());
            }
        }
        return resourcesUrlList;
    }
    /**
     * 递归拼装用户的menu展示html
     * @param resourceCode
     * @param menuHtmlSBuffer
     * @param resourceParentCodeMultimap
     */
    private void recursionMenuHtmlByResourceCodeFromDownTop(String resourceCode, StringBuffer menuHtmlSBuffer,Multimap<String, Resources> resourceParentCodeMultimap) {
        List<Resources> resourcesList = (List<Resources>) resourceParentCodeMultimap.get(resourceCode);
        if (resourcesList != null && resourcesList.size() > 0) {
            resourcesList =  Lambda.sort(resourcesList, Lambda.on(Resources.class).getDisplayIndex(), new Comparator<Integer>() {//页签排序
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1.intValue() - o2.intValue();
                }
            });
            for (Resources resources : resourcesList) {
                if (resources != null) {
                    String name = resources.getResourceName();
                    String icon = resources.getResourceIcon();
                    if (resources.getNodeType().equals(NodeTypeEnum.LEAF_NODE.getType())) {
                        String url = resources.getResourceUrl();
                        url = url.replaceFirst("\\/", "#");
                        String leafNodehtml = "<li><a class=\"menu_resource\" href=\"javascript:void(0)\" rel=\"" + url + "\" title=\"" + name + "\" desc=\"" + name + "\"><i class=\"" + icon + "\"></i> <span>" + name + "</span>";
                        if(resources.getResourceId()!=null&&!resources.getResourceId().equals("")){
                            leafNodehtml = leafNodehtml +   "<span id=\""+resources.getResourceId()+"\" class=\"pull-right-container\"></span>";
                        }
                        leafNodehtml = leafNodehtml+ "</a></li>";

                        menuHtmlSBuffer.append(leafNodehtml);
                    }
                    if (resources.getNodeType().equals(NodeTypeEnum.BRANCH_NODE.getType())) {
                        String leafNodehtml = "<li class=\"treeview active\"><a href=\"#\"><i class=\"" + icon + "\"></i> <span>" + name + "</span>";
                        if(resources.getResourceId()!=null&&!resources.getResourceId().equals("")){
                            leafNodehtml = leafNodehtml +   "<span id=\""+resources.getResourceId()+"\" class=\"pull-right-container\"></span>";
                        }
                        leafNodehtml = leafNodehtml+ "</a>";
                        menuHtmlSBuffer.append(leafNodehtml);
                        menuHtmlSBuffer.append("<ul class=\"treeview-menu\">");
                        recursionMenuHtmlByResourceCodeFromDownTop(resources.getResourceCode(), menuHtmlSBuffer,resourceParentCodeMultimap);
                        menuHtmlSBuffer.append("</ul></li>");
                    }
                }
            }
        }
    }


    public AuthorityResourcesLoad getAuthorityData() {
        return authorityData;
    }

    public void setAuthorityData(AuthorityResourcesLoad authorityData) {
        this.authorityData = authorityData;
    }

    public String getProfiles() {
        return profiles;
    }

    public void setProfiles(String profiles) {
        this.profiles = profiles;
    }
}
