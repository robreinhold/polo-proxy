package com.expedia.poloproxy;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.router.Router;

import java.util.logging.Logger;

@SuppressWarnings({"PMD.UseUtilityClass"})
public class NettyRouterStarter {

    private static final Logger LOG = Logger.getLogger(NettyRouterStarter.class.getName());
    private static final int PORT = 5050;
    public static final String DISCOVER_SERVICE = "DISCOVER";
    public static final String NOT_FOUND = "404 Not Found";

    public static void main(String[] args) throws Exception {
        Banner.printToLog();

        Router<String> router = new Router<String>()
                .GET("/", "Index")
                .GET("/polo/:serviceName", DISCOVER_SERVICE)
                .GET("/polo", DISCOVER_SERVICE)
                .notFound(NOT_FOUND);
        LOG.info(router.toString());

        NioEventLoopGroup bossGroup   = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .childOption(ChannelOption.TCP_NODELAY, java.lang.Boolean.TRUE)
                    .childOption(ChannelOption.SO_KEEPALIVE, java.lang.Boolean.TRUE)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new HttpRouterServerInitializer(router));

            Channel ch = b.bind(PORT).sync().channel();
            System.out.println("Server started: http://127.0.0.1:" + PORT + '/');

            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}


