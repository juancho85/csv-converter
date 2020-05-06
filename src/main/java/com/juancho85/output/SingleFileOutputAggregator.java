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

@Log4j2
public class SingleFileOutputAggregator implements OutputAggregator {

    private String outputFolder;
    private String outputFile;
    private TemplatingEngine engine;

    @Inject
    public SingleFileOutputAggregator(@ConverterModule.OutputPath String outputFolder, TemplatingEngine engine) {
        this.outputFolder = outputFolder;
        this.engine = engine;
        this.outputFile = "singleFile.json";
    }

    @Override
    public void handleLines(List<Map<String, String>> parsedLines) {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(String.format("%s/%s", outputFolder, outputFile), true))) {
            for(int i = 0; i < parsedLines.size(); i++) {
                writer.write(engine.render(parsedLines.get(i)));
                if(i < parsedLines.size()-1) {
                    writer.write(",\n");
                }
            }
        } catch (IOException e) {
            log.error("cannot write to output folder", e);
        }
    }
}
