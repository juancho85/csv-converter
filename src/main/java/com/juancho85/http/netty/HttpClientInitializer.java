package com.juancho85.http.netty;

import com.juancho85.parser.ParserInterface;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.ssl.SslContext;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class HttpClientInitializer extends ChannelInitializer<SocketChannel> {

    private final SslContext sslCtx;
    private final ParserInterface parser;

    public HttpClientInitializer(SslContext sslCtx, ParserInterface parser) {
        this.sslCtx = sslCtx;
        this.parser = parser;
    }

    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline p = ch.pipeline();

        // Enable HTTPS if necessary.
        if (sslCtx != null) {
            p.addLast(sslCtx.newHandler(ch.alloc()));
        }

        p.addLast(new HttpClientCodec());
        // Remove the following line if you don't want automatic content decompression.
        p.addLast(new HttpContentDecompressor());
        // Uncomment the following line if you don't want to handle HttpContents.
        //p.addLast(new HttpObjectAggregator(1048576));
        p.addLast(new HttpClientHandler());
        p.addLast(new LineBasedFrameDecoder(64 * 1024));
        p.addLast(new HttpCsvClientHandler(parser));

    }
}
