package com.daemonauth.export;

import com.daemonauth.domain.Roles;
import com.daemonauth.domain.query.Query;
import com.daemonauth.export.proxy.HttpClientInvoker;
import com.daemonauth.export.proxy.InvocationHandlerImpl;
import com.daemonauth.export.rpc.annotion.Controller;
import com.daemonauth.export.rpc.annotion.RequestMapping;
import com.daemonauth.export.rpc.annotion.RequestMethod;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * User:
 * Date: 15-1-27
 * Time: 下午8:11
 */

public class PackageUtil {
    public static void main(String[] args) throws Exception {
        Map<Method, HttpClientInvoker> methodMap = new HashMap<Method, HttpClientInvoker>();
        HttpClient httpClient = new DefaultHttpClient();
        String baseURL = "http://127.0.0.1";
        String packageName = "com.daemonauth.export.rpc";
        // List<String> classNames = getClassName(packageName);
        List<String> classNames = getClassName(packageName, false);
        if (classNames != null) {
            for (String className : classNames) {
                System.out.println(className);
                Class<?> t = Class.forName(className);

                System.out.println(t.isAnnotationPresent(Controller.class));
                Annotation[] classAnnotations = t.getAnnotations();
                String parentPath = "";
                for (Annotation annotation : classAnnotations) {
                    if (annotation instanceof RequestMapping) {
                        RequestMapping mapping = t.getAnnotation(RequestMapping.class);
                        parentPath = mapping.path();
                    }
                }
                System.out.println("根路径--" + parentPath);
                Method[] methods = t.getMethods();
                for (Method method : methods) {
                    System.out.println(method.getGenericReturnType());
                    /*  Annotation[] annotations =  method.getAnnotations();
                  for(Annotation annotation:annotations){
                        if(annotation instanceof RequestMapping){
                            RequestMapping mapping = method.getAnnotation(RequestMapping.class);
                            String path = mapping.path();
                            System.out.println("++++"+mapping.path());
                            System.out.println("++++"+mapping.method());
                            break;
                        }
                    }*/
                    if (method.isAnnotationPresent(RequestMapping.class)) {
                        RequestMapping mapping = method.getAnnotation(RequestMapping.class);
                        String path = mapping.path();
                        RequestMethod requestMethod = mapping.method();
                        HttpClientInvoker httpClientInvoker = new HttpClientInvoker();
                        httpClientInvoker.setBaseURL(baseURL);
                        httpClientInvoker.setRequetURL(parentPath + path);
                        httpClientInvoker.setReturnType(method.getGenericReturnType());
                        Type[] genericParameterTypes = method.getGenericParameterTypes();
                        if (genericParameterTypes.length > 0) {
                            httpClientInvoker.setParameterType(method.getGenericParameterTypes()[0]);//目前支持一个参数
                        }

                        httpClientInvoker.setHttpMethod(requestMethod.name());
                        httpClientInvoker.setHttpClient(httpClient);
                        methodMap.put(method, httpClientInvoker);
                    }
                }
         /*       Method targetMethod = AuthorityResourceService.class.getMethod("getRolesList",Query.class);
                Query<Roles> rolesQuery = new Query<Roles>();
                Roles roles = new Roles();
                roles.setRoleCode("admin");
                rolesQuery.setQuery(roles);
                Object obj = methodMap.get(targetMethod).invoke(rolesQuery);
                Gson gson = new Gson();
                System.out.println(gson.toJson(obj));*/
                Query<Roles> rolesQuery = new Query<Roles>();
                Roles roles = new Roles();
                roles.setRoleCode("admin");
                rolesQuery.setQuery(roles);
                Class<?>[] intfs = {t};
                Object client = Proxy.newProxyInstance(t.getClassLoader(), intfs,
                        new InvocationHandlerImpl(t, methodMap));
                System.out.println(client);
                Proxy proxy = (Proxy) client;
                System.out.println(proxy.getClass().getComponentType());

                // client.getRolesList(null);
            }

            //  methodMap.get()
        }
    }

