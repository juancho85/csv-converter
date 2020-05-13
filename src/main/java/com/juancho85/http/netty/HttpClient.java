package com.juancho85.http.netty;

import com.juancho85.parser.ParserInterface;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.extern.log4j.Log4j2;

import javax.inject.Inject;
import javax.net.ssl.SSLException;
import java.net.URI;
import java.net.URISyntaxException;

@Log4j2
public class HttpClient {
    private ParserInterface parser;

    @Inject
    public HttpClient(ParserInterface parser){
        this.parser = parser;
    }

    public boolean handleFileUrl(String fileUrl) {
        try{
            URI uri = new URI(fileUrl);
            String scheme = uri.getScheme() == null ? "http" : uri.getScheme();
            String host = uri.getHost() == null ? "127.0.0.1" : uri.getHost();
            int port = uri.getPort();
            if (port == -1) {
                if ("http".equalsIgnoreCase(scheme)) {
                    port = 80;
                } else if ("https".equalsIgnoreCase(scheme)) {
                    port = 443;
                }
            }

            if (!"http".equalsIgnoreCase(scheme) && !"https".equalsIgnoreCase(scheme)) {
                log.error("Only HTTP(S) is supported.");
                return false;
            }

            // Configure SSL context if necessary.
            final boolean ssl = "https".equalsIgnoreCase(scheme);
            final SslContext sslCtx;
            if (ssl) {
                sslCtx = SslContextBuilder.forClient()
                        .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
            } else {
                sslCtx = null;
            }

            // Configure the client.
            EventLoopGroup group = new NioEventLoopGroup();
            try {
                Bootstrap b = new Bootstrap();
                b.group(group)
                        .channel(NioSocketChannel.class)
                        .handler(new HttpClientInitializer(sslCtx, parser));

                // Make the connection attempt.
                Channel ch = b.connect(host, port).sync().channel();

                // Prepare the HTTP request.
                HttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, uri.getRawPath(), Unpooled.EMPTY_BUFFER);
                request.headers().set(HttpHeaderNames.HOST, host);
                request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
                request.headers().set(HttpHeaderNames.ACCEPT_ENCODING, HttpHeaderValues.GZIP);

                // Send the HTTP request.
                ch.writeAndFlush(request);

                // Wait for the server to close the connection.
                ch.closeFuture().sync();
            } finally {
                // Shut down executor threads to exit.
                group.shutdownGracefully();
            }
            return true;
        } catch (InterruptedException | URISyntaxException | SSLException e) {
            log.error(e);
            return false;
        }
    }

}
