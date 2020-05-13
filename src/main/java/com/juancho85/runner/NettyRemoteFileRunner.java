package com.juancho85.runner;

import com.juancho85.http.hac.RemoteCsvHandler;
import com.juancho85.http.netty.HttpClient;
import com.juancho85.injection.ConverterModule;
import com.juancho85.parser.ParserInterface;
import com.juancho85.statistics.Timed;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import javax.inject.Inject;

@Log4j2
public class NettyRemoteFileRunner implements RunnerInterface {

    private String remoteFileUrl;
    private HttpClient httpClient;

    @Inject
    public NettyRemoteFileRunner(HttpClient httpClient, @ConverterModule.remoteFileUrl String remoteFileUrl) {
        this.httpClient = httpClient;
        this.remoteFileUrl = remoteFileUrl;
    }

    @SneakyThrows
    @Timed
    public void run() {
        httpClient.handleFileUrl(remoteFileUrl);
    }
}
