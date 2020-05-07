package com.juancho85.injection;

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.matcher.Matchers;
import com.juancho85.output.MultiFileOutputAggregator;
import com.juancho85.output.OutputAggregator;
import com.juancho85.output.OutputType;
import com.juancho85.output.SingleFileOutputAggregator;
import com.juancho85.parser.CsvParser;
import com.juancho85.parser.ParserInterface;
import com.juancho85.statistics.MonitorAspect;
import com.juancho85.statistics.Timed;
import com.juancho85.template.RythmTemplatingEngine;
import com.juancho85.template.TemplatingEngine;
import lombok.Builder;

import javax.inject.Qualifier;
import java.io.File;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Builder
public class ConverterModule extends AbstractModule {

    private String templateFilePath;
    private OutputType outputType;
    private String outputPath;
    private ParserInterface parser;
    private File file;

    @Qualifier
    @Retention(RUNTIME)
    public @interface CsvFile {}

    @Qualifier
    @Retention(RUNTIME)
    public @interface OutputPath {}

    @Override
    protected void configure() {
        bind(Key.get(File.class, CsvFile.class)).toInstance(file);
        bind(Key.get(String.class, OutputPath.class)).toInstance(outputPath);
        bind(ParserInterface.class).to(CsvParser.class);
        bind(TemplatingEngine.class).toInstance(new RythmTemplatingEngine(templateFilePath));
        bindInterceptor(Matchers.any(), Matchers.annotatedWith(Timed.class), new MonitorAspect());
        switch(outputType) {
            case MULTI_FILE:
                bind(OutputAggregator.class).to(MultiFileOutputAggregator.class);
                break;
            case SINGLE_FILE:
            default:
                bind(OutputAggregator.class).to(SingleFileOutputAggregator.class);
        }
    }

}
