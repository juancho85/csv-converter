package com.juancho85.injection;

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.juancho85.http.netty.HttpClient;
import com.juancho85.output.MultiFileOutputAggregator;
import com.juancho85.output.OutputAggregator;
import com.juancho85.parser.CsvParser;
import com.juancho85.parser.ParserInterface;
import com.juancho85.runner.LocalFileRunner;
import com.juancho85.runner.NettyRemoteFileRunner;
import com.juancho85.runner.RunnerInterface;
import com.juancho85.statistics.MonitorAspect;
import com.juancho85.statistics.Timed;
import com.juancho85.template.RythmTemplatingEngine;
import com.juancho85.template.TemplatingEngine;
import lombok.Builder;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.util.List;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Builder
public class ConverterModule extends AbstractModule {

    private String templateFilePath;
    private String outputPath;
    private ParserInterface parser;
    private String remoteFileUrl;
    private String localFilePath;
    private List<String> headers;

    @Qualifier
    @Retention(RUNTIME)
    public @interface remoteFileUrl {}

    @Qualifier
    @Retention(RUNTIME)
    public @interface localFilePath {}

    @Qualifier
    @Retention(RUNTIME)
    public @interface OutputPath {}

    @Qualifier
    @Retention(RUNTIME)
    public @interface HeadersAnnotation {}

    @Override
    protected void configure() {
        if(localFilePath != null) {
            bind(RunnerInterface.class).to(LocalFileRunner.class);
            bindConstant().annotatedWith(localFilePath.class).to(localFilePath);
        } else if (remoteFileUrl != null) {
//            bind(RunnerInterface.class).to(RemoteFileRunner.class);
            bind(RunnerInterface.class).to(NettyRemoteFileRunner.class);
            bindConstant().annotatedWith(remoteFileUrl.class).to(remoteFileUrl);
        }
        bind(new TypeLiteral<List<String>>() {}).annotatedWith(HeadersAnnotation.class).toInstance(headers);
        bind(OutputAggregator.class).to(MultiFileOutputAggregator.class);
        bind(Key.get(String.class, OutputPath.class)).toInstance(outputPath);
        bind(ParserInterface.class).to(CsvParser.class);
        bind(TemplatingEngine.class).toInstance(new RythmTemplatingEngine(templateFilePath));
        bindInterceptor(Matchers.any(), Matchers.annotatedWith(Timed.class), new MonitorAspect());
        bind(HttpClient.class);
    }

}