    /**
     * 获取某包下（包括该包的所有子包）所有类
     *
     * @param packageName 包名
     * @return 类的完整名称
     */
    public static List<String> getClassName(String packageName) {
        return getClassName(packageName, true);
    }

    /**
     * 获取某包下所有类
     *
     * @param packageName  包名
     * @param childPackage 是否遍历子包
     * @return 类的完整名称
     */
    public static List<String> getClassName(String packageName, boolean childPackage) {
        List<String> fileNames = null;
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        String packagePath = packageName.replace(".", "/");
        URL url = loader.getResource(packagePath);
        if (url != null) {
            String type = url.getProtocol();
            if (type.equals("file")) {
                fileNames = getClassNameByFile(url.getPath(), null, childPackage);
            } else if (type.equals("jar")) {
                fileNames = getClassNameByJar(url.getPath(), childPackage);
            }
        } else {
            fileNames = getClassNameByJars(((URLClassLoader) loader).getURLs(), packagePath, childPackage);
        }
        return fileNames;
    }

    /**
     * 从项目文件获取某包下所有类
     *
     * @param filePath     文件路径
     * @param className    类名集合
     * @param childPackage 是否遍历子包
     * @return 类的完整名称
     */
    private static List<String> getClassNameByFile(String filePath, List<String> className, boolean childPackage) {
        List<String> myClassName = new ArrayList<String>();
        File file = new File(filePath);
        File[] childFiles = file.listFiles();
        for (File childFile : childFiles) {
            if (childFile.isDirectory()) {
                if (childPackage) {
                    myClassName.addAll(getClassNameByFile(childFile.getPath(), myClassName, childPackage));
                }
            } else {
                String childFilePath = childFile.getPath();
                if (childFilePath.endsWith(".class")) {
                    childFilePath = childFilePath.substring(childFilePath.indexOf("\\classes") + 9, childFilePath.lastIndexOf("."));
                    childFilePath = childFilePath.replace("\\", ".");
                    myClassName.add(childFilePath);
                }
            }
        }

        return myClassName;
    }

    /**
     * 从jar获取某包下所有类
     *
     * @param jarPath      jar文件路径
     * @param childPackage 是否遍历子包
     * @return 类的完整名称
     */
    private static List<String> getClassNameByJar(String jarPath, boolean childPackage) {
        List<String> myClassName = new ArrayList<String>();
        String[] jarInfo = jarPath.split("!");
        String jarFilePath = jarInfo[0].substring(jarInfo[0].indexOf("/"));
        String packagePath = jarInfo[1].substring(1);
        try {
            JarFile jarFile = new JarFile(jarFilePath);
            Enumeration<JarEntry> entrys = jarFile.entries();
            while (entrys.hasMoreElements()) {
                JarEntry jarEntry = entrys.nextElement();
                String entryName = jarEntry.getName();
                if (entryName.endsWith(".class")) {
                    if (childPackage) {
                        if (entryName.startsWith(packagePath)) {
                            entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
                            myClassName.add(entryName);
                        }
                    } else {
                        int index = entryName.lastIndexOf("/");
                        String myPackagePath;
                        if (index != -1) {
                            myPackagePath = entryName.substring(0, index);
                        } else {
                            myPackagePath = entryName;
                        }
                        if (myPackagePath.equals(packagePath)) {
                            entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
                            myClassName.add(entryName);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myClassName;
    }

    /**
     * 从所有jar中搜索该包，并获取该包下所有类
     *
     * @param urls         URL集合
     * @param packagePath  包路径
     * @param childPackage 是否遍历子包
     * @return 类的完整名称
     */
    private static List<String> getClassNameByJars(URL[] urls, String packagePath, boolean childPackage) {
        List<String> myClassName = new ArrayList<String>();
        if (urls != null) {
            for (int i = 0; i < urls.length; i++) {
                URL url = urls[i];
                String urlPath = url.getPath();
                // 不必搜索classes文件夹
                if (urlPath.endsWith("classes/")) {
                    continue;
                }
                String jarPath = urlPath + "!/" + packagePath;
                myClassName.addAll(getClassNameByJar(jarPath, childPackage));
            }
        }
        return myClassName;
    }
}