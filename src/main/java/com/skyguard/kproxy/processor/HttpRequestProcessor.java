package com.skyguard.kproxy.processor;


import com.skyguard.kproxy.cache.RedisCache;
import com.skyguard.kproxy.config.ServiceConfig;
import com.skyguard.kproxy.util.HttpClientUtil;
import com.skyguard.kproxy.util.SpringUtil;
import io.netty.handler.codec.http.HttpMethod;


public class HttpRequestProcessor {

    public static String processRequest(String uri, HttpMethod method, String content) throws Exception{

        uri = uri.substring(1);
        String host = getHost(uri);
        uri = uri.substring(uri.indexOf("/")+1);
        RedisCache redisCache = SpringUtil.getBeanByClass(RedisCache.class);
        ServiceConfig serviceConfig = redisCache.getServiceConfig(host);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("http://").append(serviceConfig.getIp()).append(":").append(serviceConfig.getPort())
                .append("/").append(uri);

        String result = "";

        String request = stringBuilder.toString();

        if(method== HttpMethod.GET){
            result = HttpClientUtil.getData(request);
        }else if(method== HttpMethod.POST){
            result = HttpClientUtil.postData(request,content);
        }

        return result;
    }

    private static String getHost(String uri){

        String host = uri.substring(0,uri.indexOf("/"));

        return host;
    }



}
