package com.skyguard.kproxy.task;

import com.skyguard.kproxy.processor.HttpRequestProcessor;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

public class HttpServerTask implements Callable<Object>{

    private static final Logger LOG = LoggerFactory.getLogger(HttpServerTask.class);


    private Object msg;

    public HttpServerTask(Object msg){
        this.msg = msg;
    }


    @Override
    public Object call() throws Exception {

        try{
            String uri = null;
            String contentMsg = null;
            HttpMethod method = null;
            String result = "";
            if (msg instanceof HttpRequest) {
                HttpRequest request = (HttpRequest)msg;
                uri = request.uri();
                method = request.method();
                LOG.info("uri:"+uri);
            }
            if (msg instanceof HttpContent) {
                HttpContent httpContent = (HttpContent) msg;
                ByteBuf content = httpContent.content();
                if (content.isReadable()) {
                    contentMsg=content.toString(CharsetUtil.UTF_8);
                }
            }
            if(StringUtils.isNotEmpty(uri)){
                result = HttpRequestProcessor.processRequest(uri,method,contentMsg);
            }
            return result;
        }catch(Exception e){
            LOG.error("get data error",e);
            return null;
        }finally{
            ReferenceCountUtil.release(msg);
        }


    }
}
