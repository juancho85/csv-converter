package com.juancho85.runner;

import com.juancho85.injection.ConverterModule;
import com.juancho85.parser.ParserInterface;
import com.juancho85.parser.generated.CSV;
import com.juancho85.parser.generated.ParseException;
import com.juancho85.statistics.Timed;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import javax.inject.Inject;
import java.io.FileInputStream;
import java.io.IOException;

@Log4j2
public class LocalFileRunner implements RunnerInterface {

    private String filePath;
    private ParserInterface parser;

    @Inject
    public LocalFileRunner(@ConverterModule.localFilePath String filePath, ParserInterface parser) {
        this.filePath = filePath;
        this.parser = parser;
    }

    @SneakyThrows
    @Timed
    public void run() {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            CSV.parse(fis, parser);
        } catch (IOException | ParseException e) {
            log.error("Could not parse the file", e);
            throw e;
        }
    }
}
