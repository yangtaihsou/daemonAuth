package com.daemonauth.export.proxy;

import com.daemonauth.export.GsonUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ByteArrayEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Type;


/**
 * User:
 * Date: 15-1-28
 * Time: 下午2:47
 */
public class HttpClientInvoker {
    private final static Logger log = LoggerFactory.getLogger(HttpClientInvoker.class);
    private String httpMethod;
    private String baseURL;
    private String requetURL;
    private Type returnType;
    private Type parameterType;
    private HttpClient httpClient;

    public Object invoke(Object args) throws Exception {
        HttpResponse response = httpClient.execute(httpMethod(args));
        HttpEntity resEntity = response.getEntity();
        InputStream is = resEntity.getContent();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int ch;
        while ((ch = is.read()) != -1) {
            baos.write(ch);
        }
        byte bytes[] = baos.toByteArray();
        String responseContent = new String(bytes, "UTF-8");
        /*log.info("HttpClientInvoker invoke{}",responseContent);*/
        return GsonUtils.fromJson(responseContent, returnType);
    }

    private HttpUriRequest httpMethod(Object args) throws Exception {
        String httpUrl = baseURL + requetURL;
        if ("GET".equals(httpMethod)) {
            return new HttpGet(httpUrl);
        } else if ("POST".equals(httpMethod)) {
            HttpPost httppost = new HttpPost(httpUrl);
            String requestData = "";//默认将数据转成json格式传送，待优化。添加注解表示用什么数据格式
            if (parameterType == null) {
                requestData = GsonUtils.toJson(args);//需要指定type
            } else {
                requestData = GsonUtils.toJson(args, parameterType);//需要指定type
            }
            ByteArrayEntity reqEntity = new ByteArrayEntity(requestData.getBytes("UTF-8"));
            httppost.setEntity(reqEntity);
            return httppost;
        } else if ("DELETE".equals(httpMethod)) {
            return new HttpDelete(httpUrl);
        } else if ("DELETE".equals(httpMethod)) {
            return new HttpPut(httpUrl);
        } else if ("TRACE".equals(httpMethod)) {
            return new HttpTrace(httpUrl);
        }
        return null;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getRequetURL() {
        return requetURL;
    }

    public void setRequetURL(String requetURL) {
        this.requetURL = requetURL;
    }

    public Type getReturnType() {
        return returnType;
    }

    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public String getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    public Type getParameterType() {
        return parameterType;
    }

    public void setParameterType(Type parameterType) {
        this.parameterType = parameterType;
    }
}
