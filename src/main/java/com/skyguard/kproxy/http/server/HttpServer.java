package com.skyguard.kproxy.http.server;

import com.skyguard.kproxy.config.SysConfig;
import com.skyguard.kproxy.netty.server.NettyHttpServer;
import com.skyguard.kproxy.util.PropertyUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpServer {

    private static final Logger LOG = LoggerFactory.getLogger(HttpServer.class);

    private static String connectTimeout = PropertyUtil.getValue(SysConfig.CONNECT_TIMEOUT);
    private static String connectPort = PropertyUtil.getValue(SysConfig.CONNECT_PORT);

    private static NettyHttpServer nettyHttpServer = new NettyHttpServer();

      public void start() throws Exception{

          int timeout = 2000;
          if(StringUtils.isNotEmpty(connectTimeout)){
              timeout = Integer.parseInt(connectTimeout);
          }

          int port = 8891;
          if(StringUtils.isNotEmpty(connectPort)){
              port = Integer.parseInt(connectPort);
          }

          nettyHttpServer.start(port,timeout);


      }

      public void stop() throws Exception{
          nettyHttpServer.stop();
      }



}
