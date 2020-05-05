package com.juancho85;

import com.juancho85.output.MultiFileOutputAggregator;
import com.juancho85.output.OutputAggregator;
import com.juancho85.output.OutputType;
import com.juancho85.output.SingleFileOutputAggregator;
import com.juancho85.parser.CsvParser;
import com.juancho85.parser.ParserInterface;
import com.juancho85.parser.generated.CSV;
import com.juancho85.template.RythmTemplatingEngine;
import com.juancho85.template.TemplatingEngine;
import lombok.extern.log4j.Log4j2;
import picocli.CommandLine;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

import static picocli.CommandLine.*;

@Command(description = "Reads com.juancho85.csv and transforms it into JSON",
        name = "com.juancho85.csv-json-transformer",
        mixinStandardHelpOptions = true,
        version = "com.juancho85.csv-json-transformer 1.0")
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
        try(FileInputStream fis = new FileInputStream(file)) {
            ParserInterface parser = new CsvParser();
            CSV.parse(fis, parser);
            if(templateFilePath == null) {
                templateFilePath = this.getClass().getClassLoader().getResource("examples/templates/book.template.json").getPath();
                log.info("setting default template path {}", templateFilePath);

            }

            if(outputPath == null) {
                outputPath = this.getClass().getClassLoader().getResource("examples/output").getPath();
                log.info("setting default template path {}", outputPath);
            }

            String template = new String(Files.readAllBytes(Paths.get(templateFilePath)));
            getOutputAggregator().handleLines(getTemplatingEngine(template), parser.getParsedLines());
        }
        return 0;
    }

    private TemplatingEngine getTemplatingEngine(String template) {
        return new RythmTemplatingEngine(template);
    }

    private OutputAggregator getOutputAggregator() {
        switch(outputType) {
            case MULTI_FILE:
                return new MultiFileOutputAggregator(outputPath);
            case SINGLE_FILE:
            default:
                return new SingleFileOutputAggregator(outputPath);
        }
    }

}
