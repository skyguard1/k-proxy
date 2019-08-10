package com.skyguard.kproxy.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientUtil {

    private static final Logger LOG = LoggerFactory.getLogger(HttpClientUtil.class);


    public static String getData(String url) throws Exception{

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);


        CloseableHttpResponse response = httpClient.execute(httpGet);
        int responseCode = response.getStatusLine().getStatusCode();

        if(responseCode==200) {
            HttpEntity httpEntity = response.getEntity();
            if (httpEntity != null) {
                String result = EntityUtils.toString(httpEntity);
                return result;
            }
        }else {
            LOG.error("send request error,responseCode is {}",responseCode);
        }

        return null;
    }

    public static String postData(String url, String param)throws Exception{

        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpEntity entity = new StringEntity(param, ContentType.APPLICATION_JSON);
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(entity);


        CloseableHttpResponse response = httpClient.execute(httpPost);
        int responseCode = response.getStatusLine().getStatusCode();

        if(responseCode==200) {
            HttpEntity httpEntity = response.getEntity();
            if (httpEntity != null) {
                String result = EntityUtils.toString(httpEntity);
                return result;
            }
        }else {
            LOG.error("send request error,responseCode is {}",responseCode);
        }

        return null;
    }




}
