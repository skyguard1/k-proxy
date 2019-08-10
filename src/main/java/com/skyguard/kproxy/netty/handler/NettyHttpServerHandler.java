package com.skyguard.kproxy.netty.handler;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.skyguard.kproxy.task.HttpServerTask;
import com.skyguard.kproxy.task.TaskExecutor;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class NettyHttpServerHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(NettyHttpServerHandler.class);




    private int timeout;

    public NettyHttpServerHandler(int timeout){
        this.timeout = timeout;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }



    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {

        ListeningExecutorService service = TaskExecutor.getInstance().getService();

        ListenableFuture<Object> future = service.submit(new HttpServerTask(msg));
        try{
            Object result=future.get(timeout,TimeUnit.MILLISECONDS);
            if(result!=null){
                String res = result.toString();
                FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(res.getBytes("UTF-8")));
                response.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
                response.headers().set(HttpHeaderNames.CONTENT_LENGTH,
                        response.content().readableBytes());
                if(ctx.channel().isOpen()&&result!=null){
                    ctx.writeAndFlush(response);
                }
            }
        }catch(Exception e){
            DefaultHttpResponse httpResponse=new DefaultHttpResponse(HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR);
            httpResponse.headers().add(HttpHeaderNames.TRANSFER_ENCODING,
                    HttpHeaderValues.CHUNKED);

            httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE,
                    HttpHeaderValues.APPLICATION_JSON);

            httpResponse.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
            httpResponse.headers().set(HttpHeaderNames.CACHE_CONTROL,"no-cache");
            httpResponse.headers().set(HttpHeaderNames.PRAGMA,"no-cache");
            httpResponse.headers().set(HttpHeaderNames.EXPIRES,"-1");
            DefaultHttpContent defaultHttpContent = new DefaultHttpContent(Unpooled.copiedBuffer(e.getMessage(), CharsetUtil.UTF_8));
            ctx.write(httpResponse);
            ctx.writeAndFlush(defaultHttpContent);
        }




    }




}
