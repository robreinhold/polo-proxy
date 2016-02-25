package com.expedia.poloproxy;

import io.netty.handler.codec.http.*;
import org.littleshoot.proxy.*;
import org.littleshoot.proxy.extras.SelfSignedMitmManager;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.logging.Logger;

@SuppressWarnings({"PMD.UseUtilityClass"})
public class LittleProxyStarter {

    private static final Logger LOG = Logger.getLogger(LittleProxyStarter.class.getName());
    private static final String HOST = HttpHeaders.Names.HOST;

    public static void main(String[] args) throws Exception {
        Banner.printToLog();

        HttpProxyServerBootstrap httpProxyServerBootstrap = DefaultHttpProxyServer.bootstrap()
                .withPort(5050)
//                .withAllowLocalOnly(false)
                .withFiltersSource(getFiltersSource())
                .withName("LittleProxyStarter")
//                .withServerResolver(getHostResolver())
                .withManInTheMiddle(new SelfSignedMitmManager());
        HttpProxyServer server = httpProxyServerBootstrap.start();
    }

    private static HostResolver getHostResolver() {
        return new HostResolver() {
            @Override
            public InetSocketAddress resolve(String host, int port) throws UnknownHostException {
                if("alice".equals(host)) {
                    return new InetSocketAddress("localhost", 8080);
                }
                throw new UnknownHostException("Didn't find Alice, I guess");
            }
        };
    }

    private static HttpFiltersSource getFiltersSource() {
        return new HttpFiltersSourceAdapter() {

            @Override
            public HttpFilters filterRequest(HttpRequest originalRequest) {

                return new HttpFiltersAdapter(originalRequest) {

                    @Override
                    public HttpResponse clientToProxyRequest(HttpObject httpObject) {
                        if(httpObject instanceof HttpRequest) {
                            HttpRequest request = (HttpRequest) httpObject;
                            LOG.info("Redirecting " + request.getUri());
                            if(request.getUri().substring(1).equals("alice")) {
                                request.headers().set(HOST, "localhost:8080");
                                request.setMethod(HttpMethod.CONNECT);
                            }
                        }
                        return null;
                    }
                };
            }
        };
    }
}


