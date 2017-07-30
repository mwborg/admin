package com.mwb.http;

import com.mwb.dao.modle.Log;
import com.mwb.http.method.PostMethodWithGzip;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.protocol.Protocol;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class SimpleHttpClient {
	
	private static final Log LOG = Log.getLog(SimpleHttpClient.class);
	
    private HttpClient httpClient;
    private String ipAddress;

    public SimpleHttpClient(int maxConnection, int connectionTimeout, int socketTimeout) throws UnknownHostException {
        MultiThreadedHttpConnectionManager connectionManager 
        = new MultiThreadedHttpConnectionManager();

        HttpConnectionManagerParams connectionManagerParams = connectionManager.getParams();
        connectionManagerParams.setDefaultMaxConnectionsPerHost(maxConnection);
        connectionManagerParams.setConnectionTimeout(connectionTimeout);
        connectionManagerParams.setSoTimeout(socketTimeout);

        HttpClientParams clientParams = new HttpClientParams();

        // 忽略cookie 避免 Cookie rejected 警告
        clientParams.setCookiePolicy(CookiePolicy.IGNORE_COOKIES);

        httpClient = new HttpClient(clientParams, connectionManager);

        Protocol https = new Protocol("https", new SSLSocketFactory(), 443);
        Protocol.registerProtocol("https", https);

        ipAddress = InetAddress.getLocalHost().getHostAddress();
    }

    public String post(String url, Map<String, String> params, Map<String, String> headers) throws Exception {
        PostMethod method = null;
        try {
        	method = processPost(url, params, headers);

            if (method.getStatusCode() == 200) {
                return method.getResponseBodyAsString();
            } else {
                throw new Exception("POST request failed - status code " + method.getStatusCode());
            }
        } finally {
            if (method != null) {
                method.releaseConnection();
            }
        }
    }
    
    public String post2XX (String url, Map<String, String> params, Map<String, String> headers) throws Exception {
        PostMethod method = null;
        try {
        	method = processPost(url, params, headers);

            if (String.valueOf(method.getStatusCode()).startsWith("2")) {
            	LOG.debug("2XX response code {}, reponse msg {} ", method.getStatusCode(), method.getResponseBodyAsString());
                return method.getResponseBodyAsString();
            } else {
            	LOG.error("non-2XX reponse code {}, response msg {} ", method.getStatusCode(), method.getResponseBodyAsString());
                throw new Exception("POST request failed - status code " + method.getStatusCode());
            }
        } finally {
            if (method != null) {
                method.releaseConnection();
            }
        }
    }
    
    private PostMethod processPost (String url, Map<String, String> params, Map<String, String> headers) throws Exception {

        PostMethod method = new PostMethodWithGzip(url);

        if (params != null) {
            Set<Entry<String, String>> entrySet = params.entrySet();
            Iterator<Entry<String, String>> iterator = entrySet.iterator();
            while (iterator.hasNext()) {
                Entry<String, String> entry = iterator.next();
                method.addParameter(entry.getKey(), entry.getValue());
            }
        }

        if (headers != null) {
            Set<Entry<String, String>> entrySet = headers.entrySet();
            Iterator<Entry<String, String>> iterator = entrySet.iterator();
            while (iterator.hasNext()) {
                Entry<String, String> entry = iterator.next();
                method.addRequestHeader(entry.getKey(), entry.getValue());
            }
        }

        method.addRequestHeader("API-RemoteIP", ipAddress);

        method.getParams().setContentCharset("UTF-8");
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));

        httpClient.executeMethod(method);
        
        return method;
    }

    public String get(String url, Map<String, String> params, Map<String, String> headers) throws Exception {
        GetMethod method = null;

        try {
            if (params != null && params.size() > 0) {
                StringBuilder sb = new StringBuilder();
                sb.append(url).append("?");
                Set<Entry<String, String>> entrySet = params.entrySet();
                Iterator<Entry<String, String>> iterator = entrySet.iterator();
                while (iterator.hasNext()) {
                    Entry<String, String> entry = iterator.next();
                    sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");

                }
                url = sb.toString();
            }

            method = new GetMethod(url);

            if (headers != null && headers.size() > 0) {
                Set<Entry<String, String>> entrySet = headers.entrySet();
                Iterator<Entry<String, String>> iterator = entrySet.iterator();
                while (iterator.hasNext()) {
                    Entry<String, String> entry = iterator.next();
                    method.addRequestHeader(entry.getKey(), entry.getValue());
                }
            }

            method.addRequestHeader("API-RemoteIP", ipAddress);

            method.getParams().setContentCharset("UTF-8");
            method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));

            httpClient.executeMethod(method);

            if (method.getStatusCode() == 200) {
                return method.getResponseBodyAsString();
            } else {
                throw new Exception("GET request failed - status code " + method.getStatusCode());
            }
        } finally {
            if (method != null) {
                method.releaseConnection();
            }
        }
    }
    
}
