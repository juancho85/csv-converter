package com.juancho85.parser;

import com.juancho85.injection.ConverterModule;
import com.juancho85.output.OutputAggregator;
import com.juancho85.parser.generated.CSV;
import com.juancho85.parser.generated.ParseException;
import com.juancho85.statistics.Timed;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import javax.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Log4j2
public class Runner {

    private File file;
    private ParserInterface parser;

    @Inject
    public Runner(@ConverterModule.CsvFile File file, ParserInterface parser) {
        this.file = file;
        this.parser = parser;
    }

    @SneakyThrows
    @Timed
    public void run() {
        try(FileInputStream fis = new FileInputStream(file)) {
            CSV.parse(fis, parser);
        } catch (IOException | ParseException e) {
            log.error("Could not parse the file", e);
            throw e;
        }
    }
}
