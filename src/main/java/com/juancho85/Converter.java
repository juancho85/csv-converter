package com.juancho85;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.juancho85.injection.ConverterModule;
import com.juancho85.runner.Configuration;
import com.juancho85.runner.RunnerInterface;
import lombok.extern.log4j.Log4j2;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import picocli.CommandLine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.concurrent.Callable;

import static picocli.CommandLine.Command;
import static picocli.CommandLine.Option;

@Command(description = "Reads com.juancho85.csv and transforms it using a template",
        name = "com.juancho85.csv-converter",
        mixinStandardHelpOptions = true,
        version = "com.juancho85.csv-converter 1.0")
@Log4j2
class Converter implements Callable<Integer> {

    @Option(names = {"-c", "--config-file"}, description = "Configuration file", required = true)
    private String configFilePath;

    public static void main(String... args) throws Exception {
        int exitCode = new CommandLine(new Converter()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception {
        Configuration configuration = getValidatedConfiguration();
        final Injector injector = Guice.createInjector(ConverterModule.builder()
                .templateFilePath(configuration.getTemplate())
                .outputPath(configuration.getOutput())
                .headers(configuration.getHeaders())
                .localFilePath(configuration.getFile())
                .remoteFileUrl(configuration.getUrl())
                .build()
        );
        RunnerInterface runner = injector.getInstance(RunnerInterface.class);
        runner.run();
        return 0;
    }

    private Configuration getValidatedConfiguration() throws FileNotFoundException {
        // TODO: validate configuration
        Yaml yaml = new Yaml(new Constructor(Configuration.class));
        InputStream inputStream = new FileInputStream(configFilePath);
        Configuration configuration = yaml.load(inputStream);
        log.info("Parsed configuration {}", configuration);
        return configuration;
    }

}
