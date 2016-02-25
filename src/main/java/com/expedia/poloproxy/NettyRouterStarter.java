package com.expedia.poloproxy;

import com.oracle.tools.packager.Log;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.router.Router;
import org.littleshoot.proxy.*;
import org.littleshoot.proxy.extras.SelfSignedMitmManager;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.logging.Logger;

@SuppressWarnings({"PMD.UseUtilityClass"})
public class NettyRouterStarter {

    private static final Logger LOG = Logger.getLogger(NettyRouterStarter.class.getName());
    private static final String HOST = HttpHeaders.Names.HOST;


    public static void main(String[] args) throws Exception {
        Banner.printToLog();

        Router<String> router = new Router<String>()
                .GET("/",             "Index page")
                .GET("/articles/:id", "Article show page")
                .notFound("404 Not Found");
        Log.info(router.toString());

        
    }
}


