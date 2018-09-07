package com.daemonauth.export.proxy;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HttpContext;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.io.IOException;
import java.util.List;

/**
 *
 */
public class PoolingHttpClient implements HttpClient, InitializingBean, DisposableBean {

    public static int DEFAULT_CONNECT_TIMEOUT = 2000;
    public static int DEFAULT_READ_TIMEOUT = 3000;
    public static int DEFAULT_WAIT_TIMEOUT = 1000;
    public static int DEFAULT_PERHOST_MAX_CONNECTIONS = 1000;
    public static int DEFAULT_MAX_CONNECTIONS = 8000;
    private static int DEFAULT_HTTP_PORT = 80;
    private static int DEFAULT_HTTPS_PORT = 443;
    private int connectTimeout = DEFAULT_CONNECT_TIMEOUT;
    private int readTimeout = DEFAULT_READ_TIMEOUT;
    private int waitTimeout = DEFAULT_WAIT_TIMEOUT;
    private int totalMaxConnection = DEFAULT_MAX_CONNECTIONS;

    private List<String> httpHostConfigs;

    private HttpClient httpClient;

    public PoolingHttpClient() {
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (httpClient == null) {

            HttpParams params = new BasicHttpParams();

            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);

            HttpConnectionParams.setConnectionTimeout(params, connectTimeout);
            HttpConnectionParams.setSoTimeout(params, readTimeout);

            HttpClientParams.setConnectionManagerTimeout(params, waitTimeout);

            SchemeRegistry schemeRegistry = new SchemeRegistry();
            schemeRegistry.register(new Scheme("http", DEFAULT_HTTP_PORT, PlainSocketFactory.getSocketFactory()));
            schemeRegistry.register(new Scheme("https", DEFAULT_HTTPS_PORT, SSLSocketFactory.getSocketFactory()));

            PoolingClientConnectionManager connectionManager = new PoolingClientConnectionManager(schemeRegistry);

            connectionManager.setDefaultMaxPerRoute(DEFAULT_PERHOST_MAX_CONNECTIONS);
            if (httpHostConfigs != null && httpHostConfigs.size() > 0) {
                for (String httpHostConfig : httpHostConfigs) {
                    String[] hostConfigs = httpHostConfig.split(",");

                    if (hostConfigs.length < 3)
                        throw new RuntimeException("HttpClient4 httpHostConfigs error!!!");

                    int _port;
                    try {
                        _port = Integer.parseInt(hostConfigs[1]);
                    } catch (NumberFormatException e) {
                        _port = DEFAULT_HTTP_PORT;
                    }

                    int _perhostMaxConnection;
                    try {
                        _perhostMaxConnection = Integer.parseInt(hostConfigs[2]);
                    } catch (NumberFormatException e) {
                        _perhostMaxConnection = DEFAULT_PERHOST_MAX_CONNECTIONS;
                    }

                    HttpHost _httpHost = new HttpHost(hostConfigs[0], _port);

                    connectionManager.setMaxPerRoute(new HttpRoute(_httpHost), _perhostMaxConnection);
                    connectionManager.setDefaultMaxPerRoute(_perhostMaxConnection);
                }
            }

            connectionManager.setMaxTotal(totalMaxConnection);

            httpClient = new DefaultHttpClient(connectionManager, params);

        }
    }

    @Override
    public HttpParams getParams() {
        return httpClient.getParams();
    }

    @Override
    public ClientConnectionManager getConnectionManager() {
        return httpClient.getConnectionManager();
    }

    @Override
    public HttpResponse execute(HttpUriRequest request) throws IOException {
        return httpClient.execute(request);
    }

    @Override
    public HttpResponse execute(HttpUriRequest request, HttpContext context) throws IOException {
        return httpClient.execute(request, context);
    }

    @Override
    public HttpResponse execute(HttpHost target, HttpRequest request) throws IOException {
        return httpClient.execute(target, request);
    }

    @Override
    public HttpResponse execute(HttpHost target, HttpRequest request, HttpContext context) throws IOException {
        return httpClient.execute(target, request, context);
    }

    @Override
    public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler) throws IOException {
        return httpClient.execute(request, responseHandler);
    }

    @Override
    public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context) throws IOException {
        return httpClient.execute(request, responseHandler, context);
    }

    @Override
    public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler) throws IOException {
        return httpClient.execute(target, request, responseHandler);
    }

    @Override
    public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context) throws IOException {
        return httpClient.execute(target, request, responseHandler, context);
    }

    @Override
    public void destroy() throws Exception {
        if (httpClient != null) {
            httpClient.getConnectionManager().shutdown();
            httpClient = null;
        }
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public void setWaitTimeout(int waitTimeout) {
        this.waitTimeout = waitTimeout;
    }

    public void setTotalMaxConnection(int totalMaxConnection) {
        this.totalMaxConnection = totalMaxConnection;
    }

    public void setHttpHostConfigs(List<String> httpHostConfigs) {
        this.httpHostConfigs = httpHostConfigs;
    }


}
