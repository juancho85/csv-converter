package com.juancho85.output;

import com.juancho85.template.TemplatingEngine;
import lombok.extern.log4j.Log4j2;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Log4j2
public class MultiFileOutputAggregator implements OutputAggregator {

    private String outputFolder;

    public MultiFileOutputAggregator(String outputFolder) {
        this.outputFolder = outputFolder;
    }

    @Override
    public void handleLines(TemplatingEngine engine, List<Map<String, String>> parsedLines) {
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
