package com.juancho85.output;

import java.util.List;
import java.util.Map;

public interface OutputAggregator {

    void handleLines(List<Map<String, String>> parsedLines);
}
