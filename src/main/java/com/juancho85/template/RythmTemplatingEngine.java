package com.juancho85.template;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.rythmengine.RythmEngine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

@Log4j2
public class RythmTemplatingEngine implements TemplatingEngine {

    private RythmEngine engine;
    private String template;

    public RythmTemplatingEngine(String templateFilePath) {
        this.template = getTemplate(templateFilePath);
        this.engine = new RythmEngine();
    }

    @SneakyThrows
    private String getTemplate(String templateFilePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(templateFilePath)));
        } catch (IOException e) {
            log.error("Could not read the template from the provided path {}", templateFilePath, e);
            throw e;
        }
    }

    @Override
    public String render(Map<String, String> values) {
        return engine.render(template, values);
    }
}
