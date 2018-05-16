package com.tmhp.ssoclient.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtil.class);

    public static final String CONTENT_TYPE = "application/json;charset=UTF-8";
    public static final String UTF_8 = "UTF-8";
    public static final int CONNECTION_TIMEOUT = 60000;

    private static RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(CONNECTION_TIMEOUT).setConnectTimeout(CONNECTION_TIMEOUT)
            .build();

    /**
     * 发送 post请求
     * @param httpUrl 地址
     * @param maps 参数
     * @throws Exception
     */
    public static String sendHttpPost(String httpUrl, Map<String, Object> maps) throws Exception {
        HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
        // 创建参数队列
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        for (String key : maps.keySet()) {
            nameValuePairs.add(new BasicNameValuePair(key, maps.get(key).toString()));
        }
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, UTF_8));
        return sendHttpPost(httpPost);
    }

    /**
     * 发送 post请求
     * @param httpUrl 地址
     * @param maps 参数
     * @throws Exception
     */
    public static String sendHttpPost(String url, String json) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        //System.out.println(CONTENT_TYPE);
        httpPost.addHeader("content-type", CONTENT_TYPE);
        httpPost.setHeader("Accept", "application/json");
        // List<NameValuePair> list = new ArrayList<NameValuePair>();
        // list.add(new BasicNameValuePair("CREDIT_LEVEL",));
        // UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,UTF_8);
        httpPost.setEntity(new StringEntity(json, UTF_8));

        return sendHttpPost(httpPost);
    }

    /**
     * 发送Post请求
     * @param httpPost
     * @return
     * @throws Exception
     */
    private static String sendHttpPost(HttpPost httpPost) throws Exception {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        String responseContent = null;
        try {
            // 创建默认的httpClient实例.
            httpClient = HttpClients.createDefault();
            httpPost.setConfig(requestConfig);
            // 执行请求
            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            entity = response.getEntity();
            responseContent = EntityUtils.toString(entity, UTF_8);
            if (statusCode != HttpStatus.SC_OK) {
                LOGGER.error("responseContent=" + responseContent);
                throw new Exception(responseContent);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        } finally {
            try {
                // 关闭连接,释放资源
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
                throw e;
            }
        }
        return responseContent;
    }

    /**
     * 发送 get请求
     * @param httpUrl
     * @throws Exception
     */
    public static String sendHttpGet(String httpUrl, Map<String, Object> maps) throws Exception {
        // 封装参数
        StringBuffer param = new StringBuffer();
        int i = 0;
        for (String key : maps.keySet()) {
            if (i == 0) {
                param.append("?");
            } else {
                param.append("&");
            }
            param.append(key).append("=").append(maps.get(key));
            i++;
        }
        httpUrl += param;
        HttpGet httpGet = new HttpGet(httpUrl);// 创建get请求
        return sendHttpGet(httpGet);
    }

    /**
     * 发送Get请求
     * @param httpPost
     * @return
     * @throws Exception
     */
    private static String sendHttpGet(HttpGet httpGet) throws Exception {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        String responseContent = null;

        try {
            // 创建默认的httpClient实例.
            httpClient = HttpClients.createDefault();
            httpGet.setConfig(requestConfig);
            // 执行请求
            response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            entity = response.getEntity();
            responseContent = EntityUtils.toString(entity, "UTF-8");
            if (statusCode != HttpStatus.SC_OK) {
                LOGGER.error("responseContent=" + responseContent);
                throw new Exception(responseContent);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        } finally {
            try {
                // 关闭连接,释放资源
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
                throw e;
            }
        }
        return responseContent;
    }
}
