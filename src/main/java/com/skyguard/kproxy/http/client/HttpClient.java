package com.skyguard.kproxy.http.client;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

public class HttpClient {

    private static final Logger LOG = LoggerFactory.getLogger(HttpClient.class);

    private ChannelFuture future;

    private String host;

    private int port;

    public HttpClient(ChannelFuture future, String host, int port){
        this.future = future;
        this.host = host;
        this.port = port;
    }


    public void sendRequest(String url,String msg){

        try {

            URI uri = new URI("/"+url);

            if (future.channel().isOpen()) {
                DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET,
                        uri.toASCIIString(), Unpooled.wrappedBuffer(msg.getBytes("UTF-8")));

                // 构建http请求
                request.headers().set(HttpHeaderNames.HOST, host);
                request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                request.headers().set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
                ChannelFuture writeFuture = future.channel().writeAndFlush(request);
                // use listener to avoid wait for write & thread context switch
                writeFuture.addListener(new ChannelFutureListener() {
                    public void operationComplete(ChannelFuture future)
                            throws Exception {
                        if (future.isSuccess()) {
                            return;
                        }
                        String errorMsg = "";
                        // write timeout

                        if (future.isCancelled()) {
                            errorMsg = "Send request to " + future.channel().toString()
                                    + " cancelled by user";
                        } else if (!future.isSuccess()) {
                            if (future.channel().isOpen()) {
                                // maybe some exception,so close the channel
                                future.channel().close();
                                }
                            errorMsg = "Send request to " + future.channel().toString() + " error" + future.cause();
                            LOG.error(errorMsg);
                        }
                        }
                });
            }

        }catch (Exception e){
            LOG.error("get data error",e);
        }





    }




}
