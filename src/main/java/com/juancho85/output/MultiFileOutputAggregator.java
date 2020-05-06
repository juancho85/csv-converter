package com.juancho85.output;

import com.juancho85.injection.ConverterModule;
import com.juancho85.template.TemplatingEngine;
import lombok.extern.log4j.Log4j2;

import javax.inject.Inject;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Log4j2
public class MultiFileOutputAggregator implements OutputAggregator {

    private String outputFolder;
    private TemplatingEngine engine;

    @Inject
    public MultiFileOutputAggregator(@ConverterModule.OutputPath String outputFolder, TemplatingEngine engine) {
        this.outputFolder = outputFolder;
        this.engine = engine;
    }

    @Override
    public void handleLines(List<Map<String, String>> parsedLines) {
        for(Map<String, String> parsedLine : parsedLines) {
            handleLine(engine.render(parsedLine));
        }
    }

    private void handleLine(String line) {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(String.format("%s/%s.json", this.outputFolder, UUID.randomUUID().toString())))) {
            writer.write(line);
        } catch (IOException e) {
            log.error("cannot write to output folder", e);
        }
    }
}
