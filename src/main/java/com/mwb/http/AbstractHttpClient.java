package com.mwb.http;

import java.util.Map;

/**
 * Created by MengWeiBo on 2017-03-29
 */

public abstract class AbstractHttpClient {
    private SimpleHttpClient httpClient;

    private boolean mock;
    
    private int maxConnection;
    private int connectionTimeout;
    private int socketTimeout;
    
    public void init() throws Exception {
        httpClient = new SimpleHttpClient(maxConnection, connectionTimeout, socketTimeout);
    }

    public String get(String url, Map<String, String> params, Map<String, String> headers) throws Exception {
        return httpClient.get(url, params, headers);
    }
    
    public String post(String url, Map<String, String> params, Map<String, String> headers) throws Exception {
        return httpClient.post(url, params, headers);
    } 
    
    public String post2XX(String url, Map<String, String> params, Map<String, String> headers) throws Exception {
        return httpClient.post2XX(url, params, headers);
    }
    
    public boolean isMock() {
        return mock;
    }

    public void setMock(boolean mock) {
        this.mock = mock;
    }

    public int getMaxConnection() {
        return maxConnection;
    }

    public void setMaxConnection(int maxConnection) {
        this.maxConnection = maxConnection;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

}
