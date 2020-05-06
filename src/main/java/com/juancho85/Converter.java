package com.juancho85;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.juancho85.injection.ConverterModule;
import com.juancho85.output.OutputType;
import com.juancho85.parser.Runner;
import lombok.extern.log4j.Log4j2;
import picocli.CommandLine;

import java.io.File;
import java.util.concurrent.Callable;

import static picocli.CommandLine.*;

@Command(description = "Reads com.juancho85.csv and transforms it using a template",
        name = "com.juancho85.csv-converter",
        mixinStandardHelpOptions = true,
        version = "com.juancho85.csv-converter 1.0")
@Log4j2
class Converter implements Callable<Integer> {

    @Parameters(index = "0", description = "The csv file.")
    private File file;

    @Option(names = {"-t", "--template-file"}, description = "Template file path")
    private String templateFilePath;

    @Option(names = {"-o", "--output-path"}, description = "Output path")
    private String outputPath;

    @Option(names = {"-ot", "--output-type"}, description = "Output type")
    private OutputType outputType = OutputType.SINGLE_FILE;

    public static void main(String... args) throws Exception {
        int exitCode = new CommandLine(new Converter()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception {
        final Injector injector = Guice.createInjector(ConverterModule.builder()
                .templateFilePath(templateFilePath)
                .outputType(outputType)
                .outputPath(outputPath)
                .file(file)
                .build()
        );
        Runner runner = injector.getInstance(Runner.class);
        runner.run();
        return 0;
    }

}
