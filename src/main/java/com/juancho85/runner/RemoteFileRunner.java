package com.juancho85.runner;

import com.juancho85.http.hac.RemoteCsvHandler;
import com.juancho85.injection.ConverterModule;
import com.juancho85.parser.ParserInterface;
import com.juancho85.statistics.Timed;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import javax.inject.Inject;

@Log4j2
public class RemoteFileRunner implements RunnerInterface {

    private String remoteFileUrl;
    private ParserInterface parser;
    private RemoteCsvHandler remoteCsvHandler;

    @Inject
    public RemoteFileRunner(RemoteCsvHandler remoteCsvHandler, @ConverterModule.remoteFileUrl String remoteFileUrl, ParserInterface parser) {
        this.remoteFileUrl = remoteFileUrl;
        this.parser = parser;
        this.remoteCsvHandler = remoteCsvHandler;
    }

    @SneakyThrows
    @Timed
    public void run() {
        remoteCsvHandler.handleFileUrl(remoteFileUrl);
    }
}
