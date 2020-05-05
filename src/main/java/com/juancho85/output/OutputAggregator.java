package com.juancho85.output;

import com.juancho85.template.TemplatingEngine;

import java.util.List;
import java.util.Map;

public interface OutputAggregator {

    void handleLines(TemplatingEngine engine, List<Map<String, String>> parsedLines);
}
