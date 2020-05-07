package com.juancho85.output;

import java.util.List;
import java.util.Map;

public interface OutputAggregator {

    void handleLine(Map<String, String> parsedLine);
}
