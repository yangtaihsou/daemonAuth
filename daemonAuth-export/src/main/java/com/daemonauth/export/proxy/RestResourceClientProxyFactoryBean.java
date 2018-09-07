package com.daemonauth.export.proxy;

import com.daemonauth.export.rpc.annotion.Controller;
import com.daemonauth.export.rpc.annotion.RequestMapping;
import com.daemonauth.export.rpc.annotion.RequestMethod;
import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * User:
 * Date: 15-1-28
 * Time: 下午1:39
 */
public class RestResourceClientProxyFactoryBean<T> implements FactoryBean,
        InitializingBean {
    Map<Method, HttpClientInvoker> methodMap = new HashMap<Method, HttpClientInvoker>();
    private Class<T> serviceInterface;
    private URI baseUri;
    private HttpClient httpClient;
    private T client;

    @Override
    public Object getObject() throws Exception {
        return client;
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        analyzeClassAnnotion();
        Class<?>[] intfs = {serviceInterface};
        client = (T) Proxy.newProxyInstance(serviceInterface.getClassLoader(), intfs,
                new InvocationHandlerImpl(serviceInterface, methodMap));
    }

    private void analyzeClassAnnotion() throws Exception {
        Annotation[] classAnnotations = serviceInterface.getAnnotations();
        if (!serviceInterface.isAnnotationPresent(Controller.class)) {
            throw new Exception("资源类不符合格式，必须带有注解是Controller的");
        }
        String classPath = "";
        for (Annotation annotation : classAnnotations) {
            if (annotation instanceof RequestMapping) {
                RequestMapping mapping = serviceInterface.getAnnotation(RequestMapping.class);
                classPath = mapping.path();
            }
        }
        Method[] methods = serviceInterface.getMethods();
        for (Method method : methods) {
                    /*  Annotation[] annotations =  method.getAnnotations();
                  for(Annotation annotation:annotations){
                        if(annotation instanceof RequestMapping){
                            RequestMapping mapping = method.getAnnotation(RequestMapping.class);
                            String path = mapping.path();
                            break;
                        }
                    }*/
            if (method.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping mapping = method.getAnnotation(RequestMapping.class);
                String path = mapping.path();
                RequestMethod requestMethod = mapping.method();
                HttpClientInvoker httpClientInvoker = new HttpClientInvoker();
                httpClientInvoker.setBaseURL(baseUri.toString());
                httpClientInvoker.setRequetURL(classPath + path);
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
    }

    public Class<T> getServiceInterface() {
        return serviceInterface;
    }

    public void setServiceInterface(Class<T> serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

    public URI getBaseUri() {
        return baseUri;
    }

    public void setBaseUri(URI baseUri) {
        this.baseUri = baseUri;
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }
}

