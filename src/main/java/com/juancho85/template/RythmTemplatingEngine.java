package com.juancho85.template;

import org.rythmengine.RythmEngine;

import java.util.Map;

public class RythmTemplatingEngine implements TemplatingEngine {

    private RythmEngine engine;
    private String template;

    public RythmTemplatingEngine(String template) {
        this.template = template;
        this.engine = new RythmEngine();
    }

    @Override
    public String render(Map<String, String> values) {
        return engine.render(template, values);
    }
}
