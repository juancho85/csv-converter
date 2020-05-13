package com.juancho85.http.netty;

import com.juancho85.parser.ParserInterface;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.log4j.Log4j2;

import javax.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Log4j2
public class HttpCsvClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Inject
    private ParserInterface parser;

    public HttpCsvClientHandler(ParserInterface parser) {
        this.parser = parser;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
        // TODO: More robust parsing
        List<String> tokens = Arrays.asList(msg.toString(StandardCharsets.UTF_8).split(","));
        parser.parse(tokens);
//        log.info();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error(cause);
        ctx.close();
    }

}
